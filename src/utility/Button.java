package utility;

import javax.swing.JButton;
import java.awt.Color;
import java.awt.Font;

/**
 * Eigener {@link JButton}, der direkt im einheitlichen Look des Spiels erstellt wird.
 */
public class Button extends JButton {
    /**
     * Erstellt einen Button mit der Spielschrift und den festen Menüfarben.
     *
     * @param text Beschriftung des Buttons
     */
    public Button(String text) {
        super(text);
        Font buttonFont = Globals.getMainFont(24); // Schrift aus Globals, damit alle Buttons gleich aussehen
        setFont(buttonFont);
        setBackground(new Color(0, 68, 68)); // Dunkles Türkis als Hintergrund
        setForeground(new Color(255, 255, 255, 179)); // Leicht transparente weiße Schrift
    }
}
