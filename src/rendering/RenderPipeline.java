package rendering;

import math.Matrix4x4;
import math.Vektor3;

import java.util.ArrayList;
import java.util.List;

public class RenderPipeline {
    public static List<Vektor3> applyTransform(List<Vektor3> vertices, Transform transform) {
        Matrix4x4 transformMatrix = Matrix4x4.getTransformationMatrix(transform);

        List<Vektor3> transformed = new ArrayList<>();
        for (Vektor3 v : vertices) {
            Vektor3 transformedVertex = transformMatrix.multiply(v);
            transformed.add(transformedVertex);
        }
        return transformed;
    }

    public static List<Vektor3> applyCameraTransform(List<Vektor3> vektors, Camera camera) {
        Matrix4x4 viewMatrix = Matrix4x4.getTransformationMatrix(camera.getInvertedTransform());

        List<Vektor3> cameraTransformed = new ArrayList<>();
        for (Vektor3 v : vektors) {
            Vektor3 transformedVertex = viewMatrix.multiply(v);
            cameraTransformed.add(transformedVertex);
        }
        return cameraTransformed;
    }

    public static List<Vektor3> applyFOV(List<Vektor3> vektors, double fov) {
        Matrix4x4 projectionMatrix = Matrix4x4.getProjectionMatrix(fov);

        List<Vektor3> fovApplied = new ArrayList<>();
        for (Vektor3 v : vektors) {
            Vektor3 projectedVertex = projectionMatrix.multiply(v);
            fovApplied.add(projectedVertex);
        }
        return fovApplied;
    }
}
