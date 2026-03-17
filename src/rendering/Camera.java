package rendering;

import math.Vektor3;

public class Camera {
    private final Transform transform;
    private double fov = 0.8; // Field of View

    public Camera() {
        transform = new Transform();
        transform.position = new Vektor3(0, 0, -5);
        transform.rotation = new Vektor3(0, 0, 0);
    }

    public Transform getInvertedTransform() {
        return  transform.invert(true, true, false);
    }

    public Transform getTransform() {
        return transform;
    }

    // Getter und Setter
    public Vektor3 getPosition() {
        return transform.position;
    }

    public void setPosition(Vektor3 position) {
        this.transform.position = position;
    }

    public Vektor3 getRotation() {
        return transform.rotation;
    }

    public double getFov() {
        return fov;
    }

    public void setFov(double fov) {
        this.fov = fov;
    }
}
