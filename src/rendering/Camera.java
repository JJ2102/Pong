package rendering;

import math.Vector3;

// Repräsentiert die 3D-Kamera der Szene
public class Camera {
    private final Transform transform;
    private double fov = 0.8; // Sichtfeld (Field of View)

    // Initialisiert die Kamera an einer Standardposition (z = -5)
    public Camera() {
        transform = new Transform();
        transform.setPosition(new Vector3(0, 0, -5));
        transform.setRotation(new Vector3(0, 0, 0));
    }

    // Liefert die invertierte Transformation (nötig für die Berechnung der View-Matrix)
    public Transform getInvertedTransform() {
        return transform.invert(true, true, false);
    }

    // ===== Getter und Setter =====
    public Vector3 getPosition() {
        return transform.getPosition();
    }

    public void setPosition(Vector3 position) {
        this.transform.setPosition(position);
    }

    public double getFov() {
        return fov;
    }

    public void setFov(double fov) {
        this.fov = fov;
    }
}
