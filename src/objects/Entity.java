package objects;

import hitboxes.BoxHitbox;
import math.Vector3;
import rendering.Mesh;
import rendering.Transform;

import java.awt.*;

// Basisklasse für alle darstellbaren Objekte in der 3D-Szene
public class Entity {
    private Mesh mesh;
    private final Transform transform;
    private final Color colorFace;
    private final Color colorEdge;
    private BoxHitbox hitbox;

    // Initialisiert eine Entität ohne Mesh an der Startposition mit den angegebenen Farben
    public Entity(Color colorFace, Color colorEdge) {
        this.mesh = null;
        this.transform = new Transform();
        this.colorFace = colorFace;
        this.colorEdge = colorEdge;
    }

    // Gibt die Transformationsdaten (Position, Rotation, Skalierung) zurück
    public Transform getTransform() {
        return transform;
    }

    // Gibt das zugeordnete Mesh zurück
    public Mesh getMesh() {
        return mesh;
    }

    // Weist der Entität ein neues Mesh zu
    public void setMesh(Mesh mesh) {
        this.mesh = mesh;
    }

    // Gibt die Füllfarbe (Face) zurück
    public Color getFaceColor() {
        return colorFace;
    }

    // Gibt die Kantenfarbe (Edge) zurück
    public Color getEdgeColor() {
        return colorEdge;
    }

    // Gibt die 3D-Hitbox der Entität zurück
    public BoxHitbox getHitbox() {
        return hitbox;
    }

    // Weist der Entität eine neue Hitbox zu
    public void setHitbox(BoxHitbox hitbox) {
        this.hitbox = hitbox;
    }

    // Hilfsmethode, die die aktuelle Position als Vektor zurückgibt
    public Vector3 getPosition() {
        return transform.getPosition();
    }
}
