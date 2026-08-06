package rendering;

import hitboxes.BoxHitbox;
import math.Vector2;
import math.Vector3;
import objects.Entity;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

// Hauptklasse zum Berechnen und Zeichnen von 3D-Szenen auf dem 2D-Bildschirm
// Hauptklasse zum Berechnen und Zeichnen von 3D-Szenen auf dem 2D-Bildschirm
public class Renderer {
    private int width;
    private int height;
    private double scale;

    // Initialisiert den Renderer anhand der Fenstermaße
    public Renderer(int width, int height) {
        this.width = width;
        this.height = height;
        this.scale = Math.min(width, height) / 2.0; // Skalierung stellt sicher, dass quadratische Proportionen erhalten bleiben
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
            return new Vector3(screenPosition.x, screenPosition.y, planeZ); // Abbruch mit Rohwerten bei ungültigem Zustand
        }

        // Mappt die Maus-/Bildschirmpixel zurück in den genutzten Bereich -1 bis 1 unter Beachtung der Skalierung
        double fovAppliedX = (screenPosition.x - width / 2.0) / scale;
        double fovAppliedY = (screenPosition.y - height / 2.0) / scale;

        Vector3 cameraPosition = camera.getPosition();
        double fov = camera.getFov();

        // Bestimmt die Distanz zwischen der Kamera und der Zielfläche (Z-Ebene)
        double depth = planeZ - cameraPosition.z;
        if (depth <= 0) {
            // Falls sich die Kamera hinter der Ebene befindet, liefert die Rechnung unsinnige Werte
            return new Vector3(cameraPosition.x, cameraPosition.y, planeZ);
        }

        // Berechnet die endgültigen Welt-X/Y Koordinaten unter Berücksichtigung des Sichtkegels (FOV)
        double worldX = cameraPosition.x + fovAppliedX * depth / fov;
        double worldY = cameraPosition.y - fovAppliedY * depth / fov; // Y wird invertiert (Bildschirm-Y wächst nach unten, Welt-Y nach oben)

        return new Vector3(worldX, worldY, planeZ);
    }

    // Projiziert einen 3D-Vektor auf die 2D-Bildschirmfläche
    private Vector2 project(Vector3 vector) {
        // Multipliziert den normalisierten Raum (-1 bis 1) mit der Skalierung und zentriert das Ergebnis auf dem Bildschirm
        double screenX = (width / 2.0 + vector.x * scale); 
        double screenY = (height / 2.0 - vector.y * scale); // Auch hier wird die Y-Achse für 2D-Darstellung invertiert
        return new Vector2(screenX, screenY);
    }

    // Zeichnet eine 3D-Entität auf den 2D-Bildschirm
    public void renderEntity(Graphics2D g, Entity entity, Camera camera) {
        renderEntity(g, entity, camera, true);
    }

    // Zeichnet eine 3D-Entität auf den 2D-Bildschirm, optional inkl. gefüllten Flächen
    public void renderEntity(Graphics2D g, Entity entity, Camera camera, boolean renderFaces) {
        if (entity == null || entity.getMesh() == null || entity.getMesh().getVertices() == null) return;

        // Anti-Aliasing (Kantenglättung) aktivieren, damit schräge Linien keine Treppenstufen bilden
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        Mesh mesh = entity.getMesh();

        // --- Render-Pipeline durchlaufen ---
        // 1. Lokale Punkte an die Position/Rotation des Objekts in der Welt rücken
        List<Vector3> transformed = RenderPipeline.applyTransform(entity.getMesh().getVertices(), entity.getTransform());
        // 2. Punkte relativ zur Kamera platzieren und ans Sichtfeld anpassen
        List<Vector3> fovApplied = RenderPipeline.applyCameraParams(transformed, camera);

        // 3. Auf 2D-Pixel-Koordinaten projizieren
        Vector2[] projectedVertices = new Vector2[fovApplied.size()];
        for (Vector3 v : fovApplied) {
            projectedVertices[fovApplied.indexOf(v)] = project(v); // Speichere die fertigen 2D-Punkte in einem Array für schnellen Zugriff
        }

        // --- Gefüllte Flächen zeichnen ---
        if (mesh.getFaces() != null && renderFaces) { 
            for (int[] face : mesh.getFaces()) { 
                if (face == null || face.length == 0) continue; // Leere/ungültige Flächen ignorieren

                // Verbindet die projizierten 2D-Eckpunkte zu einem abgerundeten Polygon und füllt es mit Farbe
                Polygon poly = Drawer.getPolygon(face, projectedVertices); 
                Drawer.drawPolygon(g, poly, entity.getFaceColor());
            }
        }

        // --- Kanten (Wireframe) zeichnen ---
        if (mesh.getEdges() != null) {
            for (int[] edge : mesh.getEdges()) {
                if (edge == null || edge.length < 2) continue; // Eine Kante braucht immer Start- und Endpunkt
                
                int i0 = edge[0];
                int i1 = edge[1];
                if (i0 < 0 || i1 < 0 || i0 >= mesh.getVertices().size() || i1 >= mesh.getVertices().size()) continue; // Index-Out-Of-Bounds-Schutz

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
        corners.add(new Vector3(min.x, min.y, min.z)); // 0: Vorne-unten-links
        corners.add(new Vector3(max.x, min.y, min.z)); // 1: Vorne-unten-rechts
        corners.add(new Vector3(max.x, max.y, min.z)); // 2: Vorne-oben-rechts
        corners.add(new Vector3(min.x, max.y, min.z)); // 3: Vorne-oben-links
        corners.add(new Vector3(min.x, min.y, max.z)); // 4: Hinten-unten-links
        corners.add(new Vector3(max.x, min.y, max.z)); // 5: Hinten-unten-rechts
        corners.add(new Vector3(max.x, max.y, max.z)); // 6: Hinten-oben-rechts
        corners.add(new Vector3(min.x, max.y, max.z)); // 7: Hinten-oben-links

        // --- SCHRITT 2: Punkte in Kamerakoordinaten umwandeln und projizieren ---
        List<Vector3> fovApplied = RenderPipeline.applyCameraParams(corners, camera);

        Vector2[] projected = new Vector2[8];
        for (Vector3 v : fovApplied) {
            projected[fovApplied.indexOf(v)] = project(v);
        }

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
