package rendering;

import hitboxes.BoxHitbox;
import math.Matrix4x4;
import math.Vector2;
import math.Vector3;
import meshes.RectangleMesh;
import objects.Entity;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.util.List;

// Hauptklasse zum Berechnen und Zeichnen von 3D-Szenen auf dem 2D-Bildschirm
public class Renderer {
    private static final Stroke EDGE_STROKE = new BasicStroke(1.0f);   // Normale Objektkanten sind 1px dünn
    private static final Stroke HITBOX_STROKE = new BasicStroke(2.0f); // Hitbox-Linien werden dicker gezeichnet

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

    // Wendet die kombinierte Pipeline-Matrix auf alle Punkte an und projiziert sie in einem Durchlauf
    private Vector2[] projectAll(List<Vector3> vertices, Matrix4x4 modelViewProjection) {
        Vector2[] projected = new Vector2[vertices.size()];
        for (int i = 0; i < vertices.size(); i++) {
            projected[i] = project(modelViewProjection.multiply(vertices.get(i)));
        }
        return projected;
    }

    // Zeichnet eine 3D-Entität auf den 2D-Bildschirm
    public void renderEntity(Graphics2D g, Entity entity, Camera camera) {
        renderEntity(g, entity, camera, true);
    }

    // Zeichnet eine 3D-Entität auf den 2D-Bildschirm, optional inkl. gefüllten Flächen
    public void renderEntity(Graphics2D g, Entity entity, Camera camera, boolean renderFaces) {
        renderEntity(g, entity, camera, renderFaces, EDGE_STROKE);
    }

    // Zeichnet eine 3D-Entität mit einer vorgegebenen Linienstärke für die Kanten
    private void renderEntity(Graphics2D g, Entity entity, Camera camera, boolean renderFaces, Stroke edgeStroke) {
        if (entity == null || entity.getMesh() == null) {
            return;
        }

        // Anti-Aliasing (Kantenglättung) aktivieren, damit schräge Linien keine Treppenstufen bilden
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        Mesh mesh = entity.getMesh();

        // --- Render-Pipeline durchlaufen ---
        // 1. Model-, View- und Projektionsmatrix einmal pro Objekt zu einer Matrix kombinieren
        Matrix4x4 modelViewProjection = RenderPipeline.getModelViewProjection(entity.getTransform(), camera);
        // 2. Alle Eckpunkte damit auf 2D-Pixel-Koordinaten bringen
        Vector2[] projectedVertices = projectAll(mesh.getVertices(), modelViewProjection);

        // --- Gefüllte Flächen zeichnen ---
        if (renderFaces) {
            for (int[] face : mesh.getFaces()) {
                // Verbindet die projizierten 2D-Eckpunkte zu einem Polygon und füllt es mit Farbe
                Polygon poly = Drawer.getPolygon(face, projectedVertices);
                Drawer.drawPolygon(g, poly, entity.getFaceColor());
            }
        }

        // --- Kanten (Wireframe) zeichnen ---
        g.setStroke(edgeStroke); // Linienstärke einmal für alle Kanten dieses Objekts setzen
        for (int[] edge : mesh.getEdges()) {
            // Verbindet Start- und Endpunkt mit einer farbigen Linie
            Drawer.drawLine(g, projectedVertices[edge[0]], projectedVertices[edge[1]], entity.getEdgeColor());
        }
    }

    // Zeichnet eine Drahtgitterdarstellung (Kanten) der 3D-Hitbox zur Visualisierung
    public void renderBoxHitbox(Graphics2D g, BoxHitbox hitbox, Camera camera, Color color) {
        if (hitbox == null) {
            return;
        }

        // Eine Hitbox ist ein achsenparalleler Quader, dafür gibt es mit RectangleMesh bereits die passende Geometrie
        Vector3 size = hitbox.getSize();
        Entity boxEntity = new Entity(color, color);
        // RectangleMesh erwartet die halben Kantenlängen, die Hitbox speichert die vollen
        boxEntity.setMesh(new RectangleMesh(size.getX() / 2, size.getY() / 2, size.getZ() / 2));
        boxEntity.getTransform().setPosition(hitbox.getCenter());

        // Nur die Kanten zeichnen, dafür etwas dicker als gewöhnliche Objektkanten
        renderEntity(g, boxEntity, camera, false, HITBOX_STROKE);
    }

    // ===== Utility-Methoden =====
    // Hilfsmethode: Konvertiert 3D-Welt- in 2D-Bildschirmkoordinaten
    public Vector2 worldToScreen(Vector3 v, Camera camera) {
        // Der Punkt liegt bereits in Weltkoordinaten, daher sind nur View und Projektion nötig
        return project(RenderPipeline.getViewProjection(camera).multiply(v));
    }
}
