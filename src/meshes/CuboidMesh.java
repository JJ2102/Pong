package meshes;

import math.Vector3;
import rendering.Mesh;

import java.util.Arrays;

/**
 * Quaderförmiges {@link Mesh} mit 8 Eckpunkten, 12 Kanten und 6 Flächen, zentriert um den Ursprung.
 */
public class CuboidMesh extends Mesh {
    /**
     * Initialisiert einen Quader (Rechteck-Mesh im 3D-Raum).
     *
     * @param xSize Ausdehnung entlang der X-Achse
     * @param ySize Ausdehnung entlang der Y-Achse
     * @param zSize Ausdehnung entlang der Z-Achse
     */
    public CuboidMesh(double xSize, double ySize, double zSize) {
        super(
                // ===== Eckpunkte (Vertices) =====
                Arrays.asList(
                        new Vector3(-xSize / 2, -ySize / 2, zSize / 2),  // Ecke 0: hinten unten links
                        new Vector3(xSize / 2, -ySize / 2, zSize / 2),   // Ecke 1: hinten unten rechts
                        new Vector3(xSize / 2, ySize / 2, zSize / 2),    // Ecke 2: hinten oben rechts
                        new Vector3(-xSize / 2, ySize / 2, zSize / 2),   // Ecke 3: hinten oben links
                        new Vector3(-xSize / 2, -ySize / 2, -zSize / 2), // Ecke 4: vorne unten links
                        new Vector3(xSize / 2, -ySize / 2, -zSize / 2),  // Ecke 5: vorne unten rechts
                        new Vector3(xSize / 2, ySize / 2, -zSize / 2),   // Ecke 6: vorne oben rechts
                        new Vector3(-xSize / 2, ySize / 2, -zSize / 2)   // Ecke 7: vorne oben links
                ),
                // ===== Kanten (Edges) =====
                new int[][] {
                        // Hintere Kanten
                        {0, 1}, {1, 2}, {2, 3}, {3, 0},
                        // Vordere Kanten
                        {4, 5}, {5, 6}, {6, 7}, {7, 4},
                        // Verbindende Kanten zwischen vorne und hinten
                        {0, 4}, {1, 5}, {2, 6}, {3, 7}
                },
                // ===== Flächen (Faces) =====
                new int[][] {
                        // Flächen werden hier als Vierecke definiert
                        {0, 1, 2, 3}, // Hinten
                        {4, 5, 6, 7}, // Vorne
                        {0, 1, 5, 4}, // Unten
                        {2, 3, 7, 6}, // Oben
                        {0, 3, 7, 4}, // Links
                        {1, 2, 6, 5}  // Rechts
                }
        );
    }
}
