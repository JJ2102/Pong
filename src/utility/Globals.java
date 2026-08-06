package utility;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Globals {
    public static Cursor getInvisibleCursor() {
        // Transparentes 16x16-Pixel-Bild als Cursor verwenden
        BufferedImage cursorImg = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);

        // Neuen unsichtbaren Cursor erstellen
        return Toolkit.getDefaultToolkit().createCustomCursor(cursorImg, new Point(0, 0), "invisibleCursor");
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
        return new Font("Arial", Font.BOLD, size);
    }

    // Zufallswerte
    public static double randomSpeed(double min, double max) {
        // zufällige Richtung: -1 oder +1
        double sign = Math.random() < 0.5 ? -1 : 1;

        // zufällige Geschwindigkeit im Bereich [min, max]
        double magnitude = min + Math.random() * (max - min);

        return sign * magnitude;
    }
}
