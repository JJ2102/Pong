package utility;

import javax.swing.*;
import java.awt.*;

// Eigener JButton, der schon im einheitlichen Look des Spiels erstellt wird
public class Button extends JButton {
    // Erstellt einen Button mit der Spielschrift und den festen Menü-Farben
    public Button(String text) {
        super(text);
        Font buttonFont = Globals.getMainFont(24); // Schrift aus Globals, damit alle Buttons gleich aussehen
        setFont(buttonFont);
        setBackground(new Color(0, 68, 68)); // dunkles Türkis als Hintergrund
        setForeground(new Color(255, 255, 255, 179)); // leicht transparente weiße Schrift
    }
}
