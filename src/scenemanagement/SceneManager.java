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

/**
 * Verwaltet die Anzeige und das Umschalten von Szenen und Overlays (Menüs, Spiel, Pause).
 * Szenen liegen auf der unteren, Overlays auf der oberen Ebene eines {@link JLayeredPane}.
 */
public class SceneManager {
    private final JLayeredPane layeredPane;
    private final Dimension size;

    private final Map<SceneType, Scene> scenes = new EnumMap<>(SceneType.class); // Alle registrierten Szenen
    private final Map<OverlayType, Overlay> overlays = new EnumMap<>(OverlayType.class); // Alle registrierten Overlays

    private SceneType activeSceneType = null; // Aktuelle Szene
    private OverlayType activeOverlayType = null; // Aktuelles Overlay

    /**
     * Initialisiert den SceneManager mit der Fenstergröße und bereitet den {@link JLayeredPane} vor.
     *
     * @param size Fenstergröße, die alle Szenen und Overlays einnehmen
     */
    public SceneManager(Dimension size) {
        this.size = size;
        this.layeredPane = new JLayeredPane(); // Erlaubt das Stapeln von Elementen (Overlays über Szenen)
        layeredPane.setPreferredSize(size);
        layeredPane.setLayout(null); // Null-Layout für absolute Positionierung
    }

    /**
     * Gibt die Ebenen-Komponente zurück, in der alle Szenen und Overlays liegen.
     *
     * @return JLayeredPane mit allen registrierten Szenen und Overlays
     */
    public JLayeredPane getLayeredPane() {
        return layeredPane;
    }

    // ===== Szenen-Methoden =====

    /**
     * Registriert eine neue Szene und fügt sie versteckt zur unteren Ebene hinzu.
     *
     * @param sceneType Typ, unter dem die Szene gespeichert wird
     * @param scene zu registrierende Szene
     */
    public void registerScene(SceneType sceneType, Scene scene) {
        scene.setBounds(0, 0, size.width, size.height); // Gesamtes Fenster einnehmen
        scene.setVisible(false); // Szene ist initial unsichtbar
        scenes.put(sceneType, scene);
        layeredPane.add(scene, JLayeredPane.DEFAULT_LAYER); // Auf die unterste Ebene zeichnen
    }

    /**
     * Wechselt zur angegebenen Szene, pausiert die aktuelle und setzt die neue fort.
     * Ist die Szene bereits aktiv, passiert nichts.
     *
     * @param sceneType Typ der Szene, die angezeigt werden soll
     */
    public void setScene(SceneType sceneType) {
        // Verhindert sinnloses Umschalten auf die bereits aktive Szene
        if (activeSceneType != null && activeSceneType == sceneType) {
            return;
        }

        // Timer/Update-Logik der momentan laufenden Szene pausieren
        if (activeSceneType != null) {
            Scene previous = scenes.get(activeSceneType);
            if (previous != null) {
                previous.onPause();
            }
        }

        activeSceneType = sceneType;
        updateVisibility(scenes, sceneType); // Nur die neue Szene sichtbar machen, die anderen verstecken

        // Neue Szene fokussieren (wichtig für Tastatureingaben) und fortsetzen
        Scene active = scenes.get(sceneType);
        if (active != null) {
            active.requestFocusInWindow();
            active.onResume();
        }

        refreshLayout();
    }

    // ===== Overlay-Methoden =====

    /**
     * Registriert ein neues Overlay und fügt es versteckt zur oberen Ebene (Palette) hinzu.
     *
     * @param overlayType Typ, unter dem das Overlay gespeichert wird
     * @param overlay zu registrierendes Overlay
     */
    public void registerOverlay(OverlayType overlayType, Overlay overlay) {
        overlay.setBounds(0, 0, size.width, size.height); // Ebenfalls das gesamte Fenster einnehmen
        overlay.setVisible(false);
        overlays.put(overlayType, overlay);
        layeredPane.add(overlay, JLayeredPane.PALETTE_LAYER); // Wird über der Szene gezeichnet
    }

    /**
     * Zeigt ein Overlay an und pausiert ggf. die darunterliegende Szene.
     * Ein bereits sichtbares anderes Overlay wird vorher versteckt, da immer nur eines sichtbar ist.
     *
     * @param overlayType Typ des Overlays, das angezeigt werden soll
     */
    public void showOverlay(OverlayType overlayType) {
        // Falls das Overlay bereits angezeigt wird, Abbruch
        if (activeOverlayType != null && activeOverlayType == overlayType) {
            return;
        }

        // Es soll immer nur ein Overlay sichtbar sein, daher aktuelles Overlay verstecken
        if (activeOverlayType != null) {
            hideOverlay(activeOverlayType);
        }

        Overlay overlay = overlays.get(overlayType);
        if (overlay != null) {
            activeOverlayType = overlayType;
            overlay.setVisible(true);

            // Darunterliegende Szene pausieren, wenn das Overlay das erfordert (z. B. beim Pausenmenü)
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

    /**
     * Versteckt das angegebene Overlay, falls es gerade sichtbar ist, und setzt ggf. die darunterliegende
     * Szene fort. Der Tastaturfokus geht anschließend zurück an die sichtbare Szene.
     *
     * @param overlayType Typ des Overlays, das versteckt werden soll
     */
    public void hideOverlay(OverlayType overlayType) {
        if (isOverlayVisible(overlayType)) {
            Overlay overlay = overlays.get(overlayType);
            if (overlay != null) {
                overlay.setVisible(false);
                activeOverlayType = null;

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

    /**
     * Prüft, ob ein bestimmtes Overlay gerade sichtbar ist.
     *
     * @param overlayType Typ des zu prüfenden Overlays
     * @return {@code true}, wenn das Overlay das aktuell angezeigte ist, sonst {@code false}
     */
    public boolean isOverlayVisible(OverlayType overlayType) {
        return activeOverlayType != null && activeOverlayType == overlayType;
    }

    // ===== Hilfsmethoden =====

    /**
     * Macht nur die Komponente zum Schlüssel {@code targetKey} sichtbar und versteckt alle anderen.
     *
     * @param components alle Komponenten, nach Schlüssel geordnet
     * @param targetKey Schlüssel der Komponente, die sichtbar bleiben soll
     * @param <K> Typ der Schlüssel
     * @param <V> Typ der Komponenten
     */
    private <K, V extends JComponent> void updateVisibility(Map<K, V> components, K targetKey) {
        for (Map.Entry<K, V> entry : components.entrySet()) {
            entry.getValue().setVisible(entry.getKey().equals(targetKey));
        }
    }

    /**
     * Fordert Swing auf, das Layout und die Darstellung zu aktualisieren.
     */
    private void refreshLayout() {
        layeredPane.revalidate();
        layeredPane.repaint();
    }

    /**
     * Sucht die sichtbare Szene und übergibt ihr den Tastaturfokus.
     */
    private void focusActiveScene() {
        for (Scene scene : scenes.values()) {
            if (scene.isVisible()) {
                scene.requestFocusInWindow();
                return;
            }
        }
    }

    /**
     * Sucht die aktuell sichtbare Szene.
     *
     * @return sichtbare Szene oder {@code null}, wenn keine Szene sichtbar ist
     */
    private Scene getActiveScene() {
        for (Scene scene : scenes.values()) {
            if (scene.isVisible()) {
                return scene;
            }
        }
        return null;
    }
}
