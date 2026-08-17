package scenemanagement;

import enums.OverlayType;
import enums.SceneType;
import scenemanagement.overlays.Overlay;
import scenemanagement.scenes.Scene;

import javax.swing.JComponent;
import javax.swing.JLayeredPane;
import java.awt.Dimension;
import java.util.EnumMap;
import java.util.Map;

// Verwaltet die Anzeige und das Umschalten von Szenen und Overlays (Menüs, Spiel, Pause)
public class SceneManager {
    private final JLayeredPane layeredPane;
    private final Dimension size;

    private final Map<SceneType, Scene> scenes = new EnumMap<>(SceneType.class); // Liste aller Szenen
    private final Map<OverlayType, Overlay> overlays = new EnumMap<>(OverlayType.class); // Liste aller Overlays

    private SceneType activeSceneId = null; // aktuelle Szene
    private OverlayType activeOverlayId = null; // aktuelles Overlay

    // Initialisiert den SceneManager mit der Fenstergröße und bereitet den JLayeredPane vor
    public SceneManager(Dimension size) {
        this.size = size; // Fenstergröße
        this.layeredPane = new JLayeredPane(); // Erlaubt das Stapeln von Elementen (Overlays über Szenen)
        layeredPane.setPreferredSize(size);
        layeredPane.setLayout(null); // Null-Layout für absolute Positionierung
    }

    public JLayeredPane getLayeredPane() {
        return layeredPane;
    }

    // ===== Scenes =====

    // Registriert eine neue Szene und fügt sie versteckt zur unteren Ebene hinzu
    public void registerScene(SceneType id, Scene panel) {
        panel.setBounds(0, 0, size.width, size.height); // Nimmt das gesamte Fenster ein
        panel.setVisible(false); // Szene ist initial unsichtbar
        scenes.put(id, panel); // In der Map speichern
        layeredPane.add(panel, JLayeredPane.DEFAULT_LAYER); // Auf die unterste Ebene zeichnen
    }

    // Wechselt zur angegebenen Szene, pausiert die aktuelle und setzt die neue fort
    public void setScene(SceneType id) {
        // Verhindert sinnloses Umschalten auf die bereits aktive Szene
        if (activeSceneId != null && activeSceneId == id) {
            return;
        }

        // Falls momentan eine Szene läuft, pausiere deren Timer/Update-Logik
        if (activeSceneId != null) {
            Scene previous = scenes.get(activeSceneId);
            if (previous != null) {
                previous.onPause();
            }
        }

        activeSceneId = id; // Neue aktive Szene merken
        updateVisibility(scenes, id); // Macht nur die neue Szene sichtbar, versteckt die anderen

        // Fokussiert die neue Szene (wichtig für Tastatureingaben) und setzt sie fort
        Scene active = scenes.get(id);
        if (active != null) {
            active.requestFocusInWindow();
            active.onResume();
        }

        refreshLayout(); // Aktualisiert die Grafikkomponenten
    }

    // ===== Overlay Methoden =====

    // Registriert ein neues Overlay und fügt es versteckt zur oberen Ebene (Palette) hinzu
    public void registerOverlay(OverlayType id, Overlay overlay) {
        overlay.setBounds(0, 0, size.width, size.height); // Nimmt ebenfalls das gesamte Fenster ein
        overlay.setVisible(false);
        overlays.put(id, overlay);
        layeredPane.add(overlay, JLayeredPane.PALETTE_LAYER); // Wird über der Szene gezeichnet
    }

    // Zeigt ein Overlay an und pausiert ggf. die darunterliegende Szene
    public void showOverlay(OverlayType id) {
        // Falls das Overlay bereits angezeigt wird, abbruch
        if (activeOverlayId != null && activeOverlayId == id) {
            return;
        }

        // Es soll immer nur ein Overlay sichtbar sein, daher aktuelles Overlay verstecken
        if (activeOverlayId != null) {
            hideOverlay(activeOverlayId);
        }

        Overlay overlay = overlays.get(id);
        if (overlay != null) {
            activeOverlayId = id;
            overlay.setVisible(true); // Overlay einblenden

            // Darunterliegende Szene pausieren, wenn das Overlay das erfordert (z.B. bei Pausenmenü)
            if (overlay.shouldPauseUnderlying()) {
                Scene active = getActiveScene();
                if (active != null) {
                    active.onPause();
                }
            }
            overlay.requestFocusInWindow(); // Eingabefokus an das Overlay geben
        }
        refreshLayout();
    }

    // Versteckt das aktuelle Overlay und setzt die darunterliegende Szene fort
    public void hideOverlay(OverlayType id) {
        if (isOverlayVisible(id)) {
            Overlay overlay = overlays.get(id);
            if (overlay != null) {
                overlay.setVisible(false); // Overlay ausblenden
                activeOverlayId = null; // Kein aktives Overlay mehr

                // Falls die darunterliegende Szene pausiert wurde, jetzt wieder fortsetzen
                if (overlay.shouldPauseUnderlying()) {
                    Scene active = getActiveScene();
                    if (active != null) {
                        active.onResume();
                    }
                }
            }

            focusActiveScene(); // Eingabefokus zurück an die Szene geben
        }
        refreshLayout();
    }

    // Gibt zurück, ob ein bestimmtes Overlay gerade sichtbar ist
    public boolean isOverlayVisible(OverlayType id) {
        return activeOverlayId != null && activeOverlayId == id;
    }

    // ===== Hilfsmethoden =====

    // Hilfsmethode: Versteckt alle Komponenten außer der aktiven (targetKey)
    private <K, V extends JComponent> void updateVisibility(Map<K, V> components, K targetKey) {
        for (Map.Entry<K, V> entry : components.entrySet()) {
            // Setzt sichtbarkeit auf true, falls der Schlüssel übereinstimmt, andernfalls auf false
            entry.getValue().setVisible(entry.getKey().equals(targetKey));
        }
    }

    // Fordert Swing auf, das Layout und die Darstellung zu aktualisieren
    private void refreshLayout() {
        layeredPane.revalidate();
        layeredPane.repaint();
    }

    // Sucht die sichtbare Szene und übergibt ihr den Tastaturfokus
    private void focusActiveScene() {
        for (Scene scene : scenes.values()) {
            if (scene.isVisible()) {
                scene.requestFocusInWindow();
                return;
            }
        }
    }

    // Gibt das Scene-Objekt der aktuell sichtbaren Szene zurück, oder null
    private Scene getActiveScene() {
        for (Scene scene : scenes.values()) {
            if (scene.isVisible()) {
                return scene;
            }
        }
        return null;
    }
}
