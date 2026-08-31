package objects;

import math.Vector3;
import meshes.CuboidMesh;
import rendering.Mesh;

import java.awt.Color;

// Repräsentiert die Spielfeld-Box im 3D-Raum
public class Box extends Entity {
    private final Vector3 size;

    // Initialisiert die Box anhand der vorgegebenen Tiefe
    public Box(double depth) {
        super(Color.BLACK, Color.WHITE);
        this.size = new Vector3(4.90, 2.8, depth);
        Mesh boxMesh = new CuboidMesh(size.getX(), size.getY(), size.getZ());
        setMesh(boxMesh);
    }

    // Gibt die Größe der Box als Vektor zurück
    public Vector3 getSize() {
        return size;
    }
}
