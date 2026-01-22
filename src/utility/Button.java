package utility;

import javax.swing.*;
import java.awt.*;

public class Button extends JButton {
    Font btnFont = new Font("Arial", Font.BOLD, 24);

    public Button(String text) {
        super(text);
        setFont(btnFont);
        setBackground(Globals.getFontColor());
        setForeground(Globals.getBackgroundColor());
    }

    public Button(String text, Color color) {
        super(text);
        setFont(btnFont);
        setBackground(color);
    }
}
