package objekts;

import meshes.RectangleMesh;
import rendering.Mesh;

import java.awt.*;

public class Box extends Entity {

    public Box() {
        super(Color.DARK_GRAY);
        Mesh boxMesh = new RectangleMesh(2.2, 1.2, 3);
        this.setMesh(boxMesh);
    }
}
