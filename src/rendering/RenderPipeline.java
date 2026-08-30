package rendering;

import math.Matrix4x4;
import math.Vector2;
import math.Vector3;

// Stellt die Matrizen bereit, die 3D-Weltkoordinaten in kameraspezifische Projektionskoordinaten überführen
public final class RenderPipeline {

    // Reine Hilfsklasse, wird nie instanziiert
    private RenderPipeline() {
    }

    // Fasst alle drei Schritte der Pipeline zu einer einzigen Matrix zusammen
    public static Matrix4x4 getModelViewProjection(Transform transform, Camera camera) {
        // Schritt 1: Die Model-Matrix überführt lokale Objektkoordinaten in Weltkoordinaten
        Matrix4x4 modelMatrix = Matrix4x4.getTransformationMatrix(transform);

        // Schritt 2 und 3 stecken bereits in der View-Projektions-Matrix
        return getViewProjection(camera).multiply(modelMatrix);
    }

    // Kombiniert View- und Projektionsmatrix für Punkte, die schon in Weltkoordinaten vorliegen
    public static Matrix4x4 getViewProjection(Camera camera) {
        // Schritt 2: Die View-Matrix richtet die Welt relativ zur Kamera aus
        Matrix4x4 viewMatrix = camera.getViewMatrix();

        // Schritt 3: Die Projektionsmatrix skaliert anhand des Sichtfelds (Field of View)
        Matrix4x4 projectionMatrix = Matrix4x4.getProjectionMatrix(camera.getFov());

        // Matrizen wirken von rechts nach links: erst die View-, dann die Projektionsmatrix
        return projectionMatrix.multiply(viewMatrix);
    }

    // Kehrt View und Projektion um: rechnet einen Punkt aus dem normalisierten Raum
    // zurück auf eine vorgegebene Z-Ebene in Weltkoordinaten
    // Das ist das Gegenstück zu getViewProjection und steht bewusst daneben: ändert sich
    // die Projektion, muss diese Umkehrung im selben Zug mit angepasst werden
    public static Vector3 reverseViewProjection(Vector2 normalized, double planeZ, Camera camera) {
        Vector3 cameraPosition = camera.getPosition();

        // Schritt 3 rückwärts: Die Projektion multipliziert mit dem Sichtfeld und teilt durch
        // die Tiefe, hier wird also mit der Tiefe multipliziert und durch das Sichtfeld geteilt
        double depth = planeZ - cameraPosition.getZ(); // Abstand der Z-Ebene zur Kamera
        double factor = depth / camera.getFov();

        // Schritt 2 rückwärts: Das Zwischenergebnis liegt relativ zur Kamera,
        // die View-Matrix wird also durch Aufaddieren ihrer Position rückgängig gemacht
        return new Vector3(
                cameraPosition.getX() + normalized.getX() * factor,
                cameraPosition.getY() + normalized.getY() * factor,
                planeZ);
    }
}
