package objects;

import math.Vector3;
import rendering.Mesh;

import java.awt.Color;

public class Vector extends Entity {
    public Vector(Vector3 position, Vector3 direction, double length, Color color) {
        super(color, color, new meshes.VectorMesh(direction, length));
        getTransform().setPosition(position);
    }
}
