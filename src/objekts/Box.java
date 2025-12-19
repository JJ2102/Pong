package objekts;

import math.Vektor3;
import meshes.Cube;
import rendering.Mesh;

public class Box extends Entity {

    public Box() {
        super();
        Mesh boxMesh = new Cube(1);
        this.setMesh(boxMesh);
        this.transform.scale = new Vektor3(1, 1, 1);
    }
}
