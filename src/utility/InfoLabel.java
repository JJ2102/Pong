package utility;

import javax.swing.*;
import java.awt.*;

public class InfoLabel extends JLabel {
    Font labelFont = new Font("Arial", Font.PLAIN, 18);

    public InfoLabel(String text) {
        super(text);
        setFont(labelFont);
        setForeground(Settings.getFontColor());
    }
}
