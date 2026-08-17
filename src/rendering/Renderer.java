package rendering;

import hitboxes.BoxHitbox;
import math.Vector2;
import math.Vector3;
import objects.Entity;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.RenderingHints;
import java.util.ArrayList;
import java.util.List;

// Hauptklasse zum Berechnen und Zeichnen von 3D-Szenen auf dem 2D-Bildschirm
public class Renderer {
    private int width;
    private int height;
    private double scale;

    // Initialisiert den Renderer anhand der Fenstermaße
    public Renderer(int width, int height) {
        this.width = width;
        this.height = height;
        // Skalierung stellt sicher, dass quadratische Proportionen erhalten bleiben
        this.scale = Math.min(width, height) / 2.0;
    }

    // Aktualisiert die Abmessungen und den Skalierungsfaktor (z.B. bei Fensteränderungen)
    public void updateSize(int width, int height) {
        this.width = width;
        this.height = height;
        this.scale = Math.min(width, height) / 2.0; // Passt die Skalierung nach Resize dynamisch an
    }

    // Wandelt 2D-Bildschirmkoordinaten in 3D-Weltkoordinaten auf einer Z-Ebene um
    public Vector3 screenToWorld(Vector2 screenPosition, double planeZ, Camera camera) {
        if (width <= 0 || height <= 0 || camera == null) {
            // Abbruch mit Rohwerten bei ungültigem Zustand
            return new Vector3(screenPosition.getX(), screenPosition.getY(), planeZ);
        }

        // Mappt die Maus-/Bildschirmpixel zurück in den genutzten Bereich -1 bis 1 unter Beachtung der Skalierung
        double fovAppliedX = (screenPosition.getX() - width / 2.0) / scale;
        double fovAppliedY = (screenPosition.getY() - height / 2.0) / scale;

        Vector3 cameraPosition = camera.getPosition();
        double fov = camera.getFov();

        // Bestimmt die Distanz zwischen der Kamera und der Zielfläche (Z-Ebene)
        double depth = planeZ - cameraPosition.getZ();
        if (depth <= 0) {
            // Falls sich die Kamera hinter der Ebene befindet, liefert die Rechnung unsinnige Werte
            return new Vector3(cameraPosition.getX(), cameraPosition.getY(), planeZ);
        }

        // Berechnet die endgültigen Welt-X/Y Koordinaten unter Berücksichtigung des Sichtkegels (FOV)
        double worldX = cameraPosition.getX() + fovAppliedX * depth / fov;
        // Y wird invertiert (Bildschirm-Y wächst nach unten, Welt-Y nach oben)
        double worldY = cameraPosition.getY() - fovAppliedY * depth / fov;

        return new Vector3(worldX, worldY, planeZ);
    }

    // Projiziert einen 3D-Vektor auf die 2D-Bildschirmfläche
    private Vector2 project(Vector3 vector) {
        // Multipliziert den normalisierten Raum (-1 bis 1) mit der Skalierung und zentriert das Ergebnis
        double screenX = (width / 2.0 + vector.getX() * scale);
        // Auch hier wird die y-Achse für 2D-Darstellung invertiert
        double screenY = (height / 2.0 - vector.getY() * scale);
        return new Vector2(screenX, screenY);
    }

    // Projiziert eine Liste von 3D-Punkten der Reihe nach auf 2D-Bildschirmkoordinaten
    private Vector2[] projectAll(List<Vector3> vectors) {
        Vector2[] projected = new Vector2[vectors.size()];
        for (int i = 0; i < vectors.size(); i++) {
            projected[i] = project(vectors.get(i));
        }
        return projected;
    }

    // Zeichnet eine 3D-Entität auf den 2D-Bildschirm
    public void renderEntity(Graphics2D g, Entity entity, Camera camera) {
        renderEntity(g, entity, camera, true);
    }

    // Zeichnet eine 3D-Entität auf den 2D-Bildschirm, optional inkl. gefüllten Flächen
    public void renderEntity(Graphics2D g, Entity entity, Camera camera, boolean renderFaces) {
        if (entity == null || entity.getMesh() == null || entity.getMesh().getVertices() == null) {
            return;
        }

        // Anti-Aliasing (Kantenglättung) aktivieren, damit schräge Linien keine Treppenstufen bilden
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        Mesh mesh = entity.getMesh();

        // --- Render-Pipeline durchlaufen ---
        // 1. Lokale Punkte an die Position/Rotation des Objekts in der Welt rücken
        List<Vector3> transformed = RenderPipeline.applyTransform(mesh.getVertices(), entity.getTransform());
        // 2. Punkte relativ zur Kamera platzieren und ans Sichtfeld anpassen
        List<Vector3> fovApplied = RenderPipeline.applyCameraParams(transformed, camera);

        // 3. Auf 2D-Pixel-Koordinaten projizieren
        Vector2[] projectedVertices = projectAll(fovApplied);

        // --- Gefüllte Flächen zeichnen ---
        if (mesh.getFaces() != null && renderFaces) {
            for (int[] face : mesh.getFaces()) {
                if (face == null || face.length == 0) { // Leere/ungültige Flächen ignorieren
                    continue;
                }

                // Verbindet die projizierten 2D-Eckpunkte zu einem Polygon und füllt es mit Farbe
                Polygon poly = Drawer.getPolygon(face, projectedVertices);
                Drawer.drawPolygon(g, poly, entity.getFaceColor());
            }
        }

        // --- Kanten (Wireframe) zeichnen ---
        if (mesh.getEdges() != null) {
            for (int[] edge : mesh.getEdges()) {
                if (edge == null || edge.length < 2) { // Eine Kante braucht immer Start- und Endpunkt
                    continue;
                }

                int i0 = edge[0];
                int i1 = edge[1];
                // Index-Out-Of-Bounds-Schutz
                if (i0 < 0 || i1 < 0 || i0 >= projectedVertices.length || i1 >= projectedVertices.length) {
                    continue;
                }

                Vector2 v1 = projectedVertices[i0];
                Vector2 v2 = projectedVertices[i1];

                // Verbindet Start- und Endpunkt mit einer farbigen Linie
                if (v1 != null && v2 != null) {
                    Drawer.drawLine(g, v1, v2, entity.getEdgeColor());
                }
            }
        }
    }

    // Zeichnet eine Drahtgitterdarstellung (Kanten) der 3D-Hitbox zur Visualisierung
    public void renderBoxHitbox(Graphics2D g, BoxHitbox hitbox, Camera camera, Color color) {
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // --- SCHRITT 1: Die 8 Eckpunkte der Box aus Min/Max berechnen ---
        Vector3 min = hitbox.getMin();
        Vector3 max = hitbox.getMax();

        List<Vector3> corners = new ArrayList<>();
        corners.add(new Vector3(min.getX(), min.getY(), min.getZ())); // 0: Vorne-unten-links
        corners.add(new Vector3(max.getX(), min.getY(), min.getZ())); // 1: Vorne-unten-rechts
        corners.add(new Vector3(max.getX(), max.getY(), min.getZ())); // 2: Vorne-oben-rechts
        corners.add(new Vector3(min.getX(), max.getY(), min.getZ())); // 3: Vorne-oben-links
        corners.add(new Vector3(min.getX(), min.getY(), max.getZ())); // 4: Hinten-unten-links
        corners.add(new Vector3(max.getX(), min.getY(), max.getZ())); // 5: Hinten-unten-rechts
        corners.add(new Vector3(max.getX(), max.getY(), max.getZ())); // 6: Hinten-oben-rechts
        corners.add(new Vector3(min.getX(), max.getY(), max.getZ())); // 7: Hinten-oben-links

        // --- SCHRITT 2: Punkte in Kamerakoordinaten umwandeln und projizieren ---
        List<Vector3> fovApplied = RenderPipeline.applyCameraParams(corners, camera);
        Vector2[] projected = projectAll(fovApplied);

        // --- SCHRITT 3: Alle 12 Kanten des Quaders einzeichnen ---
        g.setColor(color);
        g.setStroke(new BasicStroke(2)); // Die Hitbox-Linien werden etwas dicker (2px) gezeichnet

        // Vordere Fläche (4 Kanten)
        Drawer.drawLine(g, projected[0], projected[1], color);
        Drawer.drawLine(g, projected[1], projected[2], color);
        Drawer.drawLine(g, projected[2], projected[3], color);
        Drawer.drawLine(g, projected[3], projected[0], color);

        // Hintere Fläche (4 Kanten)
        Drawer.drawLine(g, projected[4], projected[5], color);
        Drawer.drawLine(g, projected[5], projected[6], color);
        Drawer.drawLine(g, projected[6], projected[7], color);
        Drawer.drawLine(g, projected[7], projected[4], color);

        // Verbindende Kanten zwischen vorne und hinten (4 Kanten)
        Drawer.drawLine(g, projected[0], projected[4], color);
        Drawer.drawLine(g, projected[1], projected[5], color);
        Drawer.drawLine(g, projected[2], projected[6], color);
        Drawer.drawLine(g, projected[3], projected[7], color);
    }

    // ===== Utility-Methoden =====
    // Hilfsmethode: Konvertiert 3D-Welt- in 2D-Bildschirmkoordinaten
    public Vector2 worldToScreen(Vector3 v, Camera camera) {
        List<Vector3> vectorList = new ArrayList<>();
        vectorList.add(v);

        // Durchläuft nur Schritt 2 & 3 der Pipeline für einen einzelnen Punkt
        Vector3 appliedFov = RenderPipeline.applyCameraParams(vectorList, camera).getFirst();

        return project(appliedFov); // Führt die finale 2D-Projektion aus
    }
}
