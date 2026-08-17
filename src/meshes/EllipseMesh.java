package meshes;

import math.Vector3;
import rendering.Mesh;

import java.util.ArrayList;
import java.util.List;

// Kugelförmiges Mesh, das aus Ringen und Segmenten aufgebaut wird
public class EllipseMesh extends Mesh {
    // Konstruktor zur Initialisierung des Ellipsen-Meshes
    public EllipseMesh(double radius, int segments, int rings) {
        super(
                generateVertices(radius, segments, rings),
                generateEdges(segments, rings),
                generateFaces(segments, rings)
        );
    }

    // ===== Mesh-Generatoren =====
    // Erzeugt die Eckpunkte (Vertices) einer Kugel
    private static List<Vector3> generateVertices(double r, int segments, int rings) {
        List<Vector3> vertices = new ArrayList<>();
        // Zeilen (Ringe) durchgehen
        for (int i = 0; i <= rings; i++) {
            double v = (double) i / rings;
            double phi = v * Math.PI; // von 0 (Nordpol) bis PI (Südpol)
            // Spalten (Segmente) durchgehen
            for (int j = 0; j <= segments; j++) {
                double u = (double) j / segments;
                double theta = u * 2.0 * Math.PI; // einmal komplett rundherum

                // Kugelkoordinaten in kartesische Koordinaten umrechnen
                double x = r * Math.sin(phi) * Math.cos(theta);
                double y = r * Math.cos(phi);
                double z = r * Math.sin(phi) * Math.sin(theta);
                vertices.add(new Vector3(x, y, z));
            }
        }
        return vertices;
    }

    // Erzeugt die Kanten zwischen benachbarten Punkten
    private static int[][] generateEdges(int segments, int rings) {
        List<int[]> edges = new ArrayList<>();
        int cols = segments + 1; // Spaltenanzahl

        for (int i = 0; i < rings; i++) {
            for (int j = 0; j < segments; j++) {
                int idx = i * cols + j; // Aktueller Index
                // horizontale Kante
                edges.add(new int[]{idx, idx + 1});
                // vertikale Kante
                edges.add(new int[]{idx, idx + cols});
            }
        }
        return edges.toArray(new int[0][]);
    }

    // Erzeugt die Dreiecksflächen für das Mesh
    private static int[][] generateFaces(int segments, int rings) {
        List<int[]> faces = new ArrayList<>();
        int cols = segments + 1; // Spaltenanzahl

        for (int i = 0; i < rings; i++) {
            for (int j = 0; j < segments; j++) {
                int idx = i * cols + j; // Basis-Index

                // Indizes der vier Eckpunkte des aktuellen Vierecks
                int a = idx;
                int b = idx + 1;
                int c = idx + cols + 1;
                int d = idx + cols;

                // Zwei Dreiecke pro Rechteck erstellen (a-b-c und a-c-d)
                faces.add(new int[]{a, b, c});
                faces.add(new int[]{a, c, d});
            }
        }
        return faces.toArray(new int[0][]);
    }
}
