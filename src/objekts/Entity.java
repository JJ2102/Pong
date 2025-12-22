package objekts;

import hitboxes.BoxHitbox;
import rendering.Mesh;
import rendering.Transform;

import java.awt.*;

public class Entity {
    protected Mesh mesh;
    protected Transform transform;
    protected Color color;
    protected BoxHitbox hitbox;

    public Entity(Mesh mesh) {
        this.mesh = mesh;
        this.transform = new Transform();
    }

    public Entity(Color color) {
        this.mesh = null;
        this.transform = new Transform();
        this.color = color;
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

    public Color getColor() {
        return color;
    }

    public BoxHitbox getHitbox() {
        return hitbox;
    }
}
