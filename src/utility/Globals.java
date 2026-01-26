package utility;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Globals {
    public static Cursor getInvisibleCursor() {
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        BufferedImage cursorImage = new BufferedImage(1, 1, BufferedImage.TRANSLUCENT);
        return toolkit.createCustomCursor(cursorImage, new Point(0, 0), "invisibleCursor");
    }

    // Farbe
    public static Color getBackgroundColor() {
        return Color.BLACK;
    }

    public static Color getFontColor() {
        return Color.GREEN;
    }
    public static Color getFontColor(int transparency) {
        return new Color(0, 255, 0, transparency);
    }

    public static Font getMainFont(int size) {
        return new Font("Arial", Font.BOLD, (int) size); // Arial
    }

    // Random Values
    public static double randomSpeed(double min, double max) {
        // zufällige Richtung: -1 oder +1
        double sign = Math.random() < 0.5 ? -1 : 1;

        // zufällige Geschwindigkeit im Bereich [min, max]
        double magnitude = min + Math.random() * (max - min);

        return sign * magnitude;
    }
}
