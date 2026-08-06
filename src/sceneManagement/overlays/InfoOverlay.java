package scenemanagement.overlays;

import enums.OverlayType;
import scenemanagement.GameWindow;
import utility.Button;
import utility.Globals;

import javax.swing.*;

public class InfoOverlay extends Overlay {
    public InfoOverlay(GameWindow window) {
        super(window, "Information", 225, false);

        JLabel controlsLabel = new JLabel( // TODO: mehr Infos ergänzen (worum es im Spiel geht, etc.)
                "<html>" +
                        "<div style='text-align: center;'>" +
                            "Use Mouse to move the paddle.<br>" +
                            "Press 'ESC' to pause the game.<br>" +
                        "</div>" +
                    "</html>");
        controlsLabel.setFont(Globals.getMainFont(24));
        controlsLabel.setForeground(Globals.getFontColor());
        addComponent(controlsLabel);

        Button cancelButton = new Button("Close");
        cancelButton.addActionListener(_ -> window.toggleOverlay(OverlayType.INFO));
        addComponent(cancelButton);

        positionComponents();
    }
}
