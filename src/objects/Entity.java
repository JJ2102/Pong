package objects;

import math.Vector3;
import objects.hitboxes.BoxHitbox;
import rendering.Mesh;
import rendering.Transform;

import java.awt.Color;

/**
 * Basisklasse für alle darstellbaren Objekte in der 3D-Szene.
 * Eine Entität besteht aus einem {@link Mesh}, einem {@link Transform}, Flächen- und Kantenfarbe
 * sowie einer optionalen {@link BoxHitbox}.
 */
public class Entity {
    private final Transform transform;
    private final Color faceColor;
    private final Color edgeColor;
    private Mesh mesh;
    private BoxHitbox hitbox;

    /**
     * Initialisiert eine Entität mit dem übergebenen Mesh und den angegebenen Farben an der Startposition.
     * Eine Hitbox ist zunächst nicht gesetzt.
     *
     * @param faceColor Füllfarbe der Flächen
     * @param edgeColor Farbe der Kanten
     * @param mesh geometrisches Modell der Entität
     */
    public Entity(Color faceColor, Color edgeColor, Mesh mesh) {
        this.mesh = mesh;
        this.transform = new Transform();
        this.faceColor = faceColor;
        this.edgeColor = edgeColor;
        this.hitbox = null;
    }

    // ===== Getter und Setter =====

    public Transform getTransform() {
        return transform;
    }

    public Mesh getMesh() {
        return mesh;
    }

    public final void setMesh(Mesh mesh) {
        this.mesh = mesh;
    }

    public Color getFaceColor() {
        return faceColor;
    }

    public Color getEdgeColor() {
        return edgeColor;
    }

    /**
     * Gibt die 3D-Hitbox der Entität zurück.
     *
     * @return {@link BoxHitbox} der Entität oder {@code null}, wenn keine gesetzt ist
     */
    public BoxHitbox getHitbox() {
        return hitbox;
    }

    public final void setHitbox(BoxHitbox hitbox) {
        this.hitbox = hitbox;
    }

    /**
     * Gibt die aktuelle Position aus dem {@link Transform} zurück.
     *
     * @return aktuelle Position der Entität
     */
    public Vector3 getPosition() {
        return transform.getPosition();
    }
}
