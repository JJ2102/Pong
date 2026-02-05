package rendering;

import hitboxes.BoxHitbox;
import math.Matrix4x4;
import math.Vektor2;
import math.Vektor3;
import objekts.Entity;

import java.awt.*;

public class Renderer {
    private int width, height;
    private double scale;
    private final Matrix4x4 TranslationMatrix;
    private final Matrix4x4 RotationMatrixX;
    private final Matrix4x4 RotationMatrixY;
    private final Matrix4x4 RotationMatrixZ;
    private final Matrix4x4 ScaleMatrix;

    public Renderer(int width, int height) {
        this.width = width;
        this.height = height;
        this.scale = Math.min(width, height) / 2.0;

        // Matritzen initialisieren
        TranslationMatrix = Matrix4x4.getMatrix();
        RotationMatrixX = Matrix4x4.getMatrix();
        RotationMatrixY = Matrix4x4.getMatrix();
        RotationMatrixZ = Matrix4x4.getMatrix();
        ScaleMatrix = Matrix4x4.getMatrix();
    }

    // Ermöglicht Aktualisierung bei Panel-Resize
    public void updateSize(int width, int height) {
        this.width = width;
        this.height = height;
        this.scale = Math.min(width, height) / 2.0;
    }

    // Projektion von 3D -> 2D
    private Vektor2 project(Vektor3 p, double fov) {
        if (p == null) return null;
        if (p.z <= 0) return null; // hinter Kamera
        double x = fov * p.x / p.z;
        double y = fov * p.y / p.z;
        double sx = (width / 2.0 + x * scale);
        double sy = (height / 2.0 - y * scale);
        return new Vektor2(sx, sy);
    }

    // Transformation anwenden (Scale, Rotation, Translation)
    private Vektor3 applyTransform(Vektor3 v) {
        // Skalierung
        Vektor3 scaled = ScaleMatrix.multiply(v);

        // Rotation um x-Achse
        Vektor3 rotatedX = RotationMatrixX.multiply(scaled);

        // Rotation um y-Achse
        Vektor3 rotatedY = RotationMatrixY.multiply(rotatedX);

        // Rotation um Z-Achse
        Vektor3 rotatedZ = RotationMatrixZ.multiply(rotatedY);

        // Translation
        return TranslationMatrix.multiply(rotatedZ);
    }

    // Converter von Welt- zu Kamerakoordinaten
    public Vektor3 worldToCamera(Vektor3 worldPos, Camera cam) {
        // Translation (Welt -> Kamera-Ursprung)
        double x = worldPos.x - cam.getPosition().x;
        double y = worldPos.y - cam.getPosition().y;
        double z = worldPos.z - cam.getPosition().z;

        // SCHRITT 2: Rotation (inverse der Kamera-Rotation)
        // Yaw (Y-Achse) - links/rechts schauen
        double cosY = Math.cos(-cam.getRotation().y);
        double sinY = Math.sin(-cam.getRotation().y);
        double rx = cosY * x + sinY * z;
        double rz = -sinY * x + cosY * z;

        // Pitch (x-Achse) - hoch/runter-schauen
        double cosX = Math.cos(-cam.getRotation().x);
        double sinX = Math.sin(-cam.getRotation().x);
        double ry = cosX * y - sinX * rz;
        rz = sinX * y + cosX * rz;

        // Roll (z-Achse) - optional, meist 0
        double cosZ = Math.cos(-cam.getRotation().z);
        double sinZ = Math.sin(-cam.getRotation().z);
        double rxx = cosZ * rx - sinZ * ry;
        double ryy = sinZ * rx + cosZ * ry;

        return new Vektor3(rxx, ryy, rz);
    }

    /**
     * Konvertiert Bildschirmkoordinaten (pixels) in Weltkoordinaten auf einer Ebene z = planeZ.
     * Vereinfacht: berücksichtigt Kamera-Position und FOV, nicht Kamera-Rotation.
     */
    public Vektor3 screenToWorld(Vektor2 screenPos, double planeZ, Camera cam) {
        if (width <= 0 || height <= 0 || cam == null) {
            return new Vektor3(screenPos.x, screenPos.y, planeZ);
        }

        // Normalisierte Bildschirmkoordinaten (gleiche Basis wie in project())
        double normalizedX = (screenPos.x - width / 2.0) / scale;
        double normalizedY = (screenPos.y - height / 2.0) / scale;

        // Kamera-Parameter
        Vektor3 camPos = cam.getPosition();
        double fov = cam.getFov();

        // Tiefe (Z der Ebene relativ zur Kamera)
        double depth = planeZ - camPos.z; // Differenz der Z-Koordinaten von Kamera und Ziel-Ebene
        if (depth <= 0) {
            // Ebene ist hinter der Kamera, fallback auf Kameraposition
            return new Vektor3(camPos.x, camPos.y, planeZ);
        }

        // invers der in project() verwendeten Projektion:
        // project: x_screen = width/2 + (fov * worldX / worldZ) * scale
        // => worldX = normalizedX * worldZ / fov
        double worldX = camPos.x + normalizedX * depth / fov;
        double worldY = camPos.y - normalizedY * depth / fov; // y umkehren (Bildschirm y wächst nach unten)

        return new Vektor3(worldX, worldY, planeZ);
    }

    // Render Entity
    public void renderEntity(Graphics2D g, Entity entity, Camera camera) {
        renderEntity(g, entity, camera, true);
    }

    public void renderEntity(Graphics2D g, Entity entity, Camera camera, boolean renderFaces) {
        if (entity == null || entity.getMesh() == null || entity.getMesh().vertices == null) return;

        // Anti-Aliasing für sauberere Linien
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Mesh und Transformation des Objekts holen
        Mesh mesh = entity.getMesh();
        Transform transform = entity.getTransform();

        // Matrizen für Transformationen initialisieren
        /* Translations matrix:
        * 1 0 0 Tx
        * 0 1 0 Ty
        * 0 0 1 Tz
        * 0 0 0 1
        * */
        TranslationMatrix.setValue(0, 3, transform.position.x);
        TranslationMatrix.setValue(1, 3, transform.position.y);
        TranslationMatrix.setValue(2, 3, transform.position.z);

        /* Rotation matrix X:
        * 1 0    0     0
        * 0 cos  -sin  0
        * 0 sin  cos   0
        * 0 0    0     1
        */
        RotationMatrixX.setValue(1, 1, Math.cos(transform.rotation.x));
        RotationMatrixX.setValue(1, 2, -Math.sin(transform.rotation.x));
        RotationMatrixX.setValue(2, 1, Math.sin(transform.rotation.x));
        RotationMatrixX.setValue(2, 2, Math.cos(transform.rotation.x));

        /* Rotation matrix Y:
        * cos 0 sin 0
        * 0   1 0   0
        * -sin0 cos 0
        * 0   0 0   1
        */
        RotationMatrixY.setValue(0, 0, Math.cos(transform.rotation.y));
        RotationMatrixY.setValue(0, 2, Math.sin(transform.rotation.y));
        RotationMatrixY.setValue(2, 0, -Math.sin(transform.rotation.y));
        RotationMatrixY.setValue(2, 2, Math.cos(transform.rotation.y));

        /* Rotation matrix Z:
        * cos -sin 0 0
        * sin cos  0 0
        * 0   0    1 0
        * 0   0    0 1
        */
        RotationMatrixZ.setValue(0, 0, Math.cos(transform.rotation.z));
        RotationMatrixZ.setValue(0, 1, -Math.sin(transform.rotation.z));
        RotationMatrixZ.setValue(1, 0, Math.sin(transform.rotation.z));
        RotationMatrixZ.setValue(1, 1, Math.cos(transform.rotation.z));

        /* Scale matrix:
        * Sx 0  0  0
        * 0  Sy 0  0
        * 0  0  Sz 0
        * 0  0  0  1
        */
        ScaleMatrix.setValue(0, 0, transform.scale.x);
        ScaleMatrix.setValue(1, 1, transform.scale.y);
        ScaleMatrix.setValue(2, 2, transform.scale.z);

        // Flächen zeichnen
        if (mesh.faces != null && renderFaces) { // Sicherheitscheck
            for (int[] face : mesh.faces) { // geht durch alle Flächen des Meshes
                if (face == null || face.length == 0) continue; // Leere Fläche überspringen

                Polygon poly = new Polygon();

                for (int idx : face) { // geht durch alle Eckpunkte der Fläche; idx ist der Index im Vertex-Array
                    if (idx < 0 || idx >= mesh.vertices.size()) continue; // idx nicht kleiner 0 oder größer als Anzahl der Eckpunkte

                    // Transformieren der einzelnen Eckpunkte der Fläche
                    Vektor3 worldPos = applyTransform(mesh.vertices.get(idx));
                    // verschiebung der Eckpunkte relativ zur Kamera
                    Vektor3 cameraPos = worldToCamera(worldPos, camera);
                    // Projektion auf 2D-Bildschirm → 2D-Koordinate
                    Vektor2 v = project(cameraPos, camera.getFov());

                    // Hinzufügen des projizierten Punkts zum Polygon
                    if (v != null) {
                        poly.addPoint((int) v.x, (int) v.y);
                    }
                }
                // Nur zeichnen, wenn mindestens ein Dreieck möglich ist
                if (poly.npoints >= 3) {
                    g.setColor(entity.getFaceColor());
                    g.fillPolygon(poly);
                    g.setColor(Color.BLACK);
                }
            }
        }

        // Kanten zeichnen
        if (mesh.edges != null) {
            for (int[] edge : mesh.edges) {
                if (edge == null || edge.length < 2) continue;
                int i0 = edge[0];
                int i1 = edge[1];
                if (i0 < 0 || i1 < 0 || i0 >= mesh.vertices.size() || i1 >= mesh.vertices.size()) continue; // Sicherheitscheck

                // Weltposition der beiden Eckpunkte der Kante
                Vektor3 worldPos1 = applyTransform(mesh.vertices.get(i0));
                Vektor3 worldPos2 = applyTransform(mesh.vertices.get(i1));

                // Umwandlung in Kamerakoordinaten
                Vektor3 cameraPos1 = worldToCamera(worldPos1, camera);
                Vektor3 cameraPos2 = worldToCamera(worldPos2, camera);

                // Projektion auf 2D-Bildschirm
                Vektor2 v1 = project(cameraPos1, camera.getFov());
                Vektor2 v2 = project(cameraPos2 , camera.getFov());

                // Zeichnen der Kante
                if (v1 != null && v2 != null) {
                    if (renderFaces) {
                        g.setColor(entity.getEdgeColor());
                    } else {
                        g.setColor(entity.getFaceColor());
                    }
                    g.setStroke(new BasicStroke(1.0f));
                    g.drawLine((int) v1.x, (int) v1.y, (int) v2.x, (int) v2.y);
                }
            }
        }
    }

    public void renderBoxHitbox(Graphics2D g, BoxHitbox hitbox, Camera camera, Color color) {


        // Anti-Aliasing für glattere Linien
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // SCHRITT 1: Die 8 Eckpunkte der Box berechnen
        Vektor3 min = hitbox.getMin();
        Vektor3 max = hitbox.getMax();

        Vektor3[] corners = new Vektor3[8];
        corners[0] = new Vektor3(min.x, min.y, min.z); // Vorne-unten-links
        corners[1] = new Vektor3(max.x, min.y, min.z); // Vorne-unten-rechts
        corners[2] = new Vektor3(max.x, max.y, min.z); // Vorne-oben-rechts
        corners[3] = new Vektor3(min.x, max.y, min.z); // Vorne-oben-links
        corners[4] = new Vektor3(min.x, min.y, max.z); // Hinten-unten-links
        corners[5] = new Vektor3(max.x, min.y, max.z); // Hinten-unten-rechts
        corners[6] = new Vektor3(max.x, max.y, max.z); // Hinten-oben-rechts
        corners[7] = new Vektor3(min.x, max.y, max.z); // Hinten-oben-links

        // SCHRITT 2: Alle Eckpunkte in Kamerakoordinaten umwandeln und auf 2D projizieren
        Vektor2[] projected = new Vektor2[8];

        for (int i = 0; i < 8; i++) {
            // Weltkoordinaten → Kamerakoordinaten (relativ zur Kamera)
            Vektor3 cameraPos = worldToCamera(corners[i], camera);

            // Kamerakoordinaten → 2D-Bildschirmkoordinaten (perspektivische Projektion)
            projected[i] = project(cameraPos, camera.getFov());
        }

        // SCHRITT 3: Kanten der Box zeichnen
        g.setColor(color);
        g.setStroke(new BasicStroke(2));

        // Vordere Fläche (4 Kanten) - Indizes 0,1,2,3
        drawEdge(g, projected, 0, 1); // Unten
        drawEdge(g, projected, 1, 2); // Rechts
        drawEdge(g, projected, 2, 3); // Oben
        drawEdge(g, projected, 3, 0); // Links

        // Hintere Fläche (4 Kanten) - Indizes 4,5,6,7
        drawEdge(g, projected, 4, 5); // Unten
        drawEdge(g, projected, 5, 6); // Rechts
        drawEdge(g, projected, 6, 7); // Oben
        drawEdge(g, projected, 7, 4); // Links

        // Verbindende Kanten (4 Kanten) - von vorne nach hinten
        drawEdge(g, projected, 0, 4); // Unten-links
        drawEdge(g, projected, 1, 5); // Unten-rechts
        drawEdge(g, projected, 2, 6); // Oben-rechts
        drawEdge(g, projected, 3, 7); // Oben-links
    }

    /*
    * Hilfsmethode: Zeichnet eine Kante zwischen zwei projizierten Punkten.
    * Prüft, ob beide Punkte sichtbar sind (nicht null = nicht hinter Kamera).
    * */
    private void drawEdge(Graphics2D g, Vektor2[] points, int index1, int index2) {
        // Sicherheitscheck: Beide Punkte müssen existieren und sichtbar sein
        if (points[index1] != null && points[index2] != null) {
            g.drawLine(
                    (int) points[index1].x, (int) points[index1].y,
                    (int) points[index2].x, (int) points[index2].y
            );
        }
    }
}
