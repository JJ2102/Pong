package rendering;

import math.Matrix4x4;
import math.Vector3;

/**
 * Stellt die 3D-Kamera der Szene dar.
 * <p>
 * Die Kamera kann ausschließlich verschoben werden, eine Blickrichtung gibt es bewusst nicht.
 */
public class Camera {
    private Vector3 position;
    private double zoomFactor = 0.8;

    /**
     * Initialisiert die Kamera an der Standardposition {@code (0, 0, -5)}.
     */
    public Camera() {
        this.position = new Vector3(0, 0, -5);
    }

    /**
     * Liefert die View-Matrix, die alle Objekte relativ zur Kamera ausrichtet.
     *
     * @return View-Matrix zur aktuellen Kameraposition
     */
    public Matrix4x4 getViewMatrix() {
        return Matrix4x4.getViewMatrix(position);
    }

    // ===== Getter und Setter =====
    public Vector3 getPosition() {
        return position;
    }

    public void setPosition(Vector3 position) {
        this.position = position;
    }

    public double getZoomFactor() {
        return zoomFactor;
    }

    public void setZoomFactor(double zoomFactor) {
        this.zoomFactor = zoomFactor;
    }
}
