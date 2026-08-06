package objects;

import hitboxes.BoxHitbox;
import math.Vector3;
import meshes.RectangleMesh;
import rendering.Mesh;

import java.awt.*;

// Basisklasse für die 3D-Paddles (Spieler und Gegner)
public class Paddle extends Entity {
    protected final double xSize = 0.3;
    protected final double ySize = 0.3;

    // Initialisiert das Paddle an der übergebenen Position mit Mesh und Hitbox
    public Paddle(Vector3 position, Color colorFace, Color colorEdge) {
        super(colorFace, colorEdge);
        Mesh panelMesh = new RectangleMesh(xSize, ySize, 0);
        this.setMesh(panelMesh);
        getTransform().setPosition(position);
        setHitbox(new BoxHitbox(getTransform().getPosition(), new Vector3(xSize*2, ySize*2, 0)));
    }
}
