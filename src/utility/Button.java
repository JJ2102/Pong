package utility;

import javax.swing.*;
import java.awt.*;

public class Button extends JButton {
    public Button(String text) {
        super(text);
        Font buttonFont = Globals.getMainFont(24);
        setFont(buttonFont);
        setBackground(new Color(0, 68, 68));
        setForeground(new Color(255, 255, 255, 179));
    }
}
