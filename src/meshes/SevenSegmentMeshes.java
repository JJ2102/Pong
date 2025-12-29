package meshes;

import rendering.Mesh;
import math.Vektor3;

import java.util.ArrayList;
import java.util.List;

public class SevenSegmentMeshes {
    // ===== NUR DIESE WERTE ANPASSEN =====
    private static final float DIGIT_WIDTH = 0.6f;           // Gesamtbreite der Ziffer
    private static final float DIGIT_HEIGHT = 1.0f;          // Gesamthöhe der Ziffer
    private static final float SEGMENT_THICKNESS = 0.10f;    // Dicke der Balken
    private static final float SEGMENT_GAP = 0.02f;          // Abstand zwischen Segmenten
    // ====================================

    // Alles andere wird automatisch berechnet:
    private static final float HORIZONTAL_SEGMENT_WIDTH = DIGIT_WIDTH * 0.85f; // Verkürzt für besseres Aussehen
    private static final float VERTICAL_SEGMENT_HEIGHT = (DIGIT_HEIGHT - SEGMENT_THICKNESS * 3 - SEGMENT_GAP * 2) / 2f;

    // Y-Positionen (vertikal) - präziser berechnet für symmetrisches Aussehen
    private static final float TOP_SEGMENT_Y = DIGIT_HEIGHT / 2f - SEGMENT_THICKNESS / 2f;
    private static final float MIDDLE_SEGMENT_Y = 0.0f;
    private static final float BOTTOM_SEGMENT_Y = -DIGIT_HEIGHT / 2f + SEGMENT_THICKNESS / 2f;

    // Vertikale Segmente - perfekt zwischen den horizontalen positioniert
    private static final float UPPER_VERTICAL_Y = (TOP_SEGMENT_Y + MIDDLE_SEGMENT_Y) / 2f;
    private static final float LOWER_VERTICAL_Y = (MIDDLE_SEGMENT_Y + BOTTOM_SEGMENT_Y) / 2f;

    // X-Position (horizontal) - vertikale Segmente präzise an den Kanten
    private static final float RIGHT_SEGMENT_X = DIGIT_WIDTH / 2f - SEGMENT_THICKNESS / 2f;

    // Segmentanordnung:
    //  AAA
    // F   B
    //  GGG
    // E   C
    //  DDD

    // Segment-Bitmuster für 0-9: A B C D E F G (1=an)
    private static final boolean[][] DIGIT_SEGMENTS = {
            // A     B      C      D      E      F      G
            {true,  true,  true,  true,  true,  true,  false}, //0
            {false, true,  true,  false, false, false, false}, //1
            {true,  true,  false, true,  true,  false, true }, //2
            {true,  true,  true,  true,  false, false, true }, //3
            {false, true,  true,  false, false, true,  true }, //4
            {true,  false, true,  true,  false, true,  true }, //5
            {true,  false, true,  true,  true,  true,  true }, //6
            {true,  true,  true,  false, false, false, false}, //7
            {true,  true,  true,  true,  true,  true,  true }, //8
            {true,  true,  true,  true,  false, true,  true }  //9
    };

    // Hilfsmethode: Segment mit abgerundeten Enden (Hexagon-Form)
    private static void addRoundedSegment(List<Vektor3> vertices, List<int[]> edges, List<int[]> faces,
                                          float centerX, float centerY, float width, float height, boolean isHorizontal) {
        int baseIndex = vertices.size();
        float halfWidth = width / 2f;
        float halfHeight = height / 2f;

        float chamfer = SEGMENT_THICKNESS * 0.4f;
        if (isHorizontal) {
            // Horizontales Segment mit schrägen Enden (Hexagon)

            vertices.add(new Vektor3(centerX - halfWidth + chamfer, centerY + halfHeight, 0)); // 0: oben links
            vertices.add(new Vektor3(centerX + halfWidth - chamfer, centerY + halfHeight, 0)); // 1: oben rechts
            vertices.add(new Vektor3(centerX + halfWidth, centerY, 0));                        // 2: rechts mitte
            vertices.add(new Vektor3(centerX + halfWidth - chamfer, centerY - halfHeight, 0)); // 3: unten rechts
            vertices.add(new Vektor3(centerX - halfWidth + chamfer, centerY - halfHeight, 0)); // 4: unten links
            vertices.add(new Vektor3(centerX - halfWidth, centerY, 0));                        // 5: links mitte

        } else {
            // Vertikales Segment mit schrägen Enden (Hexagon)

            vertices.add(new Vektor3(centerX, centerY + halfHeight, 0));                       // 0: oben mitte
            vertices.add(new Vektor3(centerX + halfWidth, centerY + halfHeight - chamfer, 0)); // 1: oben rechts
            vertices.add(new Vektor3(centerX + halfWidth, centerY - halfHeight + chamfer, 0)); // 2: unten rechts
            vertices.add(new Vektor3(centerX, centerY - halfHeight, 0));                       // 3: unten mitte
            vertices.add(new Vektor3(centerX - halfWidth, centerY - halfHeight + chamfer, 0)); // 4: unten links
            vertices.add(new Vektor3(centerX - halfWidth, centerY + halfHeight - chamfer, 0)); // 5: oben links

        }
        edges.add(new int[] {baseIndex, baseIndex + 1});
        edges.add(new int[] {baseIndex + 1, baseIndex + 2});
        edges.add(new int[] {baseIndex + 2, baseIndex + 3});
        edges.add(new int[] {baseIndex + 3, baseIndex + 4});
        edges.add(new int[] {baseIndex + 4, baseIndex + 5});
        edges.add(new int[] {baseIndex + 5, baseIndex});
        faces.add(new int[] {baseIndex, baseIndex + 1, baseIndex + 2, baseIndex + 3, baseIndex + 4, baseIndex + 5});
    }

    // Erzeuge Mesh für eine einzelne Ziffer (mit Mittelpunkt bei x=0)
    public static Mesh getDigitMesh(int digit) {
        if (digit < 0 || digit > 9) digit = 0;

        List<Vektor3> vertices = new ArrayList<>();
        List<int[]> edges = new ArrayList<>();
        List<int[]> faces = new ArrayList<>();

        boolean[] activeSegments = DIGIT_SEGMENTS[digit];

        // Segment A (oben, horizontal)
        if (activeSegments[0]) {
            addRoundedSegment(vertices, edges, faces, 0f, TOP_SEGMENT_Y,
                    HORIZONTAL_SEGMENT_WIDTH, SEGMENT_THICKNESS, true);
        }
        // Segment B (oben rechts, vertikal)
        if (activeSegments[1]) {
            addRoundedSegment(vertices, edges, faces, RIGHT_SEGMENT_X, UPPER_VERTICAL_Y,
                    SEGMENT_THICKNESS, VERTICAL_SEGMENT_HEIGHT, false);
        }
        // Segment C (unten rechts, vertikal)
        if (activeSegments[2]) {
            addRoundedSegment(vertices, edges, faces, RIGHT_SEGMENT_X, LOWER_VERTICAL_Y,
                    SEGMENT_THICKNESS, VERTICAL_SEGMENT_HEIGHT, false);
        }
        // Segment D (unten, horizontal)
        if (activeSegments[3]) {
            addRoundedSegment(vertices, edges, faces, 0f, BOTTOM_SEGMENT_Y,
                    HORIZONTAL_SEGMENT_WIDTH, SEGMENT_THICKNESS, true);
        }
        // Segment E (unten links, vertikal)
        if (activeSegments[4]) {
            addRoundedSegment(vertices, edges, faces, -RIGHT_SEGMENT_X, LOWER_VERTICAL_Y,
                    SEGMENT_THICKNESS, VERTICAL_SEGMENT_HEIGHT, false);
        }
        // Segment F (oben links, vertikal)
        if (activeSegments[5]) {
            addRoundedSegment(vertices, edges, faces, -RIGHT_SEGMENT_X, UPPER_VERTICAL_Y,
                    SEGMENT_THICKNESS, VERTICAL_SEGMENT_HEIGHT, false);
        }
        // Segment G (mittig, horizontal)
        if (activeSegments[6]) {
            addRoundedSegment(vertices, edges, faces, 0f, MIDDLE_SEGMENT_Y,
                    HORIZONTAL_SEGMENT_WIDTH, SEGMENT_THICKNESS, true);
        }

        return new Mesh(vertices, edges.toArray(new int[edges.size()][]),
                faces.toArray(new int[faces.size()][]));
    }

    // Doppelpunkt als zwei kleine abgerundete Quadrate
    public static Mesh getColonMesh() {
        List<Vektor3> vertices = new ArrayList<>();
        List<int[]> edges = new ArrayList<>();
        List<int[]> faces = new ArrayList<>();

        float dotSize = SEGMENT_THICKNESS * 0.75f;
        int segments = 8; // Anzahl der Segmente für runde Punkte

        // Oberer Punkt (als Kreis approximiert) - besser zentriert
        int upperBase = vertices.size();
        for (int i = 0; i < segments; i++) {
            double angle = 2 * Math.PI * i / segments;
            float x = (float)(Math.cos(angle) * dotSize / 2f);
            float y = 0.22f + (float)(Math.sin(angle) * dotSize / 2f);
            vertices.add(new Vektor3(x, y, 0));
        }
        for (int i = 0; i < segments; i++) {
            edges.add(new int[] {upperBase + i, upperBase + (i + 1) % segments});
        }
        int[] upperFace = new int[segments];
        for (int i = 0; i < segments; i++) upperFace[i] = upperBase + i;
        faces.add(upperFace);

        // Unterer Punkt (als Kreis approximiert)
        int lowerBase = vertices.size();
        for (int i = 0; i < segments; i++) {
            double angle = 2 * Math.PI * i / segments;
            float x = (float)(Math.cos(angle) * dotSize / 2f);
            float y = -0.22f + (float)(Math.sin(angle) * dotSize / 2f);
            vertices.add(new Vektor3(x, y, 0));
        }
        for (int i = 0; i < segments; i++) {
            edges.add(new int[] {lowerBase + i, lowerBase + (i + 1) % segments});
        }
        int[] lowerFace = new int[segments];
        for (int i = 0; i < segments; i++) lowerFace[i] = lowerBase + i;
        faces.add(lowerFace);

        return new Mesh(vertices, edges.toArray(new int[edges.size()][]),
                faces.toArray(new int[faces.size()][]));
    }

    // Kombiniertes Mesh für "L : R" mit Positionsoffsets
    public static Mesh getDisplayMesh(int leftDigit, int rightDigit) {
        List<Vektor3> vertices = new ArrayList<>();
        List<int[]> edges = new ArrayList<>();
        List<int[]> faces = new ArrayList<>();

        float characterSpacing = 0.75f; // Abstand zwischen Zeichen - kompakter für besseres Gesamtbild

        // linke Ziffer
        addMeshTranslated(vertices, edges, faces, getDigitMesh(leftDigit), -characterSpacing, 0f);
        // Doppelpunkt
        addMeshTranslated(vertices, edges, faces, getColonMesh(), 0f, 0f);
        // rechte Ziffer
        addMeshTranslated(vertices, edges, faces, getDigitMesh(rightDigit), characterSpacing, 0f);

        return new Mesh(vertices, edges.toArray(new int[edges.size()][]),
                faces.toArray(new int[faces.size()][]));
    }

    // Kopiert ein Mesh und verschiebt die Vektoren um (translateX, translateY)
    private static void addMeshTranslated(List<Vektor3> targetVertices, List<int[]> targetEdges,
                                          List<int[]> targetFaces, Mesh sourceMesh,
                                          float translateX, float translateY) {
        if (sourceMesh == null) return;

        List<Vektor3> sourceVertices = sourceMesh.vertices;
        int[][] sourceEdges = sourceMesh.edges;
        int[][] sourceFaces = sourceMesh.faces;

        int baseIndex = targetVertices.size();

        if (sourceVertices != null) {
            for (Vektor3 vertex : sourceVertices) {
                targetVertices.add(new Vektor3((float)(vertex.x + translateX),
                        (float)(vertex.y + translateY),
                        (float)vertex.z));
            }
        }

        if (sourceEdges != null) {
            for (int[] edge : sourceEdges) {
                if (edge.length >= 2) {
                    targetEdges.add(new int[] {baseIndex + edge[0], baseIndex + edge[1]});
                }
            }
        }

        if (sourceFaces != null) {
            for (int[] face : sourceFaces) {
                int[] newFace = new int[face.length];
                for (int i = 0; i < face.length; i++) {
                    newFace[i] = baseIndex + face[i];
                }
                targetFaces.add(newFace);
            }
        }
    }
}