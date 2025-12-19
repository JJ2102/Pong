package rendering;

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

        // Rotation um y-Achse (Beispiel)
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

    public void renderEntity(Graphics2D g, Entity entity, Camera camera) {
        if (entity == null || entity.getMesh() == null || entity.getMesh().vertices == null) return;

        // Anti-Aliasing für sauberere Linien
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        Mesh mesh = entity.getMesh();
        Transform transform = entity.getTransform();

        // Flächen
        if (mesh.faces != null) {
            for (int[] face : mesh.faces) {
                if (face == null || face.length == 0) continue;
                Polygon poly = new Polygon();
                for (int idx : face) {
                    if (idx < 0 || idx >= mesh.vertices.size()) continue;


                    // Objekt-Transformation → Kamera-Transformation → Projektion
                    Vektor3 worldPos = applyTransform(mesh.vertices.get(idx), transform);
                    Vektor3 cameraPos = worldToCamera(worldPos, camera);
                    Vertex v = project(cameraPos, camera.getFov());

                    // Hinzufügen des projizierten Punkts zum Polygon
                    if (v != null) {
                        poly.addPoint(v.x, v.y);
                    }
                }
                // Nur zeichnen, wenn mindestens ein Dreieck möglich ist
                if (poly.npoints >= 3) {
                    g.setColor(entity.getColor());
                    g.fillPolygon(poly);
                    g.setColor(Color.BLACK);
//                    g.drawPolygon(poly);
                }
            }
        }

        // Kanten
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
                    g.setColor(Color.BLACK);
                    g.drawLine(v1.x, v1.y, v2.x, v2.y);
                }
            }
        }
    }
}
