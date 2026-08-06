package rendering;

import math.Vector3;
import java.util.List;

// Speichert die geometrischen Daten (Punkte, Kanten, Flächen) eines 3D-Objekts
public class Mesh {
    // 3D-Punkte
    private final List<Vector3> vertices; // Eckpunkte

    // Kanten (für Drahtgitterdarstellung)
    private final int[][] edges;

    // Flächen (für gefüllte Polygone, z. B. Dreiecke)
    private final int[][] faces;

    // Initialisiert ein Mesh mit den gegebenen Punkten, Kanten und Flächen
    public Mesh(List<Vector3> vertices, int[][] edges, int[][] faces) {
        this.vertices = vertices;
        this.edges = edges;
        this.faces = faces;
    }

    // ===== Getter =====
    public int[][] getFaces() {
        return faces;
    }

    public int[][] getEdges() {
        return edges;
    }

    public List<Vector3> getVertices() {
        return vertices;
    }
}
