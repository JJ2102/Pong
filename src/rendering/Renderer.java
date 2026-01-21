package rendering;

import hitboxes.BoxHitbox;
import math.Vektor3;
import math.Vertex;
import objekts.Entity;

import java.awt.*;

public class Renderer {
    private int width, height;
    private double scale;

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
    private Vertex project(Vektor3 p, double fov) {
        if (p == null) return null;
        if (p.z <= 0) return null; // hinter Kamera
        double x = fov * p.x / p.z;
        double y = fov * p.y / p.z;
        int sx = (int) (width / 2.0 + x * scale);
        int sy = (int) (height / 2.0 - y * scale);
        return new Vertex(sx, sy);
    }

    // Transformation anwenden (Scale, Rotation, Translation)
    private Vektor3 applyTransform(Vektor3 v, Transform t) {
        // Skalierung
        double x = v.x * t.scale.x;
        double y = v.y * t.scale.y;
        double z = v.z * t.scale.z;

        // Rotation um y-Achse
        double cosY = Math.cos(t.rotation.y);
        double sinY = Math.sin(t.rotation.y);
        double rx = cosY * x + sinY * z;
        double rz = -sinY * x + cosY * z;

        // Rotation um x-Achse
        double cosX = Math.cos(t.rotation.x);
        double sinX = Math.sin(t.rotation.x);
        double ry = cosX * y - sinX * rz;
        rz = sinX * y + cosX * rz;

        // Rotation um Z-Achse
        double cosZ = Math.cos(t.rotation.z);
        double sinZ = Math.sin(t.rotation.z);
        double rxx = cosZ * rx - sinZ * ry;
        double ryy = sinZ * rx + cosZ * ry;

        // Translation
        return new Vektor3(rxx + t.position.x, ryy + t.position.y, rz + t.position.z);
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
    public Vektor3 screenToWorld(Vertex screenPos, double planeZ, Camera cam) {
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

        // Flächen zeichnen
        if (mesh.faces != null && renderFaces) { // Sicherheitscheck
            for (int[] face : mesh.faces) { // faces = int[][]
                if (face == null || face.length == 0) continue; // Leere Fläche überspringen

                Polygon poly = new Polygon();

                for (int idx : face) { // face = int[]
                    if (idx < 0 || idx >= mesh.vertices.size()) continue; // idx nicht kleiner 0 oder größer als Anzahl der Eckpunkte

                    // Transformieren der einzelnen Eckpunkte der Fläche
                    Vektor3 worldPos = applyTransform(mesh.vertices.get(idx), transform);
                    // verschiebung der Eckpunkte relativ zur Kamera
                    Vektor3 cameraPos = worldToCamera(worldPos, camera);
                    // Projektion auf 2D-Bildschirm → 2D-Koordinate
                    Vertex v = project(cameraPos, camera.getFov());

                    // Hinzufügen des projizierten Punkts zum Polygon
                    if (v != null) {
                        poly.addPoint(v.x, v.y);
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
                Vektor3 worldPos1 = applyTransform(mesh.vertices.get(i0), transform);
                Vektor3 worldPos2 = applyTransform(mesh.vertices.get(i1), transform);

                // Umwandlung in Kamerakoordinaten
                Vektor3 cameraPos1 = worldToCamera(worldPos1, camera);
                Vektor3 cameraPos2 = worldToCamera(worldPos2, camera);

                // Projektion auf 2D-Bildschirm
                Vertex v1 = project(cameraPos1, camera.getFov());
                Vertex v2 = project(cameraPos2 , camera.getFov());

                // Zeichnen der Kante
                if (v1 != null && v2 != null) {
                    if (renderFaces) {
                        g.setColor(entity.getEdgeColor());
                    } else {
                        g.setColor(entity.getFaceColor());
                    }
                    g.setStroke(new BasicStroke(1.0f));
                    g.drawLine(v1.x, v1.y, v2.x, v2.y);
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
        Vertex[] projected = new Vertex[8];

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
    private void drawEdge(Graphics2D g, Vertex[] points, int index1, int index2) {
        // Sicherheitscheck: Beide Punkte müssen existieren und sichtbar sein
        if (points[index1] != null && points[index2] != null) {
            g.drawLine(
                    points[index1].x, points[index1].y,
                    points[index2].x, points[index2].y
            );
        }
    }
}
