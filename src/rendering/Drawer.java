package rendering;

import math.Vector2;

import java.awt.*;

/**
 * Hilfsklasse zum Zeichnen von 2D-Polygonen, Linien und Text.
 */
public final class Drawer {

    /**
     * Privater Konstruktor, da die reine Hilfsklasse nie instanziiert wird.
     */
    private Drawer() {
    }

    /**
     * Wandelt eine projizierte 3D-Fläche anhand ihrer Eckpunkte in ein 2D-Polygon um.
     *
     * @param face Eckpunkt-Indizes der Fläche
     * @param projectedVertices bereits auf den Bildschirm projizierte Eckpunkte des Meshes
     * @return Polygon mit den auf ganze Pixel abgeschnittenen Bildschirmkoordinaten
     */
    public static Polygon getPolygon(int[] face, Vector2[] projectedVertices) {
        Polygon poly = new Polygon(); // Initialisiere ein leeres 2D-Polygon

        for (int idx : face) { // Iteriere über alle Eckpunkt-Indizes dieser spezifischen Fläche
            Vector2 v = projectedVertices[idx]; // Hole die fertig berechneten 2D-Bildschirmkoordinaten
            poly.addPoint((int) v.getX(), (int) v.getY()); // Pixel-Koordinaten benötigen Ganzzahlen (int)
        }
        return poly;
    }

    /**
     * Zeichnet ein gefülltes 2D-Polygon in der vorgegebenen Farbe.
     *
     * @param g Grafikkontext, in den gezeichnet wird
     * @param poly zu füllendes Polygon
     * @param color Füllfarbe
     */
    public static void drawPolygon(Graphics2D g, Polygon poly, Color color) {
        g.setColor(color);
        g.fillPolygon(poly); // Füllt das Polygon deckend auf dem Canvas
    }

    /**
     * Zeichnet eine einfache Linie zwischen zwei 2D-Punkten.
     * <p>
     * Die Linienstärke legt der Aufrufer über den {@link Stroke} fest, damit sie hier nicht überschrieben wird.
     *
     * @param g Grafikkontext, in den gezeichnet wird
     * @param v1 Startpunkt in Bildschirmkoordinaten
     * @param v2 Endpunkt in Bildschirmkoordinaten
     * @param color Linienfarbe
     */
    public static void drawLine(Graphics2D g, Vector2 v1, Vector2 v2, Color color) {
        g.setColor(color);
        // Start- und Endpunkt verbinden
        g.drawLine((int) v1.getX(), (int) v1.getY(), (int) v2.getX(), (int) v2.getY());
    }

    /**
     * Zeichnet eine Linie mit einem andersfarbigen Rand drumherum.
     * <p>
     * Der {@link Stroke} des Aufrufers wird danach wiederhergestellt.
     *
     * @param g Grafikkontext, in den gezeichnet wird
     * @param x1 X-Koordinate des Startpunkts
     * @param y1 Y-Koordinate des Startpunkts
     * @param x2 X-Koordinate des Endpunkts
     * @param y2 Y-Koordinate des Endpunkts
     * @param color Farbe der eigentlichen Linie
     * @param outlineColor Farbe des Rands
     * @param thickness Stärke der eigentlichen Linie in Pixeln
     * @param outlineWidth Breite des Rands auf jeder Seite in Pixeln
     */
    public static void drawOutlinedLine(Graphics2D g, int x1, int y1, int x2, int y2,
                                  Color color, Color outlineColor, float thickness, float outlineWidth) {
        Stroke previousStroke = g.getStroke(); // Stroke des Aufrufers merken

        // Der Rand liegt auf beiden Seiten an, daher zählt die Randbreite doppelt
        g.setStroke(new BasicStroke(thickness + 2 * outlineWidth));
        g.setColor(outlineColor);
        g.drawLine(x1, y1, x2, y2);

        // Die eigentliche Linie deckt die Mitte wieder ab, außen bleibt der Rand stehen
        g.setStroke(new BasicStroke(thickness));
        g.setColor(color);
        g.drawLine(x1, y1, x2, y2);

        g.setStroke(previousStroke); // Stroke wiederherstellen, damit nachfolgendes Zeichnen unverändert bleibt
    }

    /**
     * Zeichnet Text horizontal um {@code centerX} zentriert auf der Grundlinie {@code baselineY}.
     * <p>
     * Den Font legt der Aufrufer vorher über {@code g.setFont} fest.
     *
     * @param g Grafikkontext, in den gezeichnet wird
     * @param text zu zeichnender Text
     * @param centerX X-Koordinate der horizontalen Textmitte
     * @param baselineY Y-Koordinate der Grundlinie
     * @param color Textfarbe
     */
    public static void drawCenteredString(Graphics2D g, String text, int centerX, int baselineY, Color color) {
        Rectangle bounds = getCenteredStringBounds(g, text, centerX, baselineY);
        g.setColor(color);
        g.drawString(text, bounds.x, baselineY);
    }

    /**
     * Liefert das Rechteck, das {@link #drawCenteredString} mit denselben Werten füllen würde.
     * <p>
     * Nützlich, um etwas neben oder hinter dem Text zu platzieren, ohne selbst zu messen.
     *
     * @param g Grafikkontext, dessen aktueller Font vermessen wird
     * @param text zu vermessender Text
     * @param centerX X-Koordinate der horizontalen Textmitte
     * @param baselineY Y-Koordinate der Grundlinie
     * @return Rechteck von der Oberlänge bis zur Unterlänge über die gesamte Textbreite
     */
    public static Rectangle getCenteredStringBounds(Graphics2D g, String text, int centerX, int baselineY) {
        FontMetrics fm = g.getFontMetrics(); // Liefert die Maße des aktuellen Fonts
        int textWidth = fm.stringWidth(text);

        // Die Grundlinie sitzt zwischen Ober- und Unterlänge, daher wird nach oben der Ascent
        // abgezogen und die Höhe aus Ascent und Descent zusammengesetzt
        return new Rectangle(centerX - textWidth / 2, baselineY - fm.getAscent(),
                textWidth, fm.getAscent() + fm.getDescent());
    }
}
