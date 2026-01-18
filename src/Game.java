import sceneManagement.GameWindow;

import java.awt.*;

public class Game {
    static Dimension ScreenSize = Toolkit.getDefaultToolkit().getScreenSize(); // Bildschirmgröße ermitteln

    public static void main(String[] args) {
        GameWindow window = new GameWindow(ScreenSize); // GameWindow mit Bildschirmgröße erstellen
    }
}
