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

    public void registerScene(EnumScenes id, Scene panel) {
        panel.setBounds(0, 0, size.width, size.height);
        panel.setVisible(false);
        scenes.put(id, panel);
        layeredPane.add(panel, JLayeredPane.DEFAULT_LAYER);
    }

    public void setScene(EnumScenes id) {
        for (Map.Entry<EnumScenes, Scene> entry : scenes.entrySet()) { // alle Szenen durchgehen und nur die ausgewählte sichtbar machen
            entry.getValue().setVisible(entry.getKey() == id);
        }
        Scene active = scenes.get(id);
        if (active != null) {
            active.requestFocusInWindow();
        }
        layeredPane.revalidate();
        layeredPane.repaint();
    }

    // Overlay Methoden
    public void registerOverlay(EnumOverlays id, Overlay overlay) {
        overlay.setBounds(0, 0, size.width, size.height);
        overlay.setVisible(false);
        overlays.put(id, overlay);
        layeredPane.add(overlay, JLayeredPane.PALETTE_LAYER);
    }

    public void showOverlay(EnumOverlays id) {
        for (Map.Entry<EnumOverlays, Overlay> entry : overlays.entrySet()) { // alle Overlays durchgehen und nur die ausgewählte sichtbar machen
            entry.getValue().setVisible(entry.getKey() == id);
        }
        Overlay overlay = overlays.get(id);
        if (overlay != null && !activeOverlays.contains(id)) {
            overlay.requestFocusInWindow();
            activeOverlays.add(id);
        }
        layeredPane.revalidate();
        layeredPane.repaint();
    }

    public void hideOverlay(EnumOverlays id) {
        Overlay overlay = overlays.get(id);
        if (overlay != null && activeOverlays.contains(id)) {
            overlay.setVisible(false);
            activeOverlays.remove(id);

            // focus zurück auf die aktive Szene
            for (JPanel scene : scenes.values()) {
                if (scene.isVisible()) {
                    scene.requestFocusInWindow();
                    break;
                }
            }

            layeredPane.revalidate();
            layeredPane.repaint();
        }
    }

    public boolean isOverlayVisible(EnumOverlays id) {
        return activeOverlays.contains(id);
    }
}
