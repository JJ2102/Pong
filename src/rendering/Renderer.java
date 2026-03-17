package rendering;

import hitboxes.BoxHitbox;
import math.Matrix4x4;
import math.Vektor2;
import math.Vektor3;
import objekts.Entity;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Renderer {
    private int width, height;
    private double scale;

    // Matrizen
    private Matrix4x4 projectionMatrix;

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
    public Vektor3 screenToWorld(Vektor2 screenPos, double planeZ, Camera cam) {
        if (width <= 0 || height <= 0 || cam == null) {
            return new Vektor3(screenPos.x, screenPos.y, planeZ);
        }

        // screen in fovApplied Koordinaten
        double fovAppliedX = (screenPos.x - width / 2.0) / scale;
        double fovAppliedY = (screenPos.y - height / 2.0) / scale;

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
        // => worldX = fovAppliedX * worldZ / fov
        double worldX = camPos.x + fovAppliedX * depth / fov;
        double worldY = camPos.y - fovAppliedY * depth / fov; // y umkehren (Bildschirm y wächst nach unten)

        return new Vektor3(worldX, worldY, planeZ);
    }

    // Projektion von 3D -> 2D
    private Vektor2 project(Vektor3 vektor) {
        double screenX = (width / 2.0 + vektor.x * scale); // x-Koordinate auf Bildschirm (mit Skalierung)
        double screenY = (height / 2.0 - vektor.y * scale); // y-Koordinate auf Bildschirm (mit Skalierung, y umgekehrt)
        return new Vektor2(screenX, screenY);
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

        // Entity Mesh in welt positionieren (vertices -> transformierten Eckpunkten)
        List<Vektor3> transformed = RenderPipeline.applyTransform(entity.getMesh().vertices, entity.getTransform());
        // transformed -> cameraKoordinaten -> camera FOV angepassten Koordinaten
        List<Vektor3> fovApplied = RenderPipeline.applyCameraParams(transformed, camera);

        // Alle Vertex-Positionen durch die Model- und View-Matrix transformieren und dann auf 2D projizieren
        Vektor2[] projectedVertices = new Vektor2[fovApplied.size()];
        for (Vektor3 v : fovApplied) {
            projectedVertices[fovApplied.indexOf(v)] = project(v);
        }

        // Flächen zeichnen
        if (mesh.faces != null && renderFaces) { // Sicherheitscheck
            for (int[] face : mesh.faces) { // geht durch alle Flächen des Meshes
                if (face == null || face.length == 0) continue; // Leere Fläche überspringen

                Polygon poly = Drawer.getPolygon(face, projectedVertices); // erstellt ein Polygon aus den projizierten Eckpunkten der Fläche
                Drawer.drawPolygon(g, poly, entity.getFaceColor());
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
                    Drawer.drawLine(g, v1, v2, entity.getEdgeColor());
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

        List<Vektor3> corners = new ArrayList<>();
        corners.add(new Vektor3(min.x, min.y, min.z)); // Vorne-unten-links
        corners.add(new Vektor3(max.x, min.y, min.z)); // Vorne-unten-rechts
        corners.add(new Vektor3(max.x, max.y, min.z)); // Vorne-oben-rechts
        corners.add(new Vektor3(min.x, max.y, min.z)); // Vorne-oben-links
        corners.add(new Vektor3(min.x, min.y, max.z)); // Hinten-unten-links
        corners.add(new Vektor3(max.x, min.y, max.z)); // Hinten-unten-rechts
        corners.add(new Vektor3(max.x, max.y, max.z)); // Hinten-oben-rechts
        corners.add(new Vektor3(min.x, max.y, max.z)); // Hinten-oben-links

        // SCHRITT 2: Alle Eckpunkte in Kamerakoordinaten umwandeln und auf 2D projizieren
        List<Vektor3> fovApplied = RenderPipeline.applyCameraParams(corners, camera);

        Vektor2[] projected = new Vektor2[8];
        for (Vektor3 v : fovApplied) {
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

    // ===== Utility-Methoxiden =====
    // Konverter Welt- zu Bildschirmkoordinaten
    public Vektor2 worldToScreen(Vektor3 v, Camera camera) {
        List<Vektor3> vektorList = new ArrayList<>();
        vektorList.add(v);

        Vektor3 appliedFOV = RenderPipeline.applyCameraParams(vektorList, camera).getFirst();

        return project(appliedFOV); // 2D-Projektion
    }
}
