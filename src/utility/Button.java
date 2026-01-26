package utility;

import javax.swing.*;
import java.awt.*;

public class Button extends JButton {
    Font btnFont = Globals.getMainFont(24);

    public Button(String text) {
        super(text);
        setFont(btnFont);
        setBackground(new Color(0, 68,68));
        setForeground(new Color(255, 255, 255, 179));
    }

    public Button(String text, Color color) {
        super(text);
        setFont(btnFont);
        setBackground(color);
    }
}
