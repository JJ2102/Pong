package objekts;

import hitboxes.BoxHitbox;
import math.Vektor2;
import math.Vektor3;
import rendering.Mesh;
import rendering.Transform;

import java.awt.*;

public class Entity {
    protected Mesh mesh;
    protected Transform transform;
    protected Color colorFace;
    protected  Color colorEdge;
    protected BoxHitbox hitbox;

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

    public  Color getEdgeColor() {return colorEdge;}

    public BoxHitbox getHitbox() {
        return hitbox;
    }

    public Vektor3 getPosition() {
        return transform.position;
    }
}
