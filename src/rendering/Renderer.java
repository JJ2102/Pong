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
    private Matrix4x4 modelMatrix;
    private Matrix4x4 viewMatrix;

    public Renderer(int width, int height) {
        this.width = width;
        this.height = height;
        this.scale = Math.min(width, height) / 2.0;
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

    // Converter von Welt- zu Kamerakoordinaten
    public Vektor3 worldToCamera(Vektor3 worldPos) {
        return viewMatrix.multiply(worldPos);
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

    // Transformation anwenden (Scale, Rotation, Translation)
    private Vektor3 applyTransform(Vektor3 v) {
        return modelMatrix.multiply(v);
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
        generateModelMatrix(transform.position, transform.rotation, transform.scale);

        // Alle Vertex-Positionen durch die Model- und View-Matrix transformieren und dann auf 2D projizieren
        Vektor2[] projectedVertices = new Vektor2[mesh.vertices.size()];
        for (Vektor3 v : mesh.vertices) {
            Vektor3 transformed = applyTransform(v); // Transformieren in Weltkoordinaten
            projectedVertices[mesh.vertices.indexOf(v)] = worldToScreen(transformed, camera);
        }

        // Flächen zeichnen
        if (mesh.faces != null && renderFaces) { // Sicherheitscheck
            for (int[] face : mesh.faces) { // geht durch alle Flächen des Meshes
                if (face == null || face.length == 0) continue; // Leere Fläche überspringen

                Polygon poly = getPolygon(face, mesh, projectedVertices); // erstellt ein Polygon aus den projizierten Eckpunkten der Fläche
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

                Vektor2 v1 = projectedVertices[i0];
                Vektor2 v2 = projectedVertices[i1];

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

    private Polygon getPolygon(int[] face, Mesh mesh, Vektor2[] projectedVertices) {
        Polygon poly = new Polygon();

        for (int idx : face) { // geht durch alle Eckpunkte der Fläche; idx ist der Index im Vertex-Array
            if (idx < 0 || idx >= mesh.vertices.size()) continue; // idx nicht kleiner 0 oder größer als Anzahl der Eckpunkte

            Vektor2 v = projectedVertices[idx];

            // Hinzufügen des projizierten Punkts zum Polygon
            if (v != null) {
                poly.addPoint((int) v.x, (int) v.y);
            }
        }
        return poly;
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
            Vektor3 cameraPos = worldToCamera(corners[i]);

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

    // ===== Utility-Methoxiden =====
    // Zeichnet eine Kante zwischen zwei Punkten, wenn beide Punkte gültig sind
    private void drawEdge(Graphics2D g, Vektor2[] points, int index1, int index2) {
        // Sicherheitscheck: Beide Punkte müssen existieren und sichtbar sein
        if (points[index1] != null && points[index2] != null) {
            g.drawLine(
                    (int) points[index1].x, (int) points[index1].y,
                    (int) points[index2].x, (int) points[index2].y
            );
        }
    }

    // Matrizen-Generierung
    private void generateModelMatrix(Vektor3 position, Vektor3 rotation, Vektor3 scale) {
        Matrix4x4 translationMatrix = Matrix4x4.getTranslationMatrix(
                position.x,
                position.y,
                position.z
        );


        Matrix4x4 rotationMatrix = Matrix4x4.getRotationMatrix(
                rotation.x,
                rotation.y,
                rotation.z
        );

        Matrix4x4 scaleMatrix = Matrix4x4.getScalingMatrix(
                scale.x,
                scale.y,
                scale.z
        );

        // Model-Matrix: Alle Objekt-Transformationen (Translation * Rotation * Scale)
        modelMatrix = translationMatrix.multiply(rotationMatrix).multiply(scaleMatrix);
    }

    private void generateViewMatrix(Vektor3 camPos, Vektor3 camRot) {
        // View-Matrix: Kamera-Transformationen (Rotation * Translation)
        Matrix4x4 camTranslation = Matrix4x4.getTranslationMatrix(
                -camPos.x,
                -camPos.y,
                -camPos.z
        );

        Matrix4x4 camRotation = Matrix4x4.getRotationMatrix(
                -camRot.x,
                -camRot.y,
                -camRot.z
        );

        viewMatrix = camRotation.multiply(camTranslation);
    }

    // Konverter Welt- zu Bildschirmkoordinaten
    public Vektor2 worldToScreen(Vektor3 v, Camera camera) {
        // Matrizen für Kamera-Transformationen
        generateViewMatrix(camera.getPosition(), camera.getRotation());

        Vektor3 cameraPos = worldToCamera(v);
        return project(cameraPos, camera.getFov()); // 2D-Projektion
    }
}
