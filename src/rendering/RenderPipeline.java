package rendering;

import math.Matrix4x4;
import math.Vektor2;
import math.Vektor3;

public class RenderPipeline {
    public static Matrix4x4 applyTransform(Transform transform) {
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
