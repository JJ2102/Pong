package sceneManagement.overlays;

import enums.EnumOverlays;
import sceneManagement.GameWindow;
import utility.Button;

import javax.swing.*;

public class InfoOverlay extends Overlay {
    public InfoOverlay(GameWindow window) {
        super(window, "Information", 225);

        JLabel controlsLabel = new JLabel( //TODO: Update controls to correct ones
                "<html>" +
                        "<div style='text-align: center;'>" +
                            "Use Arrow Keys to Move" +
                            "<br>Press 'P' to Pause" +
                            "<br>Press 'Esc' to Exit" +
                        "</div>" +
                    "</html>");
        controlsLabel.setFont(controlsLabel.getFont().deriveFont(24f));
        controlsLabel.setForeground(java.awt.Color.WHITE);

        Button cancelBtn = new Button("Close");
        cancelBtn.addActionListener(_ -> window.toggleOverlay(EnumOverlays.INFO));
        addComponent(cancelBtn);

        addComponent(controlsLabel);

        positionComponents();
    }
}
