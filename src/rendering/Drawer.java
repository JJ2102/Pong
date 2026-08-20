package rendering;

import math.Vector2;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Polygon;

// Hilfsklasse zum Zeichnen von 2D-Polygonen und Linien
public final class Drawer {

    // Reine Hilfsklasse, wird nie instanziiert
    private Drawer() {
    }

    // Wandelt eine projizierte 3D-Fläche anhand der Eckpunkte in ein 2D-Polygon um
    public static Polygon getPolygon(int[] face, Vector2[] projectedVertices) {
        Polygon poly = new Polygon(); // Initialisiere ein leeres 2D-Polygon

        for (int idx : face) { // Iteriere über alle Eckpunkt-Indizes dieser spezifischen Fläche
            Vector2 v = projectedVertices[idx]; // Hole die fertig berechneten 2D-Bildschirmkoordinaten
            poly.addPoint((int) v.getX(), (int) v.getY()); // Pixel-Koordinaten benötigen Ganzzahlen (int)
        }
        return poly;
    }

    // Zeichnet ein gefülltes 2D-Polygon in der vorgegebenen Farbe
    public static void drawPolygon(Graphics2D g, Polygon poly, Color color) {
        g.setColor(color);
        g.fillPolygon(poly); // Füllt das Polygon deckend auf dem Canvas
    }

    // Zeichnet eine einfache Linie zwischen zwei 2D-Punkten
    // Die Linienstärke legt der Aufrufer über den Stroke fest, damit sie hier nicht überschrieben wird
    public static void drawLine(Graphics2D g, Vector2 v1, Vector2 v2, Color color) {
        g.setColor(color);
        // Start- und Endpunkt verbinden
        g.drawLine((int) v1.getX(), (int) v1.getY(), (int) v2.getX(), (int) v2.getY());
    }
}
