package meshes;

import rendering.Mesh;
import math.Vektor3;

import java.util.ArrayList;
import java.util.List;

public class SevenSegmentMeshes {
    // ===== NUR DIESE WERTE ANPASSEN =====
    private static final float DIGIT_WIDTH = 0.6f;
    private static final float DIGIT_HEIGHT = 1.0f;
    private static final float SEGMENT_THICKNESS = 0.10f;
    private static final float SEGMENT_GAP = 0.02f;
    // ====================================

    private static final float HORIZONTAL_SEGMENT_WIDTH = DIGIT_WIDTH * 0.85f;
    private static final float VERTICAL_SEGMENT_HEIGHT = (DIGIT_HEIGHT - SEGMENT_THICKNESS * 3 - SEGMENT_GAP * 2) / 2f;
    private static final float TOP_SEGMENT_Y = DIGIT_HEIGHT / 2f - SEGMENT_THICKNESS / 2f;
    private static final float MIDDLE_SEGMENT_Y = 0.0f;
    private static final float BOTTOM_SEGMENT_Y = -DIGIT_HEIGHT / 2f + SEGMENT_THICKNESS / 2f;
    private static final float UPPER_VERTICAL_Y = (TOP_SEGMENT_Y + MIDDLE_SEGMENT_Y) / 2f;
    private static final float LOWER_VERTICAL_Y = (MIDDLE_SEGMENT_Y + BOTTOM_SEGMENT_Y) / 2f;
    private static final float RIGHT_SEGMENT_X = DIGIT_WIDTH / 2f - SEGMENT_THICKNESS / 2f;

    private static final float COLON_DOT_SIZE = SEGMENT_THICKNESS * 0.75f;
    private static final float COLON_DOT_Y_OFFSET = 0.22f;
    private static final int COLON_SEGMENTS = 8;
    private static final float CHARACTER_SPACING = 0.75f;

    // Segment\-Definitionen: x, y, breite, höhe, isHorizontal
    private static final float[][] SEGMENT_CONFIGS = {
            {0f, TOP_SEGMENT_Y, HORIZONTAL_SEGMENT_WIDTH, SEGMENT_THICKNESS, 1}, // A
            {RIGHT_SEGMENT_X, UPPER_VERTICAL_Y, SEGMENT_THICKNESS, VERTICAL_SEGMENT_HEIGHT, 0}, // B
            {RIGHT_SEGMENT_X, LOWER_VERTICAL_Y, SEGMENT_THICKNESS, VERTICAL_SEGMENT_HEIGHT, 0}, // C
            {0f, BOTTOM_SEGMENT_Y, HORIZONTAL_SEGMENT_WIDTH, SEGMENT_THICKNESS, 1}, // D
            {-RIGHT_SEGMENT_X, LOWER_VERTICAL_Y, SEGMENT_THICKNESS, VERTICAL_SEGMENT_HEIGHT, 0}, // E
            {-RIGHT_SEGMENT_X, UPPER_VERTICAL_Y, SEGMENT_THICKNESS, VERTICAL_SEGMENT_HEIGHT, 0}, // F
            {0f, MIDDLE_SEGMENT_Y, HORIZONTAL_SEGMENT_WIDTH, SEGMENT_THICKNESS, 1}  // G
    };

    private static final boolean[][] DIGIT_SEGMENTS = {
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

    public static Mesh getDigitMesh(int digit) {
        if (digit < 0 || digit > 9) digit = 0;

        List<Vektor3> vertices = new ArrayList<>();
        List<int[]> edges = new ArrayList<>();
        List<int[]> faces = new ArrayList<>();

        boolean[] activeSegments = DIGIT_SEGMENTS[digit];
        for (int i = 0; i < 7; i++) {
            if (activeSegments[i]) {
                float[] config = SEGMENT_CONFIGS[i];
                boolean isHorizontal = config[4] > 0.5f;
                addRoundedSegment(vertices, edges, faces, config[0], config[1], config[2], config[3], isHorizontal);
            }
        }

        return new Mesh(vertices, edges.toArray(new int[0][]), faces.toArray(new int[0][]));
    }

    public static Mesh getColonMesh() {
        List<Vektor3> vertices = new ArrayList<>();
        List<int[]> edges = new ArrayList<>();
        List<int[]> faces = new ArrayList<>();

        addColonDot(vertices, edges, faces, COLON_DOT_Y_OFFSET);
        addColonDot(vertices, edges, faces, -COLON_DOT_Y_OFFSET);

        return new Mesh(vertices, edges.toArray(new int[0][]), faces.toArray(new int[0][]));
    }

    public static Mesh getDisplayMesh(int leftDigit, int rightDigit) {
        List<Vektor3> vertices = new ArrayList<>();
        List<int[]> edges = new ArrayList<>();
        List<int[]> faces = new ArrayList<>();

        addMeshTranslated(vertices, edges, faces, getDigitMesh(leftDigit), -CHARACTER_SPACING, 0f);
        addMeshTranslated(vertices, edges, faces, getColonMesh(), 0f, 0f);
        addMeshTranslated(vertices, edges, faces, getDigitMesh(rightDigit), CHARACTER_SPACING, 0f);

        return new Mesh(vertices, edges.toArray(new int[0][]), faces.toArray(new int[0][]));
    }

    private static void addRoundedSegment(List<Vektor3> vertices, List<int[]> edges, List<int[]> faces,
                                          float centerX, float centerY, float width, float height, boolean isHorizontal) {
        int baseIndex = vertices.size();
        float halfWidth = width / 2f;
        float halfHeight = height / 2f;
        float chamfer = SEGMENT_THICKNESS * 0.4f;

        if (isHorizontal) {
            vertices.add(new Vektor3(centerX - halfWidth + chamfer, centerY + halfHeight, 0));
            vertices.add(new Vektor3(centerX + halfWidth - chamfer, centerY + halfHeight, 0));
            vertices.add(new Vektor3(centerX + halfWidth, centerY, 0));
            vertices.add(new Vektor3(centerX + halfWidth - chamfer, centerY - halfHeight, 0));
            vertices.add(new Vektor3(centerX - halfWidth + chamfer, centerY - halfHeight, 0));
            vertices.add(new Vektor3(centerX - halfWidth, centerY, 0));
        } else {
            vertices.add(new Vektor3(centerX, centerY + halfHeight, 0));
            vertices.add(new Vektor3(centerX + halfWidth, centerY + halfHeight - chamfer, 0));
            vertices.add(new Vektor3(centerX + halfWidth, centerY - halfHeight + chamfer, 0));
            vertices.add(new Vektor3(centerX, centerY - halfHeight, 0));
            vertices.add(new Vektor3(centerX - halfWidth, centerY - halfHeight + chamfer, 0));
            vertices.add(new Vektor3(centerX - halfWidth, centerY + halfHeight - chamfer, 0));
        }

        addHexagon(edges, faces, baseIndex);
    }

    private static void addColonDot(List<Vektor3> vertices, List<int[]> edges, List<int[]> faces, float yOffset) {
        int baseIndex = vertices.size();

        for (int i = 0; i < COLON_SEGMENTS; i++) {
            double angle = 2 * Math.PI * i / COLON_SEGMENTS;
            float x = (float)(Math.cos(angle) * COLON_DOT_SIZE / 2f);
            float y = yOffset + (float)(Math.sin(angle) * COLON_DOT_SIZE / 2f);
            vertices.add(new Vektor3(x, y, 0));
        }

        addPolygon(edges, faces, baseIndex, COLON_SEGMENTS);
    }

    private static void addMeshTranslated(List<Vektor3> targetVertices, List<int[]> targetEdges,
                                          List<int[]> targetFaces, Mesh sourceMesh,
                                          float translateX, float translateY) {
        if (sourceMesh == null) return;

        int baseIndex = targetVertices.size();

        if (sourceMesh.vertices != null) {
            for (Vektor3 vertex : sourceMesh.vertices) {
                targetVertices.add(new Vektor3(vertex.x + translateX, vertex.y + translateY, vertex.z));
            }
        }

        if (sourceMesh.edges != null) {
            for (int[] edge : sourceMesh.edges) {
                if (edge.length >= 2) {
                    targetEdges.add(new int[] {baseIndex + edge[0], baseIndex + edge[1]});
                }
            }
        }

        if (sourceMesh.faces != null) {
            for (int[] face : sourceMesh.faces) {
                int[] newFace = new int[face.length];
                for (int i = 0; i < face.length; i++) {
                    newFace[i] = baseIndex + face[i];
                }
                targetFaces.add(newFace);
            }
        }
    }

    private static void addHexagon(List<int[]> edges, List<int[]> faces, int baseIndex) {
        addPolygon(edges, faces, baseIndex, 6);
    }

    private static void addPolygon(List<int[]> edges, List<int[]> faces, int baseIndex, int count) {
        for (int i = 0; i < count; i++) {
            edges.add(new int[] {baseIndex + i, baseIndex + (i + 1) % count});
        }

        int[] face = new int[count];
        for (int i = 0; i < count; i++) face[i] = baseIndex + i;
        faces.add(face);
    }
}