package objekts;

import math.Vektor3;
import meshes.Ellipse;
import meshes.Rectangle;
import rendering.Mesh;

import java.awt.*;
import java.awt.geom.Rectangle2D;

public class Panel extends Entity {
    private final double xSize = 0.3;
    private final double ySize = 0.3;

    public Panel(Vektor3 position, Color color) {
        super(color);
        transform.position = position;
        Mesh panalMesh = new Rectangle(xSize, ySize, 0);
        this.setMesh(panalMesh);
    }

    public double getYSize() {
        return ySize;
    }

    public double getXSize() {
        return xSize;
    }
}
