package pong;

import java.awt.Dimension;
import java.awt.Toolkit;

// Einstiegspunkt des Spiels: ermittelt die Bildschirmgröße und öffnet das Hauptfenster
public class Game {
    // Bildschirmgröße ermitteln
    private static final Dimension SCREEN_SIZE = Toolkit.getDefaultToolkit().getScreenSize();

    public static void main(String[] args) {
        new GameWindow(SCREEN_SIZE); // GameWindow mit Bildschirmgröße erstellen
    }
}
