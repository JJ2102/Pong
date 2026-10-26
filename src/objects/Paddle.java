package objects;

import math.Vector3;
import meshes.CuboidMesh;
import objects.hitboxes.BoxHitbox;

import java.awt.Color;

/**
 * Basisklasse für die flachen 3D-Paddles von {@link Player} und {@link Enemy}.
 */
public class Paddle extends Entity {
    // Volle Kantenlängen des Paddles
    protected static final double X_SIZE = 0.6;
    protected static final double Y_SIZE = 0.6;

    /**
     * Initialisiert das Paddle an der übergebenen Position mit Mesh und Hitbox.
     *
     * @param position Startposition des Paddles
     * @param faceColor Füllfarbe der Fläche
     * @param edgeColor Farbe der Kanten
     */
    public Paddle(Vector3 position, Color faceColor, Color edgeColor) {
        super(faceColor, edgeColor, new CuboidMesh(X_SIZE, Y_SIZE, 0));
        getTransform().setPosition(position);
        setHitbox(new BoxHitbox(getTransform().getPosition(), new Vector3(X_SIZE, Y_SIZE, 0), Color.YELLOW));
    }
}
