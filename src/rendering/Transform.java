package rendering;

import math.Vektor3;

public class Transform {
    public Vektor3 position;
    public Vektor3 rotation;
    public Vektor3 scale;

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
}
