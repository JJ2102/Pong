package utility;

import javax.swing.*;

public class InfoLabel extends JLabel {
    public InfoLabel(String text) {
        super(text);
        setFont(Globals.getMainFont(18));
        setForeground(Globals.getFontColor()); // Schriftfarbe aus Globals
    }
}
