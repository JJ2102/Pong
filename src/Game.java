import java.awt.*;

public class Game {
    static Dimension ScreenSize = Toolkit.getDefaultToolkit().getScreenSize(); // Bildschirmgröße ermitteln

    public static void main(String[] args) {
        Window window = new Window(ScreenSize);
    }
}
