package objects;

import math.Vector3;
import meshes.CuboidMesh;
import rendering.Mesh;

import java.awt.Color;

// Repräsentiert die Spielfeld-Box im 3D-Raum
public class Box extends Entity {
    private final Vector3 size;

    // Initialisiert die Box anhand der vorgegebenen Größe
    public Box(Vector3 size) {
        super(Color.BLACK, Color.WHITE, new CuboidMesh(size.getX(), size.getY(), size.getZ()));
        this.size = size;
    }

    // Gibt die Größe der Box als Vektor zurück
    public Vector3 getSize() {
        return size;
    }
}
