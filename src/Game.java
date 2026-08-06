import scenemanagement.GameWindow;

import java.awt.*;

public class Game {
    private static final Dimension SCREEN_SIZE = Toolkit.getDefaultToolkit().getScreenSize(); // Bildschirmgröße ermitteln

    public static void main(String[] args) {
        new GameWindow(SCREEN_SIZE); // GameWindow mit Bildschirmgröße erstellen
    }
}
