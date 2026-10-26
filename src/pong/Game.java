package pong;

import java.awt.Dimension;
import java.awt.Toolkit;

/**
 * Einstiegspunkt des Spiels.
 * Ermittelt die Bildschirmgröße und öffnet damit das Hauptfenster {@link GameWindow}.
 */
public class Game {
    private static final Dimension SCREEN_SIZE = Toolkit.getDefaultToolkit().getScreenSize();

    /**
     * Startet das Spiel, indem ein {@link GameWindow} in Bildschirmgröße erstellt wird.
     *
     * @param args Kommandozeilenargumente (werden nicht verwendet)
     */
    public static void main(String[] args) {
        new GameWindow(SCREEN_SIZE);
    }
}
