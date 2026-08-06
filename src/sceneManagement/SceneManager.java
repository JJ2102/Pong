package scenemanagement;

import enums.OverlayType;
import enums.SceneType;
import scenemanagement.overlays.Overlay;
import scenemanagement.scenes.Scene;

import javax.swing.*;
import java.awt.*;
import java.util.EnumMap;
import java.util.Map;

public class SceneManager {
    private final JLayeredPane layeredPane;
    private final Dimension size;

    private final Map<SceneType, Scene> scenes = new EnumMap<>(SceneType.class); // Liste aller Szenen
    private final Map<OverlayType, Overlay> overlays = new EnumMap<>(OverlayType.class); // Liste aller Overlays

    private SceneType activeSceneID = null; // active Szene
    private OverlayType activeOverlayID = null; // actives Overlay

    public SceneManager(Dimension size) {
        this.size = size; // Fenster größe
        this.layeredPane = new JLayeredPane();
        layeredPane.setPreferredSize(size);
        layeredPane.setLayout(null);
    }

    public JLayeredPane getLayeredPane() {
        return layeredPane;
    }

    // ===== Scenes =====
    public void registerScene(SceneType id, Scene panel) {
        panel.setBounds(0, 0, size.width, size.height); // setzt die Szenen position und größe
        panel.setVisible(false); // versteckt die Szene
        scenes.put(id, panel); // speichert die Szene in einer Liste
        layeredPane.add(panel, JLayeredPane.DEFAULT_LAYER); // fügt die szene dem layeredPane hinzu
    }

    public void setScene(SceneType id) {
        // falls die Szene bereits sichtbar ist, tue nichts
        if (activeSceneID != null && activeSceneID.equals(id)) {
            return;
        }

        // falls eine Szene gezeigt wird, pausiere diese
        if (activeSceneID != null) {
            Scene previous = scenes.get(activeSceneID);
            if (previous != null) previous.onPause();
        }

        activeSceneID = id; // setzt die active Szene auf die neue
        updateVisibility(scenes, id); // zeigt die neue Szene an und versteckt die alte

        // Fokussiert die neue Szene und setzt sie fort
        Scene active = scenes.get(id);
        if (active != null) {
            active.requestFocusInWindow();
            active.onResume();
        }

        refreshLayout(); // Aktualisiert die Anzeige, damit die neue Szene zu sehen ist
    }

    // Overlay Methoden
    public void registerOverlay(OverlayType id, Overlay overlay) {
        overlay.setBounds(0, 0, size.width, size.height); // setzt die Overlay position und größe
        overlay.setVisible(false); // versteckt das Overlay
        overlays.put(id, overlay); // speichert das Overlay in einer Liste
        layeredPane.add(overlay, JLayeredPane.PALETTE_LAYER);  // fügt das Overlay dem layeredPane hinzu
    }

    public void showOverlay(OverlayType id) {
        // Falls das Overlay bereits sichtbar ist, tue nichts
        if (activeOverlayID != null && activeOverlayID.equals(id)) {
            return;
        }

        // Falls ein anderes Overlay aktiv ist,
        // verstecke das aktive Overlay (immer nur ein Overlay sichtbar)
        if (activeOverlayID != null) {
            hideOverlay(activeOverlayID);
        }

        Overlay overlay = overlays.get(id); // holt sich das overlay aus der Liste
        if (overlay != null) { // falls das overlay mit dieser ID existiert, zeige es an
            activeOverlayID = id; // setzt das active Overlay auf das neue
            overlay.setVisible(true); // zeigt neues Overlay an

            // Darunterliegende Szene pausieren, falls nötig
            if (overlay.shouldPauseUnderlying()) {
                Scene active = getActiveScene();
                if (active != null) active.onPause();
            }
            overlay.requestFocusInWindow(); // Overlay Fokussieren
        }
        refreshLayout(); // Aktualisiert die Anzeige, damit das neue Overlay zu sehen ist
    }

    // falls das overlay sichtbar ist, verstecke es
    public void hideOverlay(OverlayType id) {
        if (isOverlayVisible(id)) {

            Overlay overlay = overlays.get(id); // holt sich das overlay aus der Liste
            if (overlay != null) { // falls das overlay mit dieser ID existiert, verstecke es
                overlay.setVisible(false); // verstecke das overlay
                activeOverlayID = null; // setzt das active Overlay auf null

                // Darunterliegende Szene fortsetzen, falls nötig
                if (overlay.shouldPauseUnderlying()) {
                    Scene active = getActiveScene();
                    if (active != null) active.onResume();
                }
            }

            focusActiveScene(); // Fokussiert die darunterliegende Szene
        }
        refreshLayout(); // Aktualisiert die Anzeige, damit das Overlay nicht mehr zu sehen ist
    }

    // gibt zurück, ob ein bestimmtes Overlay sichtbar ist
    public boolean isOverlayVisible(OverlayType id) {
        return activeOverlayID != null && activeOverlayID.equals(id);
    }

    // ===== Hilfsmethoden =====
    // versteckt alle Komponenten aus einer Liste bis auf die mit dem targetKey
    private <K, V extends JComponent> void updateVisibility(Map<K, V> components, K targetKey) {
        for (Map.Entry<K, V> entry : components.entrySet()) { // geht alle komponenten durch
            entry.getValue().setVisible(entry.getKey().equals(targetKey)); // setzt sichtbarkeit auf k == targetKey
        }
    }

    // Aktualisiert die Anzeige
    private void refreshLayout() {
        layeredPane.revalidate();
        layeredPane.repaint();
    }

    // Fokussiert die darunterliegende Szene
    private void focusActiveScene() {
        for (Scene scene : scenes.values()) {
            if (scene.isVisible()) {
                scene.requestFocusInWindow();
                return;
            }
        }
    }

    // gibt die aktuell sichtbare Szene zurück,
    // falls keine zusehen ist null
    private Scene getActiveScene() {
        for (Scene scene : scenes.values()) {
            if (scene.isVisible()) return scene;
        }
        return null;
    }
}
