package rendering;

import math.Vektor3;

public class Camera {
    private final Transform transform;
    private double fov = 0.8; // Field of View

    public Camera() {
        transform = new Transform();
        transform.setPosition(new Vektor3(0, 0, -5));
        transform.setRotation(new Vektor3(0, 0, 0));
    }

    public Transform getInvertedTransform() {
        return  transform.invert(true, true, false);
    }

    // Getter und Setter
    public Vektor3 getPosition() {
        return transform.getPosition();
    }

    public void setPosition(Vektor3 position) {
        this.transform.setPosition(position);
    }

    public double getFov() {
        return fov;
    }

    public void setFov(double fov) {
        this.fov = fov;
    }
}
