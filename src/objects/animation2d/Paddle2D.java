package objects.animation2d;

import math.Vector2;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;

// Repräsentiert ein animiertes 2D-Paddle im Hintergrund-Menü
public class Paddle2D extends Object2D {
    // Initialisiert das Paddle an der übergebenen Startposition
    public Paddle2D(Vector2 position, Dimension windowSize) {
        super(position, windowSize, 5, 70);
    }

    // Zeichnet das Paddle auf das übergebene Graphics2D-Objekt
    @Override
    public void paintMe(Graphics2D g2d) {
        double posX = getPosition().getX() - (double) width / 2;
        double posY = getPosition().getY() - (double) height / 2;

        Rectangle2D.Double paddle = new Rectangle2D.Double(posX, posY, width, height);

        g2d.setColor(Color.WHITE);
        g2d.fill(paddle);
    }

    // Bewegt das Paddle auf die angegebene Y-Position (folgt der Maus), ohne aus dem Fenster zu ragen
    public void move(double y) {
        double halfHeight = (double) height / 2;
        double clampedY = Math.clamp(y, halfHeight, windowSize.height - halfHeight);

        setPosition(new Vector2(getPosition().getX(), clampedY));
    }
}
