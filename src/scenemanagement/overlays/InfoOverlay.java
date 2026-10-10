package scenemanagement.overlays;

import enums.OverlayType;
import pong.GameWindow;
import utility.Button;
import utility.Globals;

import javax.swing.JLabel;

/**
 * Overlay mit der Steuerungs- und Spielerklärung, aufrufbar aus dem Hauptmenü.
 */
public class InfoOverlay extends Overlay {
    /**
     * Baut den Erklärtext und den Schließen-Button auf.
     *
     * @param window Hauptfenster, über das das Overlay wieder ausgeblendet wird
     */
    public InfoOverlay(GameWindow window) {
        // fast deckender Hintergrund, Szene darunter läuft weiter
        super(window, "Information", 225, false, OverlayType.INFO);

        // Der Text wird als HTML gesetzt, damit Zeilenumbrüche und Fettschrift möglich sind
        JLabel controlsLabel = createControlsLabel();
        addComponent(controlsLabel);

        Button cancelButton = new Button("Close");
        cancelButton.addActionListener(_ -> window.toggleOverlay(OverlayType.INFO)); // blendet das Overlay wieder aus
        addComponent(cancelButton);

        positionComponents(); // ordnet Titel, Text und Button untereinander an
    }

    /**
     * Erstellt das Label mit der Spielanleitung als HTML-Text im Spiel-Font.
     *
     * @return Label mit dem formatierten Erklärtext
     */
    private static JLabel createControlsLabel() {
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
        return controlsLabel;
    }
}
