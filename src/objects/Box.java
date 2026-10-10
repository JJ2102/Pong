package objects;

import math.Vector3;
import meshes.CuboidMesh;

import java.awt.Color;

/**
 * Stellt die quaderförmige Spielfeld-Box im 3D-Raum dar, in der sich Ball und Paddles bewegen.
 */
public class Box extends Entity {
    private final Vector3 size;

    /**
     * Initialisiert die Box anhand der vorgegebenen Größe.
     *
     * @param size Breite, Höhe und Tiefe der Box
     */
    public Box(Vector3 size) {
        super(Color.BLACK, Color.WHITE, new CuboidMesh(size.getX(), size.getY(), size.getZ()));
        this.size = size;
    }

    /**
     * Gibt die Größe der Box zurück.
     *
     * @return Breite, Höhe und Tiefe der Box als Vektor
     */
    public Vector3 getSize() {
        return size;
    }
}
