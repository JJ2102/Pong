package objekts;

import rendering.Mesh;
import rendering.Transform;

public class Entity {
    protected Mesh mesh;
    protected Transform transform;

    public Entity(Mesh mesh) {
        this.mesh = mesh;
        this.transform = new Transform();
    }

    public Entity() {
        this.mesh = null;
        this.transform = new Transform();
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
}
