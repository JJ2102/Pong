package rendering;

import math.Vector2;

import java.awt.*;

public class Drawer {
    public static Polygon getPolygon(int[] face, Vector2[] projectedVertices) {
        Polygon poly = new Polygon();

        for (int idx : face) { // geht durch alle Eckpunkte der Fläche; idx ist der Index im Vertex-Array
            Vector2 v = projectedVertices[idx];

            // Hinzufügen des projizierten Punkts zum Polygon
            if (v != null) {
                poly.addPoint((int) v.x, (int) v.y);
            }
        }
        return poly;
    }

    public static void drawPolygon(Graphics2D g, Polygon poly, Color color) {
        if (poly.npoints >= 3) { // Nur zeichnen, wenn mindestens 3 Punkte vorhanden sind
            g.setColor(color);
            g.fillPolygon(poly);
        }
    }

    public static void drawLine(Graphics2D g, Vector2 v1, Vector2 v2, Color color) {
        g.setColor(color);
        g.setStroke(new BasicStroke(1.0f));
        g.drawLine((int) v1.x, (int) v1.y, (int) v2.x, (int) v2.y);
    }
}
