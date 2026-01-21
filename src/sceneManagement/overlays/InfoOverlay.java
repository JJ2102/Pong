package sceneManagement.overlays;

import sceneManagement.GameWindow;

import javax.swing.*;

public class InfoOverlay extends Overlay {
    public InfoOverlay(GameWindow window) {
        super(window, "Information", 225);

        JLabel controlsLabel = new JLabel("<html><div style='text-align: center;'>Use Arrow Keys to Move<br>Press 'P' to Pause<br>Press 'Esc' to Exit</div></html>");
        controlsLabel.setFont(controlsLabel.getFont().deriveFont(24f));
        controlsLabel.setForeground(java.awt.Color.WHITE);

        addComponent(controlsLabel);

        positionComponents();
    }
}
