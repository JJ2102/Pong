package sceneManagement;

import enums.EnumOverlays;
import enums.EnumScenes;
import sceneManagement.overlays.Overlay;
import sceneManagement.scenes.Scene;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

public class SceneManager {
    private final JLayeredPane layeredPane;
    private final Dimension size;
    private final Map<EnumScenes, Scene> scenes = new EnumMap<>(EnumScenes.class);
    private final Map<EnumOverlays, Overlay> overlays = new EnumMap<>(EnumOverlays.class);
    private EnumScenes activeSceneID = null;
    private EnumOverlays activeOverlayID = null;

    public SceneManager(Dimension size) {
        this.size = size;
        this.layeredPane = new JLayeredPane();
        layeredPane.setPreferredSize(size);
        layeredPane.setLayout(null);
    }

    public JLayeredPane getLayeredPane() {
        return layeredPane;
    }

    // Scene Methoden
    public void registerScene(EnumScenes id, Scene panel) {
        panel.setBounds(0, 0, size.width, size.height);
        panel.setVisible(false);
        scenes.put(id, panel);
        layeredPane.add(panel, JLayeredPane.DEFAULT_LAYER);
    }

    public void setScene(EnumScenes id) {
        if (activeSceneID != null && !activeSceneID.equals(id)) {
            Scene previous = scenes.get(activeSceneID);
            if (previous != null) previous.onPause();
        }

        activeSceneID = id;
        updateVisibility(scenes, id);
        Scene active = scenes.get(id);
        if (active != null) {
            active.requestFocusInWindow();
            active.onResume();
        }
        refreshLayout();
    }

    // Overlay Methoden
    public void registerOverlay(EnumOverlays id, Overlay overlay) {
        overlay.setBounds(0, 0, size.width, size.height);
        overlay.setVisible(false);
        overlays.put(id, overlay);
        layeredPane.add(overlay, JLayeredPane.PALETTE_LAYER);
    }

    public void showOverlay(EnumOverlays id) {
        if (activeOverlayID != null && !activeOverlayID.equals(id)) {
            hideOverlay(activeOverlayID);
        }

        Overlay overlay = overlays.get(id);
        if (overlay != null) {
            activeOverlayID = id;
            overlay.setVisible(true);

            // Pause underlying scene if needed
            if (overlay.shouldPauseUnderlying()) {
                Scene active = getActiveScene();
                if (active != null) active.onPause();
            }
            overlay.requestFocusInWindow();
        }
        refreshLayout();
    }

    public void hideOverlay(EnumOverlays id) {
        if (isOverlayVisible(id)) {
            Overlay overlay = overlays.get(id);
            if  (overlay != null) {
                overlay.setVisible(false);
                activeOverlayID = null;

                if (overlay.shouldPauseUnderlying()) {
                    Scene active = getActiveScene();
                    if (active != null) active.onResume();
                }
            }

            focusActiveScene();
        }
        refreshLayout();
    }

    public boolean isOverlayVisible(EnumOverlays id) {
        return activeOverlayID != null && activeOverlayID.equals(id);
    }

    // Helpers
    public <K, V extends JComponent> void updateVisibility(Map<K, V> components, K targetKey) {
        for (Map.Entry<K, V> entry : components.entrySet()) {
            entry.getValue().setVisible(entry.getKey().equals(targetKey));
        }
    }

    private void refreshLayout() {
        layeredPane.revalidate();
        layeredPane.repaint();
    }

    private void focusActiveScene() {
        for (Scene scene : scenes.values()) {
            if (scene.isVisible()) {
                scene.requestFocusInWindow();
                return;
            }
        }
    }

    private Scene getActiveScene() {
        for (Scene scene : scenes.values()) {
            if (scene.isVisible()) return scene;
        }
        return null;
    }
}
