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

/**
 * Hauptklasse zum Berechnen und Zeichnen von 3D-Szenen auf dem 2D-Bildschirm.
 * <p>
 * Rechnet zwischen Weltkoordinaten und Bildschirmpixeln um und zeichnet {@link Entity}-Objekte
 * aus Sicht der {@link Camera}.
 */
public class Renderer {
    private static final Stroke EDGE_STROKE = new BasicStroke(1.0f); // Normale Objektkanten sind 1 px dünn

    private final Camera camera; // Blickpunkt, aus dem alle Objekte gezeichnet werden

    private int width;
    private int height;
    private double scale;

    /**
     * Initialisiert den Renderer anhand der Fenstermaße und des Blickpunkts.
     *
     * @param width Breite der Zeichenfläche in Pixeln
     * @param height Höhe der Zeichenfläche in Pixeln
     * @param camera {@link Camera}, aus deren Sicht gezeichnet wird
     */
    public Renderer(int width, int height, Camera camera) {
        this.camera = camera;
        updateSize(width, height);
    }

    /**
     * Aktualisiert die Abmessungen und den Skalierungsfaktor (z. B. bei Fensteränderungen).
     * <p>
     * Der Skalierungsfaktor ist die halbe kleinere Seite, damit der normalisierte Bereich
     * von -1 bis 1 immer vollständig ins Fenster passt.
     *
     * @param width neue Breite der Zeichenfläche in Pixeln
     * @param height neue Höhe der Zeichenfläche in Pixeln
     */
    public final void updateSize(int width, int height) {
        this.width = width;
        this.height = height;

        // Kleinster Wert von Breite und Höhe mindestens 1 wird genommen und halbiert
        this.scale = Math.max(1, Math.min(width, height)) / 2.0;
    }

    /**
     * Wandelt 2D-Bildschirmkoordinaten in 3D-Weltkoordinaten auf einer Z-Ebene um.
     *
     * @param screenPosition Position auf dem Bildschirm in Pixeln
     * @param planeZ Z-Koordinate der Ebene, auf der der Weltpunkt liegen soll
     * @return Punkt in Weltkoordinaten auf der Ebene {@code z = planeZ}
     */
    public Vector3 screenToWorld(Vector2 screenPosition, double planeZ) {
        // Schritt 1: Pixel zurück in den normalisierten Raum, den auch die Projektion nutzt
        // Nur dieser Schritt hängt von der Fenstergröße ab und bleibt deshalb hier
        Vector2 normalized = unproject(screenPosition);

        // Schritt 2: View und Projektion kehrt die Pipeline um, die sie auch vorwärts berechnet
        return RenderPipeline.reverseViewProjection(normalized, planeZ, camera);
    }

    /**
     * Projiziert einen Punkt aus dem normalisierten Projektionsraum auf die 2D-Bildschirmfläche.
     *
     * @param vector bereits mit der Pipeline-Matrix transformierter Punkt
     * @return Position auf dem Bildschirm in Pixeln
     */
    private Vector2 project(Vector3 vector) {
        // Halbe Fensterbreite + Versatz relativ zur Fenstermitte
        double screenX = (width / 2.0 + vector.getX() * scale);
        // Halbe Fensterhöhe - Versatz relativ zur Fenstermitte (y-Achse ist in Swing nach unten gerichtet)
        double screenY = (height / 2.0 - vector.getY() * scale);
        return new Vector2(screenX, screenY);
    }

    /**
     * Umkehrung von {@link #project}: rechnet Bildschirmpixel zurück in den normalisierten Raum (-1 bis 1).
     *
     * @param screenPosition Position auf dem Bildschirm in Pixeln
     * @return Position im normalisierten Projektionsraum
     */
    private Vector2 unproject(Vector2 screenPosition) {
        double x = (screenPosition.getX() - width / 2.0) / scale;
        // Dieselbe Spiegelung der y-Achse wie in project, nur rückwärts
        double y = (height / 2.0 - screenPosition.getY()) / scale;
        return new Vector2(x, y);
    }

    /**
     * Wendet die kombinierte Pipeline-Matrix auf alle Punkte an und projiziert sie in einem Durchlauf.
     *
     * @param vertices Eckpunkte in lokalen Objektkoordinaten
     * @param modelViewProjection kombinierte Model-View-Projektions-Matrix
     * @return projizierte Bildschirmpositionen in derselben Reihenfolge wie {@code vertices}
     */
    private Vector2[] projectAll(List<Vector3> vertices, Matrix4x4 modelViewProjection) {
        Vector2[] projected = new Vector2[vertices.size()];
        for (int i = 0; i < vertices.size(); i++) {
            projected[i] = project(modelViewProjection.multiply(vertices.get(i)));
        }
        return projected;
    }

    /**
     * Zeichnet eine 3D-Entität mit gefüllten Flächen und 1 Pixel dünnen Kanten auf den 2D-Bildschirm.
     *
     * @param g Grafikkontext, in den gezeichnet wird
     * @param entity zu zeichnende {@link Entity}
     */
    public void renderEntity(Graphics2D g, Entity entity) {
        renderEntity(g, entity, true, EDGE_STROKE);
    }

    /**
     * Zeichnet eine 3D-Entität mit einer vorgegebenen Linienstärke für die Kanten.
     * <p>
     * Ist die Entität oder ihr {@link Mesh} {@code null}, wird nichts gezeichnet.
     * Der übergebene Stroke bleibt danach im Grafikkontext gesetzt.
     *
     * @param g Grafikkontext, in den gezeichnet wird
     * @param entity zu zeichnende {@link Entity}
     * @param renderFaces ob die Flächen gefüllt gezeichnet werden
     * @param edgeStroke Linienstärke für die Kanten
     */
    public void renderEntity(Graphics2D g, Entity entity, boolean renderFaces, Stroke edgeStroke) {
        if (entity == null || entity.getMesh() == null) {
            return;
        }

        // Anti-Aliasing (Kantenglättung) aktivieren, damit schräge Linien keine Treppenstufen bilden
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        Mesh mesh = entity.getMesh();

        // Render-Pipeline durchlaufen
        // 1. Model-, View- und Projektionsmatrix einmal pro Objekt zu einer Matrix kombinieren
        Matrix4x4 modelViewProjection = RenderPipeline.getModelViewProjection(entity.getTransform(), camera);
        // 2. Alle Eckpunkte damit auf 2D-Pixel-Koordinaten bringen
        Vector2[] projectedVertices = projectAll(mesh.getVertices(), modelViewProjection);

        // Gefüllte Flächen zeichnen
        if (renderFaces) {
            for (int[] face : mesh.getFaces()) {
                // Verbindet die projizierten 2D-Eckpunkte zu einem Polygon und füllt es mit Farbe
                Polygon poly = Drawer.getPolygon(face, projectedVertices);
                Drawer.drawPolygon(g, poly, entity.getFaceColor());
            }
        }

        // Kanten (Wireframe) zeichnen
        g.setStroke(edgeStroke); // Linienstärke einmal für alle Kanten dieses Objekts setzen
        for (int[] edge : mesh.getEdges()) {
            // Verbindet Start- und Endpunkt mit einer farbigen Linie
            Drawer.drawLine(g, projectedVertices[edge[0]], projectedVertices[edge[1]], entity.getEdgeColor());
        }
    }

    // ===== Hilfsmethoden =====

    /**
     * Konvertiert 3D-Weltkoordinaten in 2D-Bildschirmkoordinaten.
     *
     * @param v Punkt in Weltkoordinaten
     * @return Position auf dem Bildschirm in Pixeln
     */
    public Vector2 worldToScreen(Vector3 v) {
        // Der Punkt liegt bereits in Weltkoordinaten, daher sind nur View und Projektion nötig
        return project(RenderPipeline.getViewProjection(camera).multiply(v));
    }
}
