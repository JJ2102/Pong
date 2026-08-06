package objects;

import math.Vector3;
import meshes.RectangleMesh;
import rendering.Mesh;

import java.awt.*;

public class Box extends Entity {
    private final Vector3 size;

    public Box(double depth) {
        super(Color.BLACK, Color.WHITE);
        this.size = new Vector3(2.45, 1.4, depth);
        Mesh boxMesh = new RectangleMesh(size.x, size.y, size.z);
        this.setMesh(boxMesh);
    }

    // Getter und Setter
    public Vector3 getSize() {
        return size;
    }
}
