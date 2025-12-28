package objekts;

import meshes.RectangleMesh;
import rendering.Mesh;

import java.awt.*;

public class Box extends Entity {

    private final double depth;

    public Box(double depth) {
        super(Color.DARK_GRAY);
        this.depth = depth;
        Mesh boxMesh = new RectangleMesh(2.45, 1.4, depth); // 2.2, 1.2, 3
        this.setMesh(boxMesh);
    }
}
