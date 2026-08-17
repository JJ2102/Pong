package utility;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;

// Sammlung statischer Hilfsmethoden für Werte, die im ganzen Spiel einheitlich sein sollen (Farben, Schrift, Zufall)
public final class Globals {

    // Reine Hilfsklasse, wird nie instanziiert
    private Globals() {
    }

    // Erzeugt einen komplett durchsichtigen Cursor, damit die Maus im Spiel nicht stört
    public static Cursor getInvisibleCursor() {
        // Transparentes 16x16-Pixel-Bild als Cursor verwenden
        BufferedImage cursorImg = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);

        // Neuen unsichtbaren Cursor erstellen
        return Toolkit.getDefaultToolkit().createCustomCursor(cursorImg, new Point(0, 0), "invisibleCursor");
    }

    // ===== Farbe =====
    // Einheitliche Hintergrundfarbe aller Szenen und Overlays
    public static Color getBackgroundColor() {
        return Color.BLACK;
    }

    // Einheitliche Schriftfarbe im typischen Retro-Grün
    public static Color getFontColor() {
        return Color.GREEN;
    }

    // Gleiche Schriftfarbe, aber mit einstellbarer Transparenz (0 = unsichtbar, 255 = voll deckend)
    public static Color getFontColor(int transparency) {
        return new Color(0, 255, 0, transparency);
    }

    // Liefert die Standardschrift des Spiels in der gewünschten Größe
    public static Font getMainFont(int size) {
        return new Font("Arial", Font.BOLD, size);
    }

    // ===== Zufallswerte =====
    // Liefert einen zufälligen Geschwindigkeitswert zwischen min und max, mit zufälligem Vorzeichen
    public static double randomSpeed(double min, double max) {
        // zufällige Richtung: -1 oder +1
        double sign = Math.random() < 0.5 ? -1 : 1;

        // zufällige Geschwindigkeit im Bereich [min, max]
        double magnitude = min + Math.random() * (max - min);

        return sign * magnitude; // Betrag mit der Richtung kombinieren
    }
}
