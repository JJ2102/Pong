package rendering;

import math.Vector3;

import java.util.List;

/**
 * Speichert die geometrischen Daten (Punkte, Kanten, Flächen) eines 3D-Objekts.
 * <p>
 * Kanten und Flächen verweisen über Indizes auf die Eckpunkte. Die Geometrie wird beim Erzeugen geprüft.
 */
public class Mesh {
    private static final int MIN_EDGE_INDICES = 2; // Eine Kante braucht Start- und Endpunkt
    private static final int MIN_FACE_INDICES = 3; // Eine Fläche braucht mindestens ein Dreieck

    // 3D-Punkte
    private final List<Vector3> vertices; // Eckpunkte

    // Kanten (für Drahtgitterdarstellung)
    private final int[][] edges;

    // Flächen (für gefüllte Polygone, z. B. Dreiecke)
    private final int[][] faces;

    /**
     * Initialisiert ein Mesh mit den gegebenen Punkten, Kanten und Flächen.
     *
     * @param vertices Eckpunkte des Meshes
     * @param edges Kanten als Paare von Eckpunkt-Indizes, darf leer sein
     * @param faces Flächen als Listen von mindestens drei Eckpunkt-Indizes, darf leer sein
     * @throws IllegalArgumentException wenn Eckpunkte fehlen oder {@code null} sind, Kanten oder Flächen
     *                                  {@code null} sind oder ein Index ungültig ist
     */
    public Mesh(List<Vector3> vertices, int[][] edges, int[][] faces) {
        // Die Geometrie wird einmal beim Erzeugen geprüft, damit der Renderer ihr danach vertrauen kann
        validate(vertices, edges, faces);

        this.vertices = vertices;
        this.edges = edges;
        this.faces = faces;
    }

    // ===== Validierung =====

    /**
     * Prüft die übergebene Geometrie auf fehlende Punkte und unbrauchbare Index-Tabellen.
     *
     * @param vertices zu prüfende Eckpunkte
     * @param edges zu prüfende Kanten
     * @param faces zu prüfende Flächen
     * @throws IllegalArgumentException wenn die Geometrie ungültig ist
     */
    private static void validate(List<Vector3> vertices, int[][] edges, int[][] faces) {
        if (vertices == null || vertices.isEmpty()) {
            throw new IllegalArgumentException("Mesh braucht mindestens einen Eckpunkt");
        }
        if (vertices.contains(null)) {
            throw new IllegalArgumentException("Mesh enthält einen Eckpunkt, der null ist");
        }
        if (edges == null || faces == null) {
            throw new IllegalArgumentException("Kanten und Flächen dürfen nicht null sein, ein leeres Array ist erlaubt");
        }

        validateIndices(edges, vertices.size(), MIN_EDGE_INDICES, "Kante");
        validateIndices(faces, vertices.size(), MIN_FACE_INDICES, "Fläche");
    }

    /**
     * Prüft eine Index-Tabelle (Kanten oder Flächen) gegen die Anzahl der vorhandenen Eckpunkte.
     *
     * @param entries zu prüfende Einträge, jeder Eintrag ist eine Liste von Eckpunkt-Indizes
     * @param vertexCount Anzahl der vorhandenen Eckpunkte
     * @param minIndices Mindestanzahl an Indizes pro Eintrag
     * @param name Bezeichnung des Eintrags für die Fehlermeldung
     * @throws IllegalArgumentException wenn ein Eintrag {@code null} ist, zu wenige Indizes hat
     *                                  oder auf einen nicht vorhandenen Eckpunkt verweist
     */
    private static void validateIndices(int[][] entries, int vertexCount, int minIndices, String name) {
        for (int[] entry : entries) {
            if (entry == null || entry.length < minIndices) {
                throw new IllegalArgumentException(name + " braucht mindestens " + minIndices + " Eckpunkte");
            }
            for (int index : entry) {
                if (index < 0 || index >= vertexCount) {
                    throw new IllegalArgumentException(name + " verweist auf den Eckpunkt " + index
                            + ", das Mesh hat aber nur " + vertexCount);
                }
            }
        }
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
