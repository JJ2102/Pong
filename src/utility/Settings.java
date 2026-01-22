package utility;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Settings {
    public static Cursor getInvisibleCursor() {
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        BufferedImage cursorImage = new BufferedImage(1, 1, BufferedImage.TRANSLUCENT);
        return toolkit.createCustomCursor(cursorImage, new Point(0, 0), "invisibleCursor");
    }

    public static Color getBackgroundColor() {
        return Color.BLACK;
    }

    public static Color getFontColor() {
        return Color.GREEN;
    }
}
