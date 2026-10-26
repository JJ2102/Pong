package meshes;

import math.Vector3;
import rendering.Mesh;

import java.util.ArrayList;
import java.util.List;

/**
 * Kugelförmiges {@link Mesh}, das aus Ringen (Stacks) und Segmenten (Sektoren) aufgebaut wird.
 */
public class SphereMesh extends Mesh {
    /**
     * Initialisiert das Kugel-Mesh.
     *
     * @param radius Radius der Kugel
     * @param sectors Anzahl der vertikalen Segmente rund um die Y-Achse
     * @param stacks Anzahl der horizontalen Ringe vom Nord- zum Südpol
     */
    public SphereMesh(double radius, int sectors, int stacks) {
        super(
                generateVertices(radius, sectors, stacks),
                generateEdges(sectors, stacks),
                generateFaces(sectors, stacks)
        );
    }

    // ===== Mesh-Generatoren =====

    /**
     * Erzeugt einen Eckpunkt (Vertex) für jeden Schnittpunkt eines Stacks (horizontal)
     * und eines Sektors (vertikal).
     * <p>
     * Pro Stack entstehen {@code sectors + 1} Punkte, damit sich der Ring an der Naht schließt.
     *
     * @param radius Radius der Kugel
     * @param sectors Anzahl der Sektoren
     * @param stacks Anzahl der Stacks
     * @return Eckpunkte zeilenweise von oben nach unten
     */
    private static List<Vector3> generateVertices(double radius, int sectors, int stacks) {
        List<Vector3> vertices = new ArrayList<>();
        double sectorStep = 2 * Math.PI / sectors;
        double stackStep = Math.PI / stacks;
        double sectorAngle, stackAngle, x, y, z, xz;

        // Schleife über die Stacks (von oben nach unten)
        for (int i = 0; i <= stacks; i++) {
            stackAngle = Math.PI / 2 - i * stackStep; // Von π/2 bis −π/2
            xz = radius * Math.cos(stackAngle);
            y = radius * Math.sin(stackAngle);

            // Schleife über die Sektoren (von links nach rechts)
            for (int j = 0; j <= sectors; j++) {
                sectorAngle = j * sectorStep; // Von 0 bis 2π
                x = xz * Math.cos(sectorAngle);
                z = xz * Math.sin(sectorAngle);
                vertices.add(new Vector3(x, y, z));
            }
        }

        return vertices;
    }

    /**
     * Erzeugt die horizontalen und vertikalen Kanten zwischen benachbarten Punkten.
     *
     * @param sectors Anzahl der Sektoren
     * @param stacks Anzahl der Stacks
     * @return Kanten als Paare von Eckpunkt-Indizes
     */
    private static int[][] generateEdges(int sectors, int stacks) {
        List<int[]> edges = new ArrayList<>();
        int cols = sectors + 1; // Spaltenanzahl

        for (int i = 0; i < stacks; i++) {
            for (int j = 0; j < sectors; j++) {
                int idx = i * cols + j; // Aktueller Index
                // Horizontale Kante
                edges.add(new int[] {idx, idx + 1});
                // Vertikale Kante
                edges.add(new int[] {idx, idx + cols});
            }
        }
        return edges.toArray(new int[0][]);
    }

    /**
     * Erzeugt die Dreiecksflächen für das Mesh, zwei Dreiecke pro Viereck zwischen zwei Stacks und Sektoren.
     *
     * @param sectors Anzahl der Sektoren
     * @param stacks Anzahl der Stacks
     * @return Flächen als Tripel von Eckpunkt-Indizes
     */
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
                faces.add(new int[] {a, b, c});
                faces.add(new int[] {a, c, d});
            }
        }
        return faces.toArray(new int[0][]);
    }
}
