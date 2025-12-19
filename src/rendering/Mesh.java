package rendering;

import math.Vektor3;
import java.util.List;

public class Mesh {
    // 3D-Punkte
    public List<Vektor3> vertices;

    // Kanten (für Drahtgitterdarstellung)
    public int[][] edges;

    // Flächen (für gefüllte Polygone, z. B. Dreiecke)
    public int[][] faces;

    public Mesh(List<Vektor3> vertices, int[][] edges, int[][] faces) {
        this.vertices = vertices;
        this.edges = edges;
        this.faces = faces;
    }
}
