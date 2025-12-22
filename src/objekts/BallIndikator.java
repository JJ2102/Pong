package objekts;

import meshes.RectangleMesh;
import rendering.Mesh;

import java.awt.*;

public class BallIndikator extends Entity {
    public  BallIndikator() {
        super(Color.blue);
        Mesh boxMesh = new RectangleMesh(2.19, 1.19, 0);
        this.setMesh(boxMesh);
    }

    public void setZPos(double z) {
        this.getTransform().position.z = z;
    }
}
