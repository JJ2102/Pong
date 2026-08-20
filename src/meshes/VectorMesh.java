package meshes;

import math.Vector3;
import rendering.Mesh;

import java.util.Arrays;

public class VectorMesh extends Mesh {
    public VectorMesh(Vector3 direction, double length) {
        super(
                // ===== Eckpunkte (Vertices) =====
                Arrays.asList(
                        new Vector3(0, 0, 0), // Startpunkt des Vektors
                        new Vector3( // Endpunkt des Vektors
                                direction.getX() * length,
                                direction.getY() * length,
                                direction.getZ() * length
                        )
                ),
                // ===== Kanten (Edges) =====
                new int[][] {
                        {0, 1}, // Kante zwischen Start- und Endpunkt
                },
                // ===== Flächen (Faces) =====
                new int[][] {}
        );
    }
}
