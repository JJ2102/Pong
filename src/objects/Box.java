package objects;

import math.Vector3;
import meshes.RectangleMesh;
import rendering.Mesh;

import java.awt.*;

// Repräsentiert die Spielfeld-Box im 3D-Raum
public class Box extends Entity {
    private final Vector3 size;

    // Initialisiert die Box anhand der vorgegebenen Tiefe
    public Box(double depth) {
        super(Color.BLACK, Color.WHITE);
        this.size = new Vector3(2.45, 1.4, depth);
        Mesh boxMesh = new RectangleMesh(size.x, size.y, size.z);
        this.setMesh(boxMesh);
    }

    // Gibt die Größe der Box als Vektor zurück
    public Vector3 getSize() {
        return size;
    }
}
