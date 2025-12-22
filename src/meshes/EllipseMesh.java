package meshes;

import math.Vektor3;
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

    private static List<Vektor3> generateVertices(double r, int segments, int rings) {
        List<Vektor3> verts = new ArrayList<>();
        for (int i = 0; i <= rings; i++) {
            double v = (double) i / rings;
            double phi = v * Math.PI; // von 0 bis PI
            for (int j = 0; j <= segments; j++) {
                double u = (double) j / segments;
                double theta = u * 2.0 * Math.PI; // von 0 bis 2PI
                double x = r * Math.sin(phi) * Math.cos(theta);
                double y = r * Math.cos(phi);
                double z = r * Math.sin(phi) * Math.sin(theta);
                verts.add(new Vektor3(x, y, z));
            }
        }
        return verts;
    }

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
