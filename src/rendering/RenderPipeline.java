package rendering;

import math.Matrix4x4;
import math.Vector3;

import java.util.ArrayList;
import java.util.List;

// Rendering-Pipeline: wandelt Vertices in drei Schritten von lokalen Objekt- in
// Bildschirm-nahe Koordinaten um (Model-Matrix -> View-Matrix -> Projektions-Matrix)
public class RenderPipeline {
    // Schritt 1 (Model-Matrix): Vertices anhand von Position/Rotation/Skalierung des Objekts in Weltkoordinaten bringen
    public static List<Vector3> applyTransform(List<Vector3> vertices, Transform transform) {
        Matrix4x4 transformMatrix = Matrix4x4.getTransformationMatrix(transform);

        List<Vector3> transformed = new ArrayList<>();
        for (Vector3 v : vertices) {
            Vector3 transformedVertex = transformMatrix.multiply(v);
            transformed.add(transformedVertex);
        }
        return transformed;
    }

    // Schritt 2 (View-Matrix): Weltkoordinaten relativ zur Kamera ausrichten (invertierte Kamera-Transformation)
    private static List<Vector3> applyCameraTransform(List<Vector3> transformed, Camera camera) {
        Matrix4x4 viewMatrix = Matrix4x4.getTransformationMatrix(camera.getInvertedTransform());

        List<Vector3> cameraTransformed = new ArrayList<>();
        for (Vector3 v : transformed) {
            Vector3 transformedVertex = viewMatrix.multiply(v);
            cameraTransformed.add(transformedVertex);
        }
        return cameraTransformed;
    }

    // Schritt 3 (Projektions-Matrix): Kamerakoordinaten anhand des Sichtfelds (FOV) skalieren, bevor sie auf 2D projiziert werden
    private static List<Vector3> applyFov(List<Vector3> cameraTransformed, double fov) {
        Matrix4x4 projectionMatrix = Matrix4x4.getProjectionMatrix(fov);

        List<Vector3> fovApplied = new ArrayList<>();
        for (Vector3 v : cameraTransformed) {
            Vector3 projectedVertex = projectionMatrix.multiply(v);
            fovApplied.add(projectedVertex);
        }
        return fovApplied;
    }

    // Fasst Schritt 2 und 3 zusammen (View- und Projektions-Transformation)
    public static List<Vector3> applyCameraParams(List<Vector3> transformed, Camera camera) {
        List<Vector3> cameraTransformed = RenderPipeline.applyCameraTransform(transformed, camera);
        return RenderPipeline.applyFov(cameraTransformed, camera.getFov());
    }
}
