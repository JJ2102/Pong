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
                            "Use Mouse to move the paddle.<br>" +
                            "Press 'ESC' to pause the game.<br>" +
                        "</div>" +
                    "</html>");
        controlsLabel.setFont(controlsLabel.getFont().deriveFont(24f));
        controlsLabel.setForeground(java.awt.Color.WHITE);
        addComponent(controlsLabel);

        Button cancelBtn = new Button("Close");
        cancelBtn.addActionListener(_ -> window.toggleOverlay(EnumOverlays.INFO));
        addComponent(cancelBtn);

        positionComponents();
    }
}
