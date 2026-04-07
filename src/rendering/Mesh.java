package rendering;

import math.Vektor3;
import java.util.List;

public class Mesh {
    // 3D-Punkte
    private final List<Vektor3> vertices; // Eckpunkte

    // Kanten (für Drahtgitterdarstellung)
    private final int[][] edges;

    // Flächen (für gefüllte Polygone, z. B. Dreiecke)
    private final int[][] faces;

    public Mesh(List<Vektor3> vertices, int[][] edges, int[][] faces) {
        this.vertices = vertices;
        this.edges = edges;
        this.faces = faces;
    }

    public int[][] getFaces() {
        return faces;
    }

    public int[][] getEdges() {
        return edges;
    }

    public List<Vektor3> getVertices() {
        return vertices;
    }
}
