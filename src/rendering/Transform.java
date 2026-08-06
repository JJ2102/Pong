package rendering;

import math.Vector3;

public class Transform {
    private Vector3 position;
    private Vector3 rotation;
    private Vector3 scale;

    public Transform() {
        this.position = new Vector3(0, 0, 0);
        this.rotation = new Vector3(0, 0, 0);
        this.scale = new Vector3(1, 1, 1);
    }

    public Transform(Vector3 position, Vector3 rotation, Vector3 scale) {
        this.position = position;
        this.rotation = rotation;
        this.scale = scale;
    }

    public Transform invert(boolean position, boolean rotation, boolean scale) {
        Transform inverted = new Transform(this.position, this.rotation, this.scale);

        if (position) inverted.position = this.position.invert();
        if (rotation) inverted.rotation = this.rotation.invert();
        if (scale) inverted.scale = this.scale.invert();

        return inverted;
    }

    // ===== Hilfsmethoden =====
    public Vector3 getPosition() {
        return position;
    }

    public void setPosition(Vector3 position) {
        this.position = position;
    }

    public Vector3 getRotation() {
        return rotation;
    }

    public void setRotation(Vector3 rotation) {
        this.rotation = rotation;
    }

    public Vector3 getScale() {
        return scale;
    }

    public void setScale(Vector3 scale) {
        this.scale = scale;
    }

    public String toString() {
        return "Transform{ " + "position=" + position + ", rotation=" + rotation + ", scale=" + scale + '}';
    }
}
