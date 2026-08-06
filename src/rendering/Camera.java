package rendering;

import math.Vector3;

public class Camera {
    private final Transform transform;
    private double fov = 0.8; // Sichtfeld (Field of View)

    public Camera() {
        transform = new Transform();
        transform.setPosition(new Vector3(0, 0, -5));
        transform.setRotation(new Vector3(0, 0, 0));
    }

    // Liefert die für die View-Matrix benötigte inverse Kamera-Transformation: um die Welt relativ
    // zur Kamera auszurichten, müssen Position und Rotation der Kamera umgekehrt angewendet werden;
    // eine Skalierung ergibt für eine Kamera keinen Sinn und bleibt daher unangetastet (false)
    public Transform getInvertedTransform() {
        return transform.invert(true, true, false);
    }

    // Getter und Setter
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
