package objects;

import math.Vector3;

import java.awt.*;

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
        super(position, Color.RED, new Color(255, 0, 255));

        this.positionZ = position.z;

        this.minX = -boxSize.x + xSize;
        this.maxX = boxSize.x - xSize;
        this.minY = -boxSize.y + ySize;
        this.maxY = boxSize.y - ySize;
    }

    // Beschränkt einen Wert auf den Bereich zwischen min und max
    private double clamp(double value, double min, double max) {
        return Math.clamp(value, min, max);
    }

    // Bewegt den Gegner weich (interpoliert) in Richtung der Ballposition
    public void move(Vector3 ballPosition, double difficulty) {
        this.getTransform().setPosition(this.getTransform().getPosition().leap(ballPosition, difficulty));
        this.getTransform().getPosition().z = positionZ;

        // Begrenze die Position innerhalb der Box
        getTransform().getPosition().x = clamp(getTransform().getPosition().x, minX, maxX);
        getTransform().getPosition().y = clamp(getTransform().getPosition().y, minY, maxY);

        getHitbox().setPosition(getTransform().getPosition());
    }

    // Setzt den Gegner auf seine X/Y-Startposition (in der Mitte) zurück
    public void reset() {
        getTransform().setPosition(new Vector3(0, 0, getTransform().getPosition().z));
    }
}
