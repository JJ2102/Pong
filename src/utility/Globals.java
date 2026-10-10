package utility;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;

/**
 * Sammlung statischer Hilfsmethoden für Werte, die im ganzen Spiel einheitlich sein sollen
 * (Farben, Schrift, Zufall).
 */
public final class Globals {

    /**
     * Privater Konstruktor, da die Hilfsklasse nie instanziiert wird.
     */
    private Globals() {
    }

    /**
     * Erzeugt einen komplett durchsichtigen Cursor, damit die Maus im Spiel nicht stört.
     *
     * @return unsichtbarer Cursor
     */
    public static Cursor getInvisibleCursor() {
        // Transparentes 16x16-Pixel-Bild als Cursor verwenden
        BufferedImage cursorImg = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);

        // Neuen unsichtbaren Cursor erstellen
        return Toolkit.getDefaultToolkit().createCustomCursor(cursorImg, new Point(0, 0), "invisibleCursor");
    }

    // ===== Farbe =====

    /**
     * Liefert die einheitliche Hintergrundfarbe aller Szenen und Overlays.
     *
     * @return Hintergrundfarbe (Schwarz)
     */
    public static Color getBackgroundColor() {
        return Color.BLACK;
    }

    /**
     * Liefert die einheitliche Schriftfarbe im typischen Retro-Grün.
     *
     * @return Schriftfarbe (Grün)
     */
    public static Color getFontColor() {
        return Color.GREEN;
    }

    /**
     * Liefert die gleiche Schriftfarbe, aber mit einstellbarer Transparenz.
     *
     * @param transparency Alphawert von 0 (unsichtbar) bis 255 (voll deckend)
     * @return grüne Schriftfarbe mit der angegebenen Transparenz
     */
    public static Color getFontColor(int transparency) {
        return new Color(0, 255, 0, transparency);
    }

    /**
     * Liefert die Standardschrift des Spiels in der gewünschten Größe.
     *
     * @param size Schriftgröße in Punkt
     * @return fette Arial-Schrift in der angegebenen Größe
     */
    public static Font getMainFont(int size) {
        return new Font("Arial", Font.BOLD, size);
    }

    // ===== Zufallswerte =====

    /**
     * Liefert einen zufälligen Geschwindigkeitswert zwischen {@code min} und {@code max} mit zufälligem Vorzeichen.
     *
     * @param min kleinster Betrag der Geschwindigkeit
     * @param max größter Betrag der Geschwindigkeit
     * @return zufällige Geschwindigkeit, deren Betrag in [{@code min}, {@code max}] liegt
     */
    public static double randomSpeed(double min, double max) {
        // zufällige Richtung: -1 oder +1
        double sign = Math.random() < 0.5 ? -1 : 1;

        // zufällige Geschwindigkeit im Bereich [min, max]
        double magnitude = min + Math.random() * (max - min);

        return sign * magnitude; // Betrag mit der Richtung kombinieren
    }
}
