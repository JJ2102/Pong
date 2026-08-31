package meshes;

import math.Vector3;
import rendering.Mesh;

import java.util.ArrayList;
import java.util.List;

// Erzeugt das Mesh einer Siebensegmentanzeige (zwei Ziffern mit Doppelpunkt)
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

    // X-Position der rechten Segmente, die linken liegen gespiegelt dazu
    private static final float RIGHT_SEGMENT_X = DIGIT_WIDTH / 2f - SEGMENT_THICKNESS / 2f;

    // Abschrägung der Segment-Ecken
    private static final float SEGMENT_CHAMFER = SEGMENT_THICKNESS * 0.4f;

    // Einstellungen für den Doppelpunkt (Colon)
    private static final float COLON_DOT_SIZE = SEGMENT_THICKNESS * 0.75f;
    private static final float COLON_DOT_Y_OFFSET = 0.22f;
    private static final int COLON_DOT_CORNERS = 8;
    private static final float CHARACTER_SPACING = 0.75f;

    // ===== Segment-Definitionen =====
    // Ein Segment ist ein Rechteck um seinen Mittelpunkt; die Ausrichtung entscheidet,
    // an welchen Ecken es abgeschrägt wird
    private record Segment(float centerX, float centerY, float width, float height, boolean isHorizontal) {
    }

    // Segmentanordnung wie klassisch üblich (A=oben, B=rechts-oben, C=rechts-unten,
    // D=unten, E=links-unten, F=links-oben, G=Mitte)
    private static final Segment[] SEGMENTS = {
            new Segment(0f, TOP_SEGMENT_Y, HORIZONTAL_SEGMENT_WIDTH, SEGMENT_THICKNESS, true), // A (Oben)
            new Segment(RIGHT_SEGMENT_X, UPPER_VERTICAL_Y, SEGMENT_THICKNESS, VERTICAL_SEGMENT_HEIGHT, false), // B (Rechts oben)
            new Segment(RIGHT_SEGMENT_X, LOWER_VERTICAL_Y, SEGMENT_THICKNESS, VERTICAL_SEGMENT_HEIGHT, false), // C (Rechts unten)
            new Segment(0f, BOTTOM_SEGMENT_Y, HORIZONTAL_SEGMENT_WIDTH, SEGMENT_THICKNESS, true), // D (Unten)
            new Segment(-RIGHT_SEGMENT_X, LOWER_VERTICAL_Y, SEGMENT_THICKNESS, VERTICAL_SEGMENT_HEIGHT, false), // E (Links unten)
            new Segment(-RIGHT_SEGMENT_X, UPPER_VERTICAL_Y, SEGMENT_THICKNESS, VERTICAL_SEGMENT_HEIGHT, false), // F (Links oben)
            new Segment(0f, MIDDLE_SEGMENT_Y, HORIZONTAL_SEGMENT_WIDTH, SEGMENT_THICKNESS, true)  // G (Mitte)
    };

    // Mapping, welche Segmente für welche Ziffer (0-9) aktiv sind
    private static final boolean[][] DIGIT_SEGMENTS = {
            // A      B      C      D      E      F      G
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

    // ===== Aufbau der Anzeige =====
    // Gesammelte Geometrie, während die Anzeige Stück für Stück entsteht
    private final List<Vector3> vertices = new ArrayList<>();
    private final List<int[]> edges = new ArrayList<>();
    private final List<int[]> faces = new ArrayList<>();

    // Wird nur von getDisplayMesh benutzt, eine Instanz baut genau eine Anzeige
    private SevenSegmentMeshes() {
    }

    // Erzeugt das gesamte Anzeige-Mesh mit linker Ziffer, Doppelpunkt und rechter Ziffer
    public static Mesh getDisplayMesh(int leftDigit, int rightDigit) {
        SevenSegmentMeshes display = new SevenSegmentMeshes();

        display.addDigit(leftDigit, -CHARACTER_SPACING);
        display.addColon();
        display.addDigit(rightDigit, CHARACTER_SPACING);

        return new Mesh(display.vertices, display.edges.toArray(new int[0][]), display.faces.toArray(new int[0][]));
    }

    // ===== Hilfsmethoden zur Geometrie-Erstellung =====
    // Fügt die aktiven Segmente einer Ziffer hinzu, um offsetX nach links oder rechts verschoben
    private void addDigit(int digit, float offsetX) {
        // Die Anzeige kennt nur die Ziffern 0-9, alles andere wird darauf begrenzt
        boolean[] activeSegments = DIGIT_SEGMENTS[Math.clamp(digit, 0, 9)];

        for (int i = 0; i < SEGMENTS.length; i++) {
            if (activeSegments[i]) {
                addSegment(SEGMENTS[i], offsetX);
            }
        }
    }

    // Fügt den Doppelpunkt aus seinen zwei Punkten in der Mitte der Anzeige hinzu
    private void addColon() {
        addColonDot(COLON_DOT_Y_OFFSET);
        addColonDot(-COLON_DOT_Y_OFFSET);
    }

    // Erzeugt ein Segment als Sechseck mit abgeschrägten Ecken (chamfer), damit die Segmente
    // wie bei einer echten Siebensegmentanzeige spitz zueinander zulaufen, statt rechteckig zu wirken
    private void addSegment(Segment segment, float offsetX) {
        float x = segment.centerX() + offsetX;
        float y = segment.centerY();
        float halfWidth = segment.width() / 2f;
        float halfHeight = segment.height() / 2f;

        if (segment.isHorizontal()) {
            // Die Spitzen des Sechsecks zeigen nach links und rechts
            addPolygon(
                    new Vector3(x - halfWidth + SEGMENT_CHAMFER, y + halfHeight, 0),
                    new Vector3(x + halfWidth - SEGMENT_CHAMFER, y + halfHeight, 0),
                    new Vector3(x + halfWidth, y, 0),
                    new Vector3(x + halfWidth - SEGMENT_CHAMFER, y - halfHeight, 0),
                    new Vector3(x - halfWidth + SEGMENT_CHAMFER, y - halfHeight, 0),
                    new Vector3(x - halfWidth, y, 0));
        } else {
            // Die Spitzen des Sechsecks zeigen nach oben und unten
            addPolygon(
                    new Vector3(x, y + halfHeight, 0),
                    new Vector3(x + halfWidth, y + halfHeight - SEGMENT_CHAMFER, 0),
                    new Vector3(x + halfWidth, y - halfHeight + SEGMENT_CHAMFER, 0),
                    new Vector3(x, y - halfHeight, 0),
                    new Vector3(x - halfWidth, y - halfHeight + SEGMENT_CHAMFER, 0),
                    new Vector3(x - halfWidth, y + halfHeight - SEGMENT_CHAMFER, 0));
        }
    }

    // Erzeugt einen Punkt des Doppelpunkts als kleines regelmäßiges Vieleck
    private void addColonDot(float centerY) {
        float radius = COLON_DOT_SIZE / 2f;
        Vector3[] corners = new Vector3[COLON_DOT_CORNERS];

        for (int i = 0; i < COLON_DOT_CORNERS; i++) {
            double angle = 2 * Math.PI * i / COLON_DOT_CORNERS;
            corners[i] = new Vector3(Math.cos(angle) * radius, centerY + Math.sin(angle) * radius, 0);
        }

        addPolygon(corners);
    }

    // Hängt ein geschlossenes Polygon an: Punkte übernehmen, Randkanten ziehen und eine Fläche bilden
    private void addPolygon(Vector3... corners) {
        int baseIndex = vertices.size(); // Startindex der neuen Punkte
        int[] face = new int[corners.length];

        for (int i = 0; i < corners.length; i++) {
            vertices.add(corners[i]);
            edges.add(new int[] {baseIndex + i, baseIndex + (i + 1) % corners.length});
            face[i] = baseIndex + i;
        }

        faces.add(face);
    }
}
