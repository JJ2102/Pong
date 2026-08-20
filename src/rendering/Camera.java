package rendering;

import math.Matrix4x4;
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

    // Liefert die View-Matrix, die alle Objekte relativ zur Kamera ausrichtet
    public Matrix4x4 getViewMatrix() {
        return Matrix4x4.getViewMatrix(transform);
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
