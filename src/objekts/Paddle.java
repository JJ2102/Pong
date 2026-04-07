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
        transform.setPosition(position);
        hitbox = new BoxHitbox(transform.getPosition() , new Vektor3(xSize*2, ySize*2, 0));
    }
}
