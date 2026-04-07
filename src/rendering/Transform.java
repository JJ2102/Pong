package rendering;

import math.Vektor3;

public class Transform {
    private Vektor3 position;
    private Vektor3 rotation;
    private Vektor3 scale;

    public Transform() {
        this.position = new Vektor3(0, 0, 0);
        this.rotation = new Vektor3(0, 0, 0);
        this.scale = new Vektor3(1, 1, 1);
    }

    public Transform(Vektor3 position, Vektor3 rotation, Vektor3 scale) {
        this.position = position;
        this.rotation = rotation;
        this.scale = scale;
    }

    public Transform invert(boolean pos, boolean rot, boolean scale) {
        Transform inverted = new Transform(this.position, this.rotation, this.scale);

        if (pos) inverted.position = this.position.invert();
        if (rot) inverted.rotation = this.rotation.invert();
        if (scale) inverted.scale = this.scale.invert();

        return inverted;
    }

    // ===== utility =====
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

    public Vektor3 getScale() {
        return scale;
    }

    public void setScale(Vektor3 scale) {
        this.scale = scale;
    }

    public String toString() {
        return "Transform{ " + "position=" + position + ", rotation=" + rotation + ", scale=" + scale + '}';
    }
}
