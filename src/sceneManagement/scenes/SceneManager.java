package sceneManagement.scenes;

import enums.EnumScenes;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

public class SceneManager {
    private final JLayeredPane layeredPane;
    private final Dimension size;
    private final Map<EnumScenes, JPanel> scenes = new EnumMap<>(EnumScenes.class);
    private final List<JComponent> activeOverlays = new ArrayList<>();

    public SceneManager(Dimension size) {
        this.size = size;
        this.layeredPane = new JLayeredPane();
        layeredPane.setPreferredSize(size);
        layeredPane.setLayout(null);
    }

    public JLayeredPane getLayeredPane() {
        return layeredPane;
    }

    public void registerScene(EnumScenes id, JPanel panel) {
        panel.setBounds(0, 0, size.width, size.height);
        panel.setVisible(false);
        scenes.put(id, panel);
        layeredPane.add(panel, JLayeredPane.DEFAULT_LAYER);
    }

    public void setScene(EnumScenes id) {
        for (Map.Entry<EnumScenes, JPanel> entry : scenes.entrySet()) {
            entry.getValue().setVisible(entry.getKey() == id);
        }
        JPanel active = scenes.get(id);
        if (active != null) {
            active.requestFocusInWindow();
        }
        layeredPane.revalidate();
        layeredPane.repaint();
    }

    public void showOverlay(JComponent overlay) {
        if (!activeOverlays.contains(overlay)) {
            overlay.setBounds(0, 0, size.width, size.height);
            layeredPane.add(overlay, JLayeredPane.PALETTE_LAYER);
            overlay.setVisible(true);
            activeOverlays.add(overlay);
            overlay.requestFocusInWindow();
            layeredPane.revalidate();
            layeredPane.repaint();
        }
    }

    public void hideOverlay(JComponent overlay) {
        if (activeOverlays.contains(overlay)) {
            overlay.setVisible(false);
            layeredPane.remove(overlay);
            activeOverlays.remove(overlay);
            layeredPane.revalidate();
            layeredPane.repaint();
            // focus zurück auf die aktive Szene
            for (JPanel scene : scenes.values()) {
                if (scene.isVisible()) {
                    scene.requestFocusInWindow();
                    break;
                }
            }
        }
    }

    public boolean isOverlayVisible(JComponent overlay) {
        return activeOverlays.contains(overlay);
    }
}
