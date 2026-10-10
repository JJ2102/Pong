package objects.animation2d;

import math.Vector2;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;

/**
 * Stellt ein animiertes 2D-Paddle in der Hintergrund-Animation des Menüs dar.
 */
public class Paddle2D extends Object2D {
    /**
     * Initialisiert das Paddle mit fester Größe an der übergebenen Startposition.
     *
     * @param position Startposition (Mittelpunkt) des Paddles
     * @param windowSize Größe der zeichenbaren Fläche
     */
    public Paddle2D(Vector2 position, Dimension windowSize) {
        super(position, windowSize, 5, 70);
    }

    /**
     * Zeichnet das Paddle als weißes Rechteck auf das übergebene Graphics2D-Objekt.
     *
     * @param g2d Grafikkontext, auf den gezeichnet wird
     */
    @Override
    public void paintMe(Graphics2D g2d) {
        double posX = getPosition().getX() - (double) width / 2;
        double posY = getPosition().getY() - (double) height / 2;

        Rectangle2D.Double paddle = new Rectangle2D.Double(posX, posY, width, height);

        g2d.setColor(Color.WHITE);
        g2d.fill(paddle);
    }

    /**
     * Bewegt das Paddle auf die angegebene Y-Position (folgt der Maus), ohne dass es aus dem Fenster ragt.
     *
     * @param y gewünschte Y-Position des Mittelpunkts
     */
    public void move(double y) {
        double halfHeight = (double) height / 2;
        double clampedY = Math.clamp(y, halfHeight, windowSize.height - halfHeight);

        setPosition(new Vector2(getPosition().getX(), clampedY));
    }
}
