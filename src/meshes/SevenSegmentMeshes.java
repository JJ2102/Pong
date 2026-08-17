package meshes;

import math.Vector3;
import rendering.Mesh;

import java.util.ArrayList;
import java.util.List;

// Erzeugt die Meshes einer Siebensegmentanzeige (zwei Ziffern mit Doppelpunkt)
public final class SevenSegmentMeshes {
    // ===== NUR DIESE WERTE ANPASSEN =====
    private static final float DIGIT_WIDTH = 0.6f;
    private static final float DIGIT_HEIGHT = 1.0f;
    private static final float SEGMENT_THICKNESS = 0.10f;
    private static final float SEGMENT_GAP = 0.02f;
    // ====================================

    // ===== Interne Konstanten zur Positionierung =====
    private static final float HORIZONTAL_SEGMENT_WIDTH = DIGIT_WIDTH * 0.85f;
    private static final float VERTICAL_SEGMENT_HEIGHT = (DIGIT_HEIGHT - SEGMENT_THICKNESS * 3 - SEGMENT_GAP * 2) / 2f;

    // Y-Positionen der horizontalen Segmente
    private static final float TOP_SEGMENT_Y = DIGIT_HEIGHT / 2f - SEGMENT_THICKNESS / 2f;
    private static final float MIDDLE_SEGMENT_Y = 0.0f;
    private static final float BOTTOM_SEGMENT_Y = -DIGIT_HEIGHT / 2f + SEGMENT_THICKNESS / 2f;

    // Y-Positionen der vertikalen Segmente
    private static final float UPPER_VERTICAL_Y = (TOP_SEGMENT_Y + MIDDLE_SEGMENT_Y) / 2f;
    private static final float LOWER_VERTICAL_Y = (MIDDLE_SEGMENT_Y + BOTTOM_SEGMENT_Y) / 2f;

    // X-Position der rechten Segmente
    private static final float RIGHT_SEGMENT_X = DIGIT_WIDTH / 2f - SEGMENT_THICKNESS / 2f;

    // Einstellungen für den Doppelpunkt (Colon)
    private static final float COLON_DOT_SIZE = SEGMENT_THICKNESS * 0.75f;
    private static final float COLON_DOT_Y_OFFSET = 0.22f;
    private static final int COLON_SEGMENTS = 8;
    private static final float CHARACTER_SPACING = 0.75f;

    // ===== Segment-Definitionen =====
    // Layout der 7 Segmente: x, y, breite, höhe, isHorizontal
    // Segmentanordnung wie klassisch üblich (A=oben, B=rechts-oben, C=rechts-unten,
    // D=unten, E=links-unten, F=links-oben, G=Mitte)
    private static final float[][] SEGMENT_CONFIGS = {
            {0f, TOP_SEGMENT_Y, HORIZONTAL_SEGMENT_WIDTH, SEGMENT_THICKNESS, 1}, // A (Oben)
            {RIGHT_SEGMENT_X, UPPER_VERTICAL_Y, SEGMENT_THICKNESS, VERTICAL_SEGMENT_HEIGHT, 0}, // B (Rechts oben)
            {RIGHT_SEGMENT_X, LOWER_VERTICAL_Y, SEGMENT_THICKNESS, VERTICAL_SEGMENT_HEIGHT, 0}, // C (Rechts unten)
            {0f, BOTTOM_SEGMENT_Y, HORIZONTAL_SEGMENT_WIDTH, SEGMENT_THICKNESS, 1}, // D (Unten)
            {-RIGHT_SEGMENT_X, LOWER_VERTICAL_Y, SEGMENT_THICKNESS, VERTICAL_SEGMENT_HEIGHT, 0}, // E (Links unten)
            {-RIGHT_SEGMENT_X, UPPER_VERTICAL_Y, SEGMENT_THICKNESS, VERTICAL_SEGMENT_HEIGHT, 0}, // F (Links oben)
            {0f, MIDDLE_SEGMENT_Y, HORIZONTAL_SEGMENT_WIDTH, SEGMENT_THICKNESS, 1}  // G (Mitte)
    };

    // Mapping, welche Segmente für welche Ziffer (0-9) aktiv sind
    private static final boolean[][] DIGIT_SEGMENTS = {
            {true,  true,  true,  true,  true,  true,  false}, // 0
            {false, true,  true,  false, false, false, false}, // 1
            {true,  true,  false, true,  true,  false, true }, // 2
            {true,  true,  true,  true,  false, false, true }, // 3
            {false, true,  true,  false, false, true,  true }, // 4
            {true,  false, true,  true,  false, true,  true }, // 5
            {true,  false, true,  true,  true,  true,  true }, // 6
            {true,  true,  true,  false, false, false, false}, // 7
            {true,  true,  true,  true,  true,  true,  true }, // 8
            {true,  true,  true,  true,  false, true,  true }  // 9
    };

    // Reine Hilfsklasse, wird nie instanziiert
    private SevenSegmentMeshes() {
    }

    // ===== Mesh-Generatoren =====
    // Generiert ein Mesh für eine einzelne Ziffer (0-9)
    public static Mesh getDigitMesh(int digit) {
        if (digit < 0 || digit > 9) {
            digit = 0; // Fallback auf 0
        }

        List<Vector3> vertices = new ArrayList<>();
        List<int[]> edges = new ArrayList<>();
        List<int[]> faces = new ArrayList<>();

        boolean[] activeSegments = DIGIT_SEGMENTS[digit];
        // Geht alle 7 Segmente durch und fügt sie hinzu, falls aktiv
        for (int i = 0; i < 7; i++) {
            if (activeSegments[i]) {
                float[] config = SEGMENT_CONFIGS[i];
                boolean isHorizontal = config[4] > 0.5f;
                addRoundedSegment(vertices, edges, faces, config[0], config[1], config[2], config[3], isHorizontal);
            }
        }

        return new Mesh(vertices, edges.toArray(new int[0][]), faces.toArray(new int[0][]));
    }

    // Generiert ein Mesh für einen Doppelpunkt
    public static Mesh getColonMesh() {
        List<Vector3> vertices = new ArrayList<>();
        List<int[]> edges = new ArrayList<>();
        List<int[]> faces = new ArrayList<>();

        // Fügt zwei Punkte für den Doppelpunkt hinzu (einen oben, einen unten)
        addColonDot(vertices, edges, faces, COLON_DOT_Y_OFFSET);
        addColonDot(vertices, edges, faces, -COLON_DOT_Y_OFFSET);

        return new Mesh(vertices, edges.toArray(new int[0][]), faces.toArray(new int[0][]));
    }

    // Generiert das gesamte Anzeige-Mesh mit linker Ziffer, Doppelpunkt und rechter Ziffer
    public static Mesh getDisplayMesh(int leftDigit, int rightDigit) {
        List<Vector3> vertices = new ArrayList<>();
        List<int[]> edges = new ArrayList<>();
        List<int[]> faces = new ArrayList<>();

        // Fügt die einzelnen Bestandteile übersetzt an ihren Platz hinzu
        addMeshTranslated(vertices, edges, faces, getDigitMesh(leftDigit), -CHARACTER_SPACING, 0f);
        addMeshTranslated(vertices, edges, faces, getColonMesh(), 0f, 0f);
        addMeshTranslated(vertices, edges, faces, getDigitMesh(rightDigit), CHARACTER_SPACING, 0f);

        return new Mesh(vertices, edges.toArray(new int[0][]), faces.toArray(new int[0][]));
    }

    // ===== Hilfsmethoden zur Geometrie-Erstellung =====
    // Erzeugt ein Segment als Sechseck mit abgeschrägten Ecken (chamfer), damit die Segmente
    // wie bei einer echten Siebensegmentanzeige spitz zueinander zulaufen, statt rechteckig zu wirken
    private static void addRoundedSegment(List<Vector3> vertices, List<int[]> edges, List<int[]> faces,
                                          float centerX, float centerY, float width, float height,
                                          boolean isHorizontal) {
        int baseIndex = vertices.size(); // Startindex für die neuen Punkte
        float halfWidth = width / 2f;
        float halfHeight = height / 2f;
        float chamfer = SEGMENT_THICKNESS * 0.4f;

        if (isHorizontal) {
            // Eckpunkte für ein horizontales Segment setzen
            vertices.add(new Vector3(centerX - halfWidth + chamfer, centerY + halfHeight, 0));
            vertices.add(new Vector3(centerX + halfWidth - chamfer, centerY + halfHeight, 0));
            vertices.add(new Vector3(centerX + halfWidth, centerY, 0));
            vertices.add(new Vector3(centerX + halfWidth - chamfer, centerY - halfHeight, 0));
            vertices.add(new Vector3(centerX - halfWidth + chamfer, centerY - halfHeight, 0));
            vertices.add(new Vector3(centerX - halfWidth, centerY, 0));
        } else {
            // Eckpunkte für ein vertikales Segment setzen
            vertices.add(new Vector3(centerX, centerY + halfHeight, 0));
            vertices.add(new Vector3(centerX + halfWidth, centerY + halfHeight - chamfer, 0));
            vertices.add(new Vector3(centerX + halfWidth, centerY - halfHeight + chamfer, 0));
            vertices.add(new Vector3(centerX, centerY - halfHeight, 0));
            vertices.add(new Vector3(centerX - halfWidth, centerY - halfHeight + chamfer, 0));
            vertices.add(new Vector3(centerX - halfWidth, centerY + halfHeight - chamfer, 0));
        }

        // Verbindet die Punkte zu einem Sechseck
        addHexagon(edges, faces, baseIndex);
    }

    // Erzeugt einen runden Punkt für den Doppelpunkt
    private static void addColonDot(List<Vector3> vertices, List<int[]> edges, List<int[]> faces, float yOffset) {
        int baseIndex = vertices.size(); // Startindex für die neuen Punkte

        // Berechnet die Eckpunkte für einen Kreis
        for (int i = 0; i < COLON_SEGMENTS; i++) {
            double angle = 2 * Math.PI * i / COLON_SEGMENTS;
            float x = (float)(Math.cos(angle) * COLON_DOT_SIZE / 2f);
            float y = yOffset + (float)(Math.sin(angle) * COLON_DOT_SIZE / 2f);
            vertices.add(new Vector3(x, y, 0));
        }

        // Verbindet die Punkte zu einem Polygon
        addPolygon(edges, faces, baseIndex, COLON_SEGMENTS);
    }

    // Fügt ein bestehendes Mesh (sourceMesh) verschoben in ein Ziel-Mesh ein
    private static void addMeshTranslated(List<Vector3> targetVertices, List<int[]> targetEdges,
                                          List<int[]> targetFaces, Mesh sourceMesh,
                                          float translateX, float translateY) {
        if (sourceMesh == null) {
            return;
        }

        int baseIndex = targetVertices.size(); // Basis-Index zur Anpassung der Kanten/Flächen

        // Punkte kopieren und verschieben
        if (sourceMesh.getVertices() != null) {
            for (Vector3 vertex : sourceMesh.getVertices()) {
                targetVertices.add(new Vector3(vertex.getX() + translateX, vertex.getY() + translateY, vertex.getZ()));
            }
        }

        // Kanten kopieren und Indizes anpassen
        if (sourceMesh.getEdges() != null) {
            for (int[] edge : sourceMesh.getEdges()) {
                if (edge.length >= 2) {
                    targetEdges.add(new int[] {baseIndex + edge[0], baseIndex + edge[1]});
                }
            }
        }

        // Flächen kopieren und Indizes anpassen
        if (sourceMesh.getFaces() != null) {
            for (int[] face : sourceMesh.getFaces()) {
                int[] newFace = new int[face.length];
                for (int i = 0; i < face.length; i++) {
                    newFace[i] = baseIndex + face[i];
                }
                targetFaces.add(newFace);
            }
        }
    }

    // Hilfsmethode, um ein Sechseck zu erstellen
    private static void addHexagon(List<int[]> edges, List<int[]> faces, int baseIndex) {
        addPolygon(edges, faces, baseIndex, 6);
    }

    // Hilfsmethode, um ein generisches Polygon aus n Punkten zu erstellen
    private static void addPolygon(List<int[]> edges, List<int[]> faces, int baseIndex, int count) {
        // Erzeugt die Kanten zwischen benachbarten Punkten
        for (int i = 0; i < count; i++) {
            edges.add(new int[] {baseIndex + i, baseIndex + (i + 1) % count});
        }

        // Erzeugt eine einzelne Fläche aus allen Punkten
        int[] face = new int[count];
        for (int i = 0; i < count; i++) face[i] = baseIndex + i;
        faces.add(face);
    }
}
