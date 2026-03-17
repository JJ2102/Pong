package rendering;

import math.Matrix4x4;
import math.Vektor3;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RenderPipeline {
    public static List<Vektor3> applyTransform(Transform transform, List<Vektor3> vertices) {
        List<Vektor3> output = new ArrayList<>();

        Matrix4x4 transformMatrix = getTransformMatrix(transform);

        for (Vektor3 v : vertices) {
            Vektor3 transformedVertex = transformMatrix.multiply(v);
            output.add(transformedVertex);
        }

        return output;
    }

    private static Matrix4x4 getTransformMatrix(Transform transform) {
        Vektor3 position = transform.position;
        Vektor3 rotation = transform.rotation;
        Vektor3 scale = transform.scale;

        Matrix4x4 translationMatrix = Matrix4x4.getTranslationMatrix(
                position.x,
                position.y,
                position.z
        );


        Matrix4x4 rotationMatrix = Matrix4x4.getRotationMatrix(
                rotation.x,
                rotation.y,
                rotation.z
        );

        Matrix4x4 scaleMatrix = Matrix4x4.getScalingMatrix(
                scale.x,
                scale.y,
                scale.z
        );

        // Model-Matrix: Alle Objekt-Transformationen (Translation * Rotation * Scale)
        return translationMatrix.multiply(rotationMatrix).multiply(scaleMatrix);
    }
}
