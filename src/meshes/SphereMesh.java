package meshes;

import math.Vector3;
import rendering.Mesh;

import java.util.ArrayList;
import java.util.List;

// Kugelförmiges Mesh, das aus Ringen und Segmenten aufgebaut wird
public class SphereMesh extends Mesh {
    // Konstruktor zur Initialisierung des Ellipsen-Meshes
    public SphereMesh(double radius, int sectors, int stacks) {

        super(
                generateVertices(radius, sectors, stacks),
                generateEdges(sectors, stacks),
                generateFaces(sectors, stacks)
        );
    }

    // ===== Mesh-Generatoren =====
    // Einen Eckpunkt (Vertex) für jeden schnittpunkt
    // eines Stacks (horizontal) und Sektors (vertikal) erzeugen
    private static List<Vector3> generateVertices(double radius, int sectors, int stacks) {
        List<Vector3> vertices = new ArrayList<>();
        double sectorStep = 2 * Math.PI / sectors;
        double stackStep = Math.PI / stacks;
        double sectorAngle, stackAngle, x, y, z, xz;

        for (int i = 0; i <= stacks; i++) {
            stackAngle = Math.PI / 2 - i * stackStep; // Von +90° bis -90°
            xz = radius * Math.cos(stackAngle);
            y = radius * Math.sin(stackAngle);

            for (int j = 0; j <= sectors; j++) {
                sectorAngle = j * sectorStep; // Von 0° bis 360°
                x = xz * Math.cos(sectorAngle);
                z = xz * Math.sin(sectorAngle);
                vertices.add(new Vector3(x, y, z));
            }
        }

        return vertices;
    }

    // Erzeugt die Kanten zwischen benachbarten Punkten
    private static int[][] generateEdges(int sectors, int stacks) {
        List<int[]> edges = new ArrayList<>();
        int cols = sectors + 1; // Spaltenanzahl

        for (int i = 0; i < stacks; i++) {
            for (int j = 0; j < sectors; j++) {
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
    private static int[][] generateFaces(int sectors, int stacks) {
        List<int[]> faces = new ArrayList<>();
        int cols = sectors + 1; // Spaltenanzahl

        for (int i = 0; i < stacks; i++) {
            for (int j = 0; j < sectors; j++) {
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
