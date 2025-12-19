package rendering;

import math.Vektor3;

public class Camera {
    private Vektor3 position;
    private Vektor3 rotation; // Pitch, Yaw, Roll
    private double fov = 0.8; // Field of View

    public Camera() {
        this.position = new Vektor3(0, 0, -5);
        this.rotation = new Vektor3(0, 0, 0);
    }

    // Getter und Setter
    public Vektor3 getPosition() {
        return position;
    }

    public void setPosition(Vektor3 position) {
        this.position = position;
    }

    public Vektor3 getRotation() {
        return rotation;
    }

    public void setRotation(Vektor3 rotation) {
        this.rotation = rotation;
    }

    public double getFov() {
        return fov;
    }

    public void setFov(double fov) {
        this.fov = fov;
    }
}
