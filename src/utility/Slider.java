package utility;

import javax.swing.*;
import java.awt.*;

public class Slider extends JSlider {
    public Slider(int min, int max, int value) {
        super(min, max, value);
        setMajorTickSpacing((max - min) / 5);
        setMinorTickSpacing((max - min) / 20);
        setPaintTicks(true);
        setPaintLabels(true);
        setBackground(Settings.getBackgroundColor());
    }
}
