package objects;

import math.Vector3;

import java.awt.Color;

// Repräsentiert das gegnerische (KI-gesteuerte) Paddle
public class Enemy extends Paddle {
    // Min und Max-Werte für die Bewegung
    private final double minX;
    private final double maxX;
    private final double minY;
    private final double maxY;

    private final double positionZ;

    // Initialisiert den Gegner und berechnet seine Bewegungsgrenzen anhand der Boxgröße
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

    // Bewegt den Gegner weich (interpoliert) in Richtung der Ballposition
    public void move(Vector3 ballPosition, double difficulty) {
        getTransform().setPosition(getTransform().getPosition().lerp(ballPosition, difficulty));

        // Begrenze die Position innerhalb der Box, die Z-Ebene bleibt dabei fest
        Vector3 position = getTransform().getPosition();
        position.setZ(positionZ);
        position.setX(Math.clamp(position.getX(), minX, maxX));
        position.setY(Math.clamp(position.getY(), minY, maxY));

        getHitbox().setPosition(position);
    }

    // Setzt den Gegner auf seine X/Y-Startposition (in der Mitte) zurück
    public void reset() {
        getTransform().setPosition(new Vector3(0, 0, getTransform().getPosition().getZ()));
    }
}
