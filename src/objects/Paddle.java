package objects;

import hitboxes.BoxHitbox;
import math.Vector3;
import meshes.RectangleMesh;
import rendering.Mesh;

import java.awt.Color;

// Basisklasse für die 3D-Paddles (Spieler und Gegner)
public class Paddle extends Entity {
    protected static final double X_SIZE = 0.3;
    protected static final double Y_SIZE = 0.3;

    // Initialisiert das Paddle an der übergebenen Position mit Mesh und Hitbox
    public Paddle(Vector3 position, Color colorFace, Color colorEdge) {
        super(colorFace, colorEdge);
        Mesh panelMesh = new RectangleMesh(X_SIZE, Y_SIZE, 0);
        setMesh(panelMesh);
        getTransform().setPosition(position);
        setHitbox(new BoxHitbox(getTransform().getPosition(), new Vector3(X_SIZE * 2, Y_SIZE * 2, 0)));
    }
}
