package objects.animation2d;

import enums.Direction;
import math.Vector2;
import utility.Globals;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;

// Repräsentiert den animierten 2D-Ball im Hintergrund-Menü
public class Ball2D extends Object2D {
    // Initialisiert den Ball mit einer zufälligen Startgeschwindigkeit
    public Ball2D(Dimension windowSize, int size) {
        super(windowSize, size, size);
        velocity = new Vector2(Globals.randomSpeed(3, 5), Globals.randomSpeed(3, 5));
    }

    // Kehrt die horizontale Bewegungsrichtung um
    public void switchXDirection() {
        velocity.setX(velocity.getX() * -1);
    }

    // Kehrt die vertikale Bewegungsrichtung um
    public void switchYDirection() {
        velocity.setY(velocity.getY() * -1);
    }

    // Bewegt den Ball basierend auf seiner aktuellen Geschwindigkeit einen Schritt weiter
    public void move() {
        setPosition(getPosition().add(velocity));
    }

    // Prüft, ob der Ball die Paddles (links/rechts) oder den oberen/unteren Rand erreicht hat
    public Direction isOut(Dimension windowSize) {
        boolean outLeft = getPosition().getX() <= width;
        boolean outRight = getPosition().getX() >= windowSize.width - width * 2;
        boolean outTop = getPosition().getY() <= height;
        boolean outBottom = getPosition().getY() >= windowSize.height - height * 2;

        if (outLeft || outRight) {
            return Direction.X;
        } else if (outTop || outBottom) {
            return Direction.Y;
        } else {
            return Direction.NONE;
        }
    }

    // Zeichnet den Ball auf das übergebene Grafics2D-Objekt
    public void paintMe(Graphics2D g2d) {
        int posX = (int) (getPosition().getX() - (double) width / 2);
        int posY = (int) (getPosition().getY() - (double) height / 2);

        Ellipse2D.Double ball = new Ellipse2D.Double(posX, posY, width * 2, height * 2);

        g2d.setColor(Color.WHITE);
        g2d.draw(ball);
        g2d.fill(ball);
    }
}
