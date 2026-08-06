package objects;

import hitboxes.BoxHitbox;
import math.Vector3;
import rendering.Mesh;
import rendering.Transform;

import java.awt.*;

public class Entity {
    private Mesh mesh;
    private Transform transform;
    private Color colorFace;
    private Color colorEdge;
    private BoxHitbox hitbox;

    public Entity(Color colorFace, Color colorEdge) {
        this.mesh = null;
        this.transform = new Transform();
        this.colorFace = colorFace;
        this.colorEdge = colorEdge;
    }

    // Getter und Setter
    public Transform getTransform() {
        return transform;
    }

    public Mesh getMesh() {
        return mesh;
    }

    public void setMesh(Mesh mesh) {
        this.mesh = mesh;
    }

    public Color getFaceColor() {
        return colorFace;
    }

    public Color getEdgeColor() {
        return colorEdge;
    }

    public BoxHitbox getHitbox() {
        return hitbox;
    }

    public void setHitbox(BoxHitbox hitbox) {
        this.hitbox = hitbox;
    }

    public Vector3 getPosition() {
        return transform.getPosition();
    }
}
