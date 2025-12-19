package objekts;

import math.Vektor3;
import meshes.Ellipse;
import meshes.Rectangle;
import rendering.Mesh;

import java.awt.*;
import java.awt.geom.Rectangle2D;

public class Panel extends Entity {
    private final double xSize = 1;
    private final double ySize = 1;

    public Panel(Vektor3 position) {
        super(new Color(0, 0, 0, 150));
        Mesh panalMesh = new Rectangle(xSize, ySize, 0.1);
        this.setMesh(panalMesh);
    }

    public double getYSize() {
        return ySize;
    }

    public double getXSize() {
        return xSize;
    }
}
