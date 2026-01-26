package utility;

import javax.swing.*;
import java.awt.*;

public class InfoLabel extends JLabel {
    Font labelFont = Globals.getMainFont(18);

    public InfoLabel(String text) {
        super(text);
        setFont(labelFont);
        setForeground(Globals.getFontColor());
    }
}
