package meshes;

import rendering.Mesh;
import math.Vector3;
import java.util.Arrays;

public class RectangleMesh extends Mesh {
    // Konstruktor zur Initialisierung eines Quaders (Rechteck-Mesh im 3D-Raum)
    public RectangleMesh(double xSize, double ySize, double zSize) {
        super(
                // ===== Eckpunkte (Vertices) =====
                Arrays.asList( 
                        new Vector3(-xSize, -ySize, zSize),  // Ecke 0: Vorne unten Links
                        new Vector3(xSize, -ySize, zSize),   // Ecke 1: Vorne unten Rechts
                        new Vector3(xSize, ySize, zSize),    // Ecke 2: Vorne oben Rechts
                        new Vector3(-xSize, ySize, zSize),   // Ecke 3: Vorne oben Links
                        new Vector3(-xSize, -ySize, -zSize), // Ecke 4: Hinten unten Links
                        new Vector3(xSize, -ySize, -zSize),  // Ecke 5: Hinten unten Rechts
                        new Vector3(xSize, ySize, -zSize),   // Ecke 6: Hinten oben Rechts
                        new Vector3(-xSize, ySize, -zSize)   // Ecke 7: Hinten oben Links
                ),
                // ===== Kanten (Edges) =====
                new int[][] { 
                        // Vordere Kanten
                        {0,1},{1,2},{2,3},{3,0},
                        // Hintere Kanten
                        {4,5},{5,6},{6,7},{7,4},
                        // Verbindende Kanten zwischen vorne und hinten
                        {0,4},{1,5},{2,6},{3,7}
                },
                // ===== Flächen (Faces) =====
                new int[][] { 
                        // Flächen werden hier als Vierecke definiert
                        {0,1,2,3}, // Vorne
                        {4,5,6,7}, // Hinten
                        {0,1,5,4}, // Unten
                        {2,3,7,6}, // Oben
                        {0,3,7,4}, // Links
                        {1,2,6,5}  // Rechts
                }
        );
    }
}
