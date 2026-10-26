package rendering;

import math.Matrix4x4;
import math.Vector2;
import math.Vector3;

/**
 * Stellt die Matrizen bereit, die 3D-Weltkoordinaten in kameraspezifische Projektionskoordinaten überführen.
 */
public final class RenderPipeline {
    /**
     * Privater Konstruktor, da die Hilfsklasse nie instanziiert wird.
     */
    private RenderPipeline() {
    }

    /**
     * Fasst alle drei Schritte der Pipeline (Model, View, Projektion) zu einer einzigen Matrix zusammen.
     * <pre>
     * MVP = Projektion * View * Model
     * </pre>
     *
     * @param transform {@link Transform} des Objekts, aus dem die Model-Matrix entsteht
     * @param camera {@link Camera}, aus deren Sicht projiziert wird
     * @return kombinierte Model-View-Projektions-Matrix
     */
    public static Matrix4x4 getModelViewProjection(Transform transform, Camera camera) {
        // Schritt 1: Die Model-Matrix überführt lokale Objektkoordinaten in Weltkoordinaten
        Matrix4x4 modelMatrix = Matrix4x4.getTransformationMatrix(transform);

        // Schritt 2 und 3 stecken bereits in der View-Projektions-Matrix
        return getViewProjection(camera).multiply(modelMatrix);
    }

    /**
     * Kombiniert View- und Projektionsmatrix für Punkte, die schon in Weltkoordinaten vorliegen.
     *
     * @param camera {@link Camera}, aus deren Sicht projiziert wird
     * @return kombinierte View-Projektions-Matrix
     */
    public static Matrix4x4 getViewProjection(Camera camera) {
        // Schritt 2: Die View-Matrix richtet die Welt relativ zur Kamera aus
        Matrix4x4 viewMatrix = camera.getViewMatrix();

        // Schritt 3: Die Projektionsmatrix skaliert anhand des Zoomfaktors
        Matrix4x4 projectionMatrix = Matrix4x4.getProjectionMatrix(camera.getZoomFactor());

        // Matrizen wirken von rechts nach links: erst die View-, dann die Projektionsmatrix
        return projectionMatrix.multiply(viewMatrix);
    }

    /**
     * Kehrt View und Projektion um und bestimmt den Weltpunkt auf einer Z-Ebene,
     * der auf die gegebene normalisierte Position projiziert wird.
     *
     * @param normalized Position im normalisierten Projektionsraum
     * @param planeZ Z-Koordinate der Ebene, auf der der Weltpunkt liegen soll
     * @param camera {@link Camera}, aus deren Sicht projiziert wurde
     * @return Punkt in Weltkoordinaten auf der Ebene {@code z = planeZ}
     */
    public static Vector3 reverseViewProjection(Vector2 normalized, double planeZ, Camera camera) {
        Vector3 cameraPosition = camera.getPosition();

        double depth = planeZ - cameraPosition.getZ(); // Abstand der Z-Ebene zur Kamera
        double factor = depth / camera.getZoomFactor();

        return new Vector3(
                cameraPosition.getX() + normalized.getX() * factor,
                cameraPosition.getY() + normalized.getY() * factor,
                planeZ);
    }
}
