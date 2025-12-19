package objekts;

import meshes.Cube;
import rendering.Mesh;

public class Box extends Entity {

    public Box() {
        super();
        Mesh boxMesh = new Cube(1);
        this.setMesh(boxMesh);
    }
}
