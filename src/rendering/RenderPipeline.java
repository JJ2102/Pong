package rendering;

import math.Matrix4x4;
import math.Vector3;

import java.util.ArrayList;
import java.util.List;

// Transformiert 3D-Weltkoordinaten schrittweise in kameraspezifische Projektionskoordinaten
public class RenderPipeline {
    
    // Schritt 1: Überführt lokale Objektkoordinaten in Weltkoordinaten (Model-Matrix)
    public static List<Vector3> applyTransform(List<Vector3> vertices, Transform transform) {
        // Erzeuge eine kombinierte Transformationsmatrix aus Position, Rotation und Skalierung
        Matrix4x4 transformMatrix = Matrix4x4.getTransformationMatrix(transform);

        List<Vector3> transformed = new ArrayList<>();
        // Wende die Matrix auf jeden einzelnen Punkt des Meshes an
        for (Vector3 v : vertices) {
            Vector3 transformedVertex = transformMatrix.multiply(v); // Punkt wird im Raum verschoben/gedreht
            transformed.add(transformedVertex);
        }
        return transformed;
    }

    // Schritt 2: Richtet Weltkoordinaten relativ zur Kamera aus (View-Matrix)
    private static List<Vector3> applyCameraTransform(List<Vector3> transformed, Camera camera) {
        // Für die View-Matrix wird die Transformation der Kamera umgekehrt auf alle Objekte angewendet
        Matrix4x4 viewMatrix = Matrix4x4.getTransformationMatrix(camera.getInvertedTransform());

        List<Vector3> cameraTransformed = new ArrayList<>();
        for (Vector3 v : transformed) {
            Vector3 transformedVertex = viewMatrix.multiply(v); // Punkte bewegen sich relativ zur Kameraposition
            cameraTransformed.add(transformedVertex);
        }
        return cameraTransformed;
    }

    // Schritt 3: Skaliert Kamerakoordinaten basierend auf dem Sichtfeld (Projektions-Matrix)
    private static List<Vector3> applyFov(List<Vector3> cameraTransformed, double fov) {
        // Erzeuge die Projektionsmatrix anhand des Sichtfelds (Field of View)
        Matrix4x4 projectionMatrix = Matrix4x4.getProjectionMatrix(fov);

        List<Vector3> fovApplied = new ArrayList<>();
        for (Vector3 v : cameraTransformed) {
            Vector3 projectedVertex = projectionMatrix.multiply(v); // Erzeugt den finalen 3D-Wert vor der 2D-Flachdrückung
            fovApplied.add(projectedVertex);
        }
        return fovApplied;
    }

    // Führt View- und Projektions-Transformation (Schritt 2 und 3) nacheinander aus
    public static List<Vector3> applyCameraParams(List<Vector3> transformed, Camera camera) {
        // Pipeline abarbeiten: zuerst Kamera-Ausrichtung, dann Sichtfeld-Skalierung
        List<Vector3> cameraTransformed = RenderPipeline.applyCameraTransform(transformed, camera);
        return RenderPipeline.applyFov(cameraTransformed, camera.getFov());
    }
}
