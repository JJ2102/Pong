package utility;

import javax.swing.JLabel;

// Eigenes JLabel für Info- und Statustexte, das direkt im Stil des Spiels erstellt wird
public class InfoLabel extends JLabel {
    // Erstellt ein Label mit der Spielschrift in kleiner Größe und der grünen Schriftfarbe
    public InfoLabel(String text) {
        super(text);
        setFont(Globals.getMainFont(18)); // Schriftart und -größe aus Globals
        setForeground(Globals.getFontColor()); // Schriftfarbe aus Globals
    }
}
