package sceneManagement;

import enums.Difficulty;
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
    private final List<EnumOverlays> activeOverlays = new ArrayList<>();
    private final Map<EnumOverlays, Overlay> overlays = new EnumMap<>(EnumOverlays.class);

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
        updateVisibility(scenes, id);
        Scene active = scenes.get(id);
        if (active != null) {
            active.requestFocusInWindow();
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
        updateVisibility(overlays, id);
        Overlay overlay = overlays.get(id);
        if (overlay != null && !activeOverlays.contains(id)) {
            overlay.requestFocusInWindow();
            activeOverlays.add(id);
        }
        refreshLayout();
    }

    public void hideOverlay(EnumOverlays id) {
        Overlay overlay = overlays.get(id);
        if (overlay != null && activeOverlays.contains(id)) {
            overlay.setVisible(false);
            activeOverlays.remove(id);
            focusActiveScene();
            refreshLayout();
        }
    }

    public boolean isOverlayVisible(EnumOverlays id) {
        return activeOverlays.contains(id);
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

}
