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

    // ===== Hilfsmethoden =====

    /**
     * Liefert die Position des Objekts.
     *
     * @return Position im 3D-Raum
     */
    public Vector3 getPosition() {
        return position;
    }

    /**
     * Setzt die Position des Objekts.
     *
     * @param position neue Position im 3D-Raum
     */
    public void setPosition(Vector3 position) {
        this.position = position;
    }

    /**
     * Liefert die Rotation des Objekts.
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

    /**
     * Liefert die Skalierung des Objekts.
     *
     * @return Skalierungsfaktoren entlang der X-, Y- und Z-Achse
     */
    public Vector3 getScale() {
        return scale;
    }

    /**
     * Setzt die Skalierung des Objekts.
     *
     * @param scale neue Skalierungsfaktoren entlang der X-, Y- und Z-Achse
     */
    public void setScale(Vector3 scale) {
        this.scale = scale;
    }

    @Override
    public String toString() {
        return "Transform{ " + "position=" + position + ", rotation=" + rotation + ", scale=" + scale + '}';
    }
}
