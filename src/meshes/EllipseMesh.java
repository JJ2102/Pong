package meshes;

import math.Vector3;
import rendering.Mesh;

import java.util.ArrayList;
import java.util.List;

public class EllipseMesh extends Mesh {
    public EllipseMesh(double radius, int segments, int rings) {
        super(
                generateVertices(radius, segments, rings),
                generateEdges(segments, rings),
                generateFaces(segments, rings)
        );
    }

    // Erzeugt eine UV-Kugel: "rings" Breitenkreise (Pol zu Pol) x "segments" Längenkreise (rundherum).
    // Jeder Punkt wird über Kugelkoordinaten in kartesische x/y/z-Koordinaten umgerechnet:
    // phi ist der Polarwinkel (0 = Nordpol, PI = Südpol), theta der Winkel rundherum (0 bis 2*PI).
    // Die Punkte werden zeilenweise (Ring für Ring) in eine flache Liste geschrieben, sodass sie sich
    // wie ein Raster mit (rings+1) Zeilen und (segments+1) Spalten verhält - genau dieses Raster
    // nutzen generateEdges/generateFaces unten, um benachbarte Punkte per Index zu verbinden.
    private static List<Vector3> generateVertices(double r, int segments, int rings) {
        List<Vector3> verts = new ArrayList<>();
        for (int i = 0; i <= rings; i++) {
            double v = (double) i / rings;
            double phi = v * Math.PI; // von 0 (Nordpol) bis PI (Südpol)
            for (int j = 0; j <= segments; j++) {
                double u = (double) j / segments;
                double theta = u * 2.0 * Math.PI; // einmal komplett rundherum
                double x = r * Math.sin(phi) * Math.cos(theta);
                double y = r * Math.cos(phi);
                double z = r * Math.sin(phi) * Math.sin(theta);
                verts.add(new Vector3(x, y, z));
            }
        }
        return verts;
    }

    // Verbindet jeden Punkt im Raster (siehe generateVertices) mit seinem rechten und seinem unteren
    // Nachbarn; "cols" ist die Breite einer Rasterzeile, mit der sich aus dem laufenden Index idx
    // der Index des jeweiligen Nachbarn berechnen lässt (idx+1 = rechts, idx+cols = darunter)
    private static int[][] generateEdges(int segments, int rings) {
        List<int[]> edges = new ArrayList<>();
        int cols = segments + 1;
        for (int i = 0; i < rings; i++) {
            for (int j = 0; j < segments; j++) {
                int idx = i * cols + j;
                // horizontale Kante
                edges.add(new int[]{idx, idx + 1});
                // vertikale Kante
                edges.add(new int[]{idx, idx + cols});
            }
        }
        return edges.toArray(new int[0][]);
    }

    // Bildet aus je 4 benachbarten Rasterpunkten (a, b rechts daneben, c diagonal, d darunter)
    // ein Viereck und zerlegt es in 2 Dreiecke (a-b-c und a-c-d), da Meshes nur Dreiecksflächen kennen
    private static int[][] generateFaces(int segments, int rings) {
        List<int[]> faces = new ArrayList<>();
        int cols = segments + 1;
        for (int i = 0; i < rings; i++) {
            for (int j = 0; j < segments; j++) {
                int idx = i * cols + j;
                int a = idx;
                int b = idx + 1;
                int c = idx + cols + 1;
                int d = idx + cols;
                // zwei Dreiecke pro Rechteck
                faces.add(new int[]{a, b, c});
                faces.add(new int[]{a, c, d});
            }
        }
        return faces.toArray(new int[0][]);
    }
}
