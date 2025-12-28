// java
package meshes;

import rendering.Mesh;
import math.Vektor3;

import java.util.ArrayList;
import java.util.List;

public class SevenSegmentMeshes {
    // Segmentgrößen (anpassbar)
    private static final float H_WIDTH = 0.8f;   // horizontale Segmentbreite
    private static final float V_HEIGHT = 0.6f;  // vertikale Segmenthöhe
    private static final float THICK = 0.12f;    // Segmentdicke
    // Positionen der Segmente relativ zum Ziffernmittelpunkt
    private static final float A_Y = 0.5f;
    private static final float G_Y = -0.05f;
    private static final float D_Y = -0.6f;
    private static final float R_X = 0.35f;
    private static final float T_Y = 0.15f;
    private static final float B_Y = -0.35f;

    // Segment-Bitmuster für 0-9: A B C D E F G (1=an)
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

    // Hilfsmethode: Quad an Position (cx,cy) mit Breite w und Höhe h erzeugen
    private static void addQuad(List<Vektor3> verts, List<int[]> edges, List<int[]> faces,
                                float cx, float cy, float w, float h) {
        int base = verts.size();
        float hw = w / 2f;
        float hh = h / 2f;
        verts.add(new Vektor3(cx - hw, cy + hh, 0)); // 0
        verts.add(new Vektor3(cx + hw, cy + hh, 0)); // 1
        verts.add(new Vektor3(cx + hw, cy - hh, 0)); // 2
        verts.add(new Vektor3(cx - hw, cy - hh, 0)); // 3

        edges.add(new int[] {base, base + 1});
        edges.add(new int[] {base + 1, base + 2});
        edges.add(new int[] {base + 2, base + 3});
        edges.add(new int[] {base + 3, base});

        faces.add(new int[] {base, base + 1, base + 2, base + 3});
    }

    // Erzeuge Mesh für eine einzelne Ziffer (mit Mittelpunkt bei x=0)
    public static Mesh getDigitMesh(int digit) {
        if (digit < 0 || digit > 9) digit = 0;
        List<Vektor3> verts = new ArrayList<>();
        List<int[]> edges = new ArrayList<>();
        List<int[]> faces = new ArrayList<>();

        boolean[] seg = DIGIT_SEGMENTS[digit];

        // A (oben, horizontal)
        if (seg[0]) addQuad(verts, edges, faces, 0f, A_Y, H_WIDTH, THICK);
        // B (oben rechts, vertikal)
        if (seg[1]) addQuad(verts, edges, faces, R_X, T_Y, THICK, V_HEIGHT);
        // C (unten rechts, vertikal)
        if (seg[2]) addQuad(verts, edges, faces, R_X, B_Y, THICK, V_HEIGHT);
        // D (unten, horizontal)
        if (seg[3]) addQuad(verts, edges, faces, 0f, D_Y, H_WIDTH, THICK);
        // E (unten links, vertikal)
        if (seg[4]) addQuad(verts, edges, faces, -R_X, B_Y, THICK, V_HEIGHT);
        // F (oben links, vertikal)
        if (seg[5]) addQuad(verts, edges, faces, -R_X, T_Y, THICK, V_HEIGHT);
        // G (mittig, horizontal)
        if (seg[6]) addQuad(verts, edges, faces, 0f, G_Y, H_WIDTH, THICK);

        return new Mesh(verts, edges.toArray(new int[edges.size()][]), faces.toArray(new int[faces.size()][]));
    }

    // Doppelpunkt als zwei kleine Quadrate
    public static Mesh getColonMesh() {
        List<Vektor3> verts = new ArrayList<>();
        List<int[]> edges = new ArrayList<>();
        List<int[]> faces = new ArrayList<>();

        float dotSize = THICK * 0.8f;
        addQuad(verts, edges, faces, 0f, 0.22f, dotSize, dotSize);
        addQuad(verts, edges, faces, 0f, -0.22f, dotSize, dotSize);

        return new Mesh(verts, edges.toArray(new int[edges.size()][]), faces.toArray(new int[faces.size()][]));
    }

    // Kombiniertes Mesh für "L : R" mit Positionsoffsets
    public static Mesh getDisplayMesh(int leftDigit, int rightDigit) {
        List<Vektor3> verts = new ArrayList<>();
        List<int[]> edges = new ArrayList<>();
        List<int[]> faces = new ArrayList<>();

        float spacing = 0.85f; // Abstand zwischen Zeichen

        // linke Ziffer
        addMeshTranslated(verts, edges, faces, getDigitMesh(leftDigit), -spacing, 0f);
        // Doppelpunkt
        addMeshTranslated(verts, edges, faces, getColonMesh(), 0f, 0f);
        // rechte Ziffer
        addMeshTranslated(verts, edges, faces, getDigitMesh(rightDigit), spacing, 0f);

        return new Mesh(verts, edges.toArray(new int[edges.size()][]), faces.toArray(new int[faces.size()][]));
    }

    // Kopiert ein Mesh und verschiebt die Vektoren um (tx,ty)
    private static void addMeshTranslated(List<Vektor3> verts, List<int[]> edges, List<int[]> faces, Mesh mesh, float tx, float ty) {
        // Mesh hat in deinem Projekt öffentliche Felder: vertices, edges, faces
        if (mesh == null) return;
        List<Vektor3> srcVerts = mesh.vertices;
        int[][] srcEdges = mesh.edges;
        int[][] srcFaces = mesh.faces;

        int base = verts.size();
        if (srcVerts != null) {
            for (Vektor3 v : srcVerts) {
                verts.add(new Vektor3((float)(v.x + tx), (float)(v.y + ty), (float)v.z));
            }
        }

        if (srcEdges != null) {
            for (int[] e : srcEdges) {
                // Erwartet immer Paare
                if (e.length >= 2) edges.add(new int[] {base + e[0], base + e[1]});
            }
        }

        if (srcFaces != null) {
            for (int[] f : srcFaces) {
                int[] ff = new int[f.length];
                for (int i = 0; i < f.length; i++) ff[i] = base + f[i];
                faces.add(ff);
            }
        }
    }
}
