package objects.animation2d;

import math.Vector2;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;

// Repräsentiert ein animiertes 2D-Paddle im Hintergrund-Menü
public class Paddle2D extends Object2D {
    // Initialisiert das Paddle an der übergebenen Startposition
    public Paddle2D(Vector2 position) {
        super(position, 5, 70);
    }

    // Zeichnet das Paddle auf das übergebene Grafics2D-Objekt
    public void paintMe(Graphics2D g2d) {
        int posX = (int) (getPosition().getX() - (double) width / 2);
        int posY = (int) (getPosition().getY() - (double) height / 2);

        Rectangle r = new Rectangle(posX, posY, width, height);
        g2d.setColor(Color.WHITE);
        g2d.draw(r);
        g2d.fill(r);
    }

    // Bewegt das Paddle auf die angegebene Y-Position (folgt dem Ball)
    public void move(double y) {
        setPosition(new Vector2(getPosition().getX(), y));
    }
}
