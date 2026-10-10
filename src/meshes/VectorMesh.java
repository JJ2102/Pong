package meshes;

import math.Vector3;
import rendering.Mesh;

import java.util.Arrays;

/**
 * {@link Mesh} aus einer einzelnen Linie vom Ursprung in eine Richtung, z. B. zur Darstellung eines Vektors.
 */
public class VectorMesh extends Mesh {
    /**
     * Erzeugt eine Linie vom Ursprung bis {@code direction * length}.
     *
     * @param direction Richtung der Linie
     * @param length Faktor, mit dem die Richtung skaliert wird
     */
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
