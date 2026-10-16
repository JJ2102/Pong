package objects;

import objects.hitboxes.BoxHitbox;
import math.Vector3;
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

    /**
     * Gibt die Transformationsdaten (Position, Rotation, Skalierung) zurück.
     *
     * @return {@link Transform} der Entität
     */
    public Transform getTransform() {
        return transform;
    }

    /**
     * Gibt das zugeordnete Mesh zurück.
     *
     * @return {@link Mesh} der Entität
     */
    public Mesh getMesh() {
        return mesh;
    }

    /**
     * Weist der Entität ein neues Mesh zu.
     *
     * @param mesh neues {@link Mesh}
     */
    public final void setMesh(Mesh mesh) {
        this.mesh = mesh;
    }

    /**
     * Gibt die Füllfarbe (Face) zurück.
     *
     * @return Füllfarbe der Flächen
     */
    public Color getFaceColor() {
        return faceColor;
    }

    /**
     * Gibt die Kantenfarbe (Edge) zurück.
     *
     * @return Farbe der Kanten
     */
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

    /**
     * Weist der Entität eine neue Hitbox zu.
     *
     * @param hitbox neue {@link BoxHitbox}
     */
    public final void setHitbox(BoxHitbox hitbox) {
        this.hitbox = hitbox;
    }

    /**
     * Hilfsmethode, die die aktuelle Position aus dem {@link Transform} zurückgibt.
     *
     * @return aktuelle Position der Entität
     */
    public Vector3 getPosition() {
        return transform.getPosition();
    }
}
