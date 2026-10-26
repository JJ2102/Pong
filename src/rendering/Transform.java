package rendering;

import math.Vector3;

/**
 * Kapselt Position, Rotation und Skalierung eines Objekts im 3D-Raum.
 */
public class Transform {
    private Vector3 position;
    private Vector3 rotation;
    private Vector3 scale;

    /**
     * Initialisiert eine Standard-Transformation (Ursprung, keine Rotation, Skalierung 1).
     */
    public Transform() {
        this.position = new Vector3(0, 0, 0);
        this.rotation = new Vector3(0, 0, 0);
        this.scale = new Vector3(1, 1, 1);
    }

    // ===== Getter und Setter =====

    public Vector3 getPosition() {
        return position;
    }

    public void setPosition(Vector3 position) {
        this.position = position;
    }

    /**
     * Gibt die Rotation des Objekts zurück.
     *
     * @return Rotation um die X-, Y- und Z-Achse in Radiant
     */
    public Vector3 getRotation() {
        return rotation;
    }

    /**
     * Setzt die Rotation des Objekts.
     *
     * @param rotation neue Rotation um die X-, Y- und Z-Achse in Radiant
     */
    public void setRotation(Vector3 rotation) {
        this.rotation = rotation;
    }

    public Vector3 getScale() {
        return scale;
    }

    public void setScale(Vector3 scale) {
        this.scale = scale;
    }

    @Override
    public String toString() {
        return "Transform{ " + "position=" + position + ", rotation=" + rotation + ", scale=" + scale + '}';
    }
}
