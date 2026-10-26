package objects;

import math.Vector3;

import java.awt.Color;

/**
 * Stellt das Paddle der CPU dar, also des vom Computer gesteuerten Gegners.
 * Es folgt der Ballposition innerhalb der Box auf einer festen Z-Ebene.
 */
public class Enemy extends Paddle {
    // Min- und Max-Werte für die Bewegung
    private final double minX;
    private final double maxX;
    private final double minY;
    private final double maxY;

    private final double positionZ;

    /**
     * Initialisiert das CPU-Paddle und berechnet seine Bewegungsgrenzen anhand der Boxgröße.
     *
     * @param position Startposition des CPU-Paddles, deren Z-Wert fest beibehalten wird
     * @param boxSize Größe der Spielfeld-Box
     */
    public Enemy(Vector3 position, Vector3 boxSize) {
        super(position, Color.RED, Color.PINK);

        this.positionZ = position.getZ();

        // Grenzen gelten für den Mittelpunkt, daher zählen von Box und Paddle jeweils die halben Ausdehnungen
        Vector3 halfBoxSize = boxSize.divide(2);
        this.minX = -halfBoxSize.getX() + X_SIZE / 2;
        this.maxX = halfBoxSize.getX() - X_SIZE / 2;
        this.minY = -halfBoxSize.getY() + Y_SIZE / 2;
        this.maxY = halfBoxSize.getY() - Y_SIZE / 2;
    }

    /**
     * Bewegt das CPU-Paddle weich (linear interpoliert) in Richtung der Ballposition und begrenzt es auf die Box.
     *
     * @param ballPosition aktuelle Position des Balls
     * @param difficulty Interpolationsfaktor; je größer, desto schneller folgt die CPU dem Ball
     */
    public void move(Vector3 ballPosition, double difficulty) {
        getTransform().setPosition(getTransform().getPosition().lerp(ballPosition, difficulty));

        // Position auf die Box begrenzen, die Z-Ebene bleibt dabei fest
        Vector3 position = getTransform().getPosition();
        position.setZ(positionZ);
        position.setX(Math.clamp(position.getX(), minX, maxX));
        position.setY(Math.clamp(position.getY(), minY, maxY));

        getHitbox().setPosition(position);
    }

    /**
     * Setzt das CPU-Paddle auf seine X/Y-Startposition in der Mitte zurück.
     */
    public void reset() {
        getTransform().setPosition(new Vector3(0, 0, getTransform().getPosition().getZ()));
    }
}
