package objekts;

import math.Vektor3;
import meshes.RectangleMesh;
import rendering.Mesh;

import java.awt.*;

public class Box extends Entity {
    private final Vektor3 size;

    public Box(double depth) {
        super(Color.BLACK, Color.WHITE);
        this.size = new Vektor3(2.45, 1.4, depth);
        Mesh boxMesh = new RectangleMesh(size.x, size.y, size.z);
        this.setMesh(boxMesh);
    }

    // Getter und Setter
    public Vektor3 getSize() {
        return size;
    }
}
