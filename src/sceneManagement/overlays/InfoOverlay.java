package sceneManagement.overlays;

import enums.EnumOverlays;
import sceneManagement.GameWindow;
import utility.Button;
import utility.Globals;

import javax.swing.*;

public class InfoOverlay extends Overlay {
    public InfoOverlay(GameWindow window) {
        super(window, "Information", 225, false);

        JLabel controlsLabel = new JLabel( // TODO: add more info (what is the Game about, etc.)
                "<html>" +
                        "<div style='text-align: center;'>" +
                            "Use Mouse to move the paddle.<br>" +
                            "Press 'ESC' to pause the game.<br>" +
                        "</div>" +
                    "</html>");
        controlsLabel.setFont(Globals.getMainFont(24));
        controlsLabel.setForeground(Globals.getFontColor());
        addComponent(controlsLabel);

        Button cancelBtn = new Button("Close");
        cancelBtn.addActionListener(_ -> window.toggleOverlay(EnumOverlays.INFO));
        addComponent(cancelBtn);

        positionComponents();
    }
}
