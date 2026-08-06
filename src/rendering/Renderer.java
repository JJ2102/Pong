package rendering;

import hitboxes.BoxHitbox;
import math.Vector2;
import math.Vector3;
import objects.Entity;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Renderer {
    private int width;
    private int height;
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

    /**
     * Konvertiert Bildschirmkoordinaten (pixels) in Weltkoordinaten auf einer Ebene z = planeZ.
     * Vereinfacht: berücksichtigt Kamera-Position und FOV, nicht Kamera-Rotation.
     */
    public Vector3 screenToWorld(Vector2 screenPosition, double planeZ, Camera camera) {
        if (width <= 0 || height <= 0 || camera == null) {
            return new Vector3(screenPosition.x, screenPosition.y, planeZ);
        }

        // screen in fovApplied Koordinaten
        double fovAppliedX = (screenPosition.x - width / 2.0) / scale;
        double fovAppliedY = (screenPosition.y - height / 2.0) / scale;

        // Kamera-Parameter
        Vector3 cameraPosition = camera.getPosition();
        double fov = camera.getFov();

        // Tiefe (Z der Ebene relativ zur Kamera)
        double depth = planeZ - cameraPosition.z; // Differenz der Z-Koordinaten von Kamera und Ziel-Ebene
        if (depth <= 0) {
            // Ebene ist hinter der Kamera, fallback auf Kameraposition
            return new Vector3(cameraPosition.x, cameraPosition.y, planeZ);
        }

        double worldX = cameraPosition.x + fovAppliedX * depth / fov;
        double worldY = cameraPosition.y - fovAppliedY * depth / fov; // y umkehren (Bildschirm y wächst nach unten)

        return new Vector3(worldX, worldY, planeZ);
    }

    // Projektion von 3D -> 2D
    private Vector2 project(Vector3 vector) {
        double screenX = (width / 2.0 + vector.x * scale); // x-Koordinate auf Bildschirm (mit Skalierung)
        double screenY = (height / 2.0 - vector.y * scale); // y-Koordinate auf Bildschirm (mit Skalierung, y umgekehrt)
        return new Vector2(screenX, screenY);
    }

    // Render Entity
    public void renderEntity(Graphics2D g, Entity entity, Camera camera) {
        renderEntity(g, entity, camera, true);
    }

    public void renderEntity(Graphics2D g, Entity entity, Camera camera, boolean renderFaces) {
        if (entity == null || entity.getMesh() == null || entity.getMesh().getVertices() == null) return;

        // Anti-Aliasing für sauberere Linien
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Mesh und Transformation des Objekts holen
        Mesh mesh = entity.getMesh();

        // Entity Mesh in welt positionieren (vertices -> transformierten Eckpunkten)
        List<Vector3> transformed = RenderPipeline.applyTransform(entity.getMesh().getVertices(), entity.getTransform());
        // transformed -> cameraKoordinaten -> camera FOV angepassten Koordinaten
        List<Vector3> fovApplied = RenderPipeline.applyCameraParams(transformed, camera);

        // Alle Vertex-Positionen durch die Model- und View-Matrix transformieren und dann auf 2D projizieren
        Vector2[] projectedVertices = new Vector2[fovApplied.size()];
        for (Vector3 v : fovApplied) {
            projectedVertices[fovApplied.indexOf(v)] = project(v);
        }

        // Flächen zeichnen
        if (mesh.getFaces() != null && renderFaces) { // Sicherheitscheck
            for (int[] face : mesh.getFaces()) { // geht durch alle Flächen des Meshes
                if (face == null || face.length == 0) continue; // Leere Fläche überspringen

                Polygon poly = Drawer.getPolygon(face, projectedVertices); // erstellt ein Polygon aus den projizierten Eckpunkten der Fläche
                Drawer.drawPolygon(g, poly, entity.getFaceColor());
            }
        }

        // Kanten zeichnen
        if (mesh.getEdges() != null) {
            for (int[] edge : mesh.getEdges()) {
                if (edge == null || edge.length < 2) continue;
                int i0 = edge[0];
                int i1 = edge[1];
                if (i0 < 0 || i1 < 0 || i0 >= mesh.getVertices().size() || i1 >= mesh.getVertices().size()) continue; // Sicherheitscheck

                Vector2 v1 = projectedVertices[i0];
                Vector2 v2 = projectedVertices[i1];

                // Zeichnen der Kante
                if (v1 != null && v2 != null) {
                    Drawer.drawLine(g, v1, v2, entity.getEdgeColor());
                }
            }
        }
    }

    public void renderBoxHitbox(Graphics2D g, BoxHitbox hitbox, Camera camera, Color color) {
        // Anti-Aliasing für glattere Linien
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // SCHRITT 1: Die 8 Eckpunkte der Box berechnen
        Vector3 min = hitbox.getMin();
        Vector3 max = hitbox.getMax();

        List<Vector3> corners = new ArrayList<>();
        corners.add(new Vector3(min.x, min.y, min.z)); // Vorne-unten-links
        corners.add(new Vector3(max.x, min.y, min.z)); // Vorne-unten-rechts
        corners.add(new Vector3(max.x, max.y, min.z)); // Vorne-oben-rechts
        corners.add(new Vector3(min.x, max.y, min.z)); // Vorne-oben-links
        corners.add(new Vector3(min.x, min.y, max.z)); // Hinten-unten-links
        corners.add(new Vector3(max.x, min.y, max.z)); // Hinten-unten-rechts
        corners.add(new Vector3(max.x, max.y, max.z)); // Hinten-oben-rechts
        corners.add(new Vector3(min.x, max.y, max.z)); // Hinten-oben-links

        // SCHRITT 2: Alle Eckpunkte in Kamerakoordinaten umwandeln und auf 2D projizieren
        List<Vector3> fovApplied = RenderPipeline.applyCameraParams(corners, camera);

        Vector2[] projected = new Vector2[8];
        for (Vector3 v : fovApplied) {
            projected[fovApplied.indexOf(v)] = project(v);
        }

        // SCHRITT 3: Kanten der Box zeichnen
        g.setColor(color);
        g.setStroke(new BasicStroke(2));

        // Vordere Fläche (4 Kanten) - Indizes 0,1,2,3
        Drawer.drawLine(g, projected[0], projected[1], color); // Unten
        Drawer.drawLine(g, projected[1], projected[2], color); // Rechts
        Drawer.drawLine(g, projected[2], projected[3], color); // Oben
        Drawer.drawLine(g, projected[3], projected[0], color); // Links

        // Hintere Fläche (4 Kanten) - Indizes 4,5,6,7
        Drawer.drawLine(g, projected[4], projected[5], color); // Unten
        Drawer.drawLine(g, projected[5], projected[6], color); // Rechts
        Drawer.drawLine(g, projected[6], projected[7], color); // Oben
        Drawer.drawLine(g, projected[7], projected[4], color); // Links

        // Verbindende Kanten (4 Kanten) - von vorne nach hinten
        Drawer.drawLine(g, projected[0], projected[4], color); // Unten-links
        Drawer.drawLine(g, projected[1], projected[5], color); // Unten-rechts
        Drawer.drawLine(g, projected[2], projected[6], color); // Oben-rechts
        Drawer.drawLine(g, projected[3], projected[7], color); // Oben-links
    }

    // ===== Utility-Methoden =====
    // Konverter Welt- zu Bildschirmkoordinaten
    public Vector2 worldToScreen(Vector3 v, Camera camera) {
        List<Vector3> vectorList = new ArrayList<>();
        vectorList.add(v);

        Vector3 appliedFov = RenderPipeline.applyCameraParams(vectorList, camera).getFirst();

        return project(appliedFov); // 2D-Projektion
    }
}
