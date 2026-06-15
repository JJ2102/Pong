package sceneManagement.overlays;

import enums.EnumOverlays;
import sceneManagement.GameWindow;
import utility.Button;
import utility.Globals;

import javax.swing.*;

public class InfoOverlay extends Overlay {
    public InfoOverlay(GameWindow window) {
        super(window, "Information", 225, false);

        JLabel controlsLabel = new JLabel("""
    <html>
        <div style='text-align: center;'>
            <b>How to Play</b><br><br>

            Move the paddle with your mouse.<br>
            Block incoming shots and outplay your opponent.<br>
            Earn points whenever the ball gets past the opponent's paddle.<br>
            Press <b>ESC</b> to pause the game.<br><br>

            <i>Inspired by the classic Atari game Pong.</i>
        </div>
    </html>
    """);
        controlsLabel.setFont(Globals.getMainFont(24));
        controlsLabel.setForeground(Globals.getFontColor());
        addComponent(controlsLabel);

        Button cancelBtn = new Button("Close");
        cancelBtn.addActionListener(_ -> window.toggleOverlay(EnumOverlays.INFO));
        addComponent(cancelBtn);

        positionComponents();
    }
}
