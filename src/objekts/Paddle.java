package objekts;

import hitboxes.BoxHitbox;
import math.Vektor3;
import meshes.RectangleMesh;
import rendering.Mesh;

import java.awt.*;

public class Paddle extends Entity {
    protected final double xSize = 0.3;
    protected final double ySize = 0.3;

    public Paddle(Vektor3 position, Color colorFace, Color colorEdge) {
        super(colorFace, colorEdge);
        Mesh panalMesh = new RectangleMesh(xSize, ySize, 0);
        this.setMesh(panalMesh);
        transform.position = position;
        hitbox = new BoxHitbox(transform.position , new Vektor3(xSize*2, ySize*2, 0));
    }

    public double getYSize() {
        return ySize;
    }

    public double getXSize() {
        return xSize;
    }
}
