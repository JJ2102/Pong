package objekts;

import math.Vektor3;
import meshes.Rectangle;
import rendering.Mesh;

import java.awt.*;

public class Box extends Entity {

    public Box() {
        super(Color.DARK_GRAY);
        Mesh boxMesh = new Rectangle(2.2, 1.2, 3);
        this.setMesh(boxMesh);
    }
}
