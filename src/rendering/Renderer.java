package rendering;

import math.Matrix4x4;
import math.Vector2;
import math.Vector3;
import objects.Entity;

import java.awt.BasicStroke;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.util.List;

// Hauptklasse zum Berechnen und Zeichnen von 3D-Szenen auf dem 2D-Bildschirm
public class Renderer {
    private static final Stroke EDGE_STROKE = new BasicStroke(1.0f);   // Normale Objektkanten sind 1px dünn

    private final Camera camera; // Blickpunkt, aus dem alle Objekte gezeichnet werden

    private int width;
    private int height;
    private double scale;

    // Initialisiert den Renderer anhand der Fenstermaße und des Blickpunkts
    public Renderer(int width, int height, Camera camera) {
        this.camera = camera;
        updateSize(width, height);
    }

    // Aktualisiert die Abmessungen und den Skalierungsfaktor (z.B. bei Fensteränderungen)
    public final void updateSize(int width, int height) {
        this.width = width;
        this.height = height;
        // Sichert quadratische Proportionen und bleibt auch bei einer noch nicht
        // ausgelegten Zeichenfläche (Breite/Höhe 0) garantiert größer als null
        this.scale = Math.max(1, Math.min(width, height)) / 2.0;
    }

    // Wandelt 2D-Bildschirmkoordinaten in 3D-Weltkoordinaten auf einer Z-Ebene um
    public Vector3 screenToWorld(Vector2 screenPosition, double planeZ) {
        // Schritt 1: Pixel zurück in den normalisierten Raum, den auch die Projektion nutzt
        // Nur dieser Schritt hängt von der Fenstergröße ab und bleibt deshalb hier
        Vector2 normalized = unproject(screenPosition);

        // Schritt 2: View und Projektion kehrt die Pipeline um, die sie auch vorwärts berechnet
        return RenderPipeline.reverseViewProjection(normalized, planeZ, camera);
    }

    // Projiziert einen 3D-Vektor auf die 2D-Bildschirmfläche
    private Vector2 project(Vector3 vector) {
        // Multipliziert den normalisierten Raum (-1 bis 1) mit der Skalierung und zentriert das Ergebnis
        double screenX = (width / 2.0 + vector.getX() * scale);
        // Auch hier wird die y-Achse für 2D-Darstellung invertiert
        double screenY = (height / 2.0 - vector.getY() * scale);
        return new Vector2(screenX, screenY);
    }

    // Umkehrung von project: rechnet Bildschirmpixel zurück in den normalisierten Raum (-1 bis 1)
    private Vector2 unproject(Vector2 screenPosition) {
        double x = (screenPosition.getX() - width / 2.0) / scale;
        // Dieselbe Spiegelung der y-Achse wie in project, nur rückwärts
        double y = (height / 2.0 - screenPosition.getY()) / scale;
        return new Vector2(x, y);
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
    public void renderEntity(Graphics2D g, Entity entity) {
        renderEntity(g, entity, true, EDGE_STROKE);
    }

    // Zeichnet eine 3D-Entität mit einer vorgegebenen Linienstärke für die Kanten
    public void renderEntity(Graphics2D g, Entity entity, boolean renderFaces, Stroke edgeStroke) {
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

    // ===== Utility-Methoden =====
    // Hilfsmethode: Konvertiert 3D-Welt- in 2D-Bildschirmkoordinaten
    public Vector2 worldToScreen(Vector3 v) {
        // Der Punkt liegt bereits in Weltkoordinaten, daher sind nur View und Projektion nötig
        return project(RenderPipeline.getViewProjection(camera).multiply(v));
    }
}
