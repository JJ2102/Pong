package rendering;

import math.Matrix4x4;
import math.Vektor3;

import java.util.ArrayList;
import java.util.List;

public class RenderPipeline {
    public static List<Vektor3> applyTransform(Transform transform, List<Vektor3> vertices) {
        List<Vektor3> output = new ArrayList<>();

        Matrix4x4 transformMatrix = Matrix4x4.getTransformationMatrix(transform);

        for (Vektor3 v : vertices) {
            Vektor3 transformedVertex = transformMatrix.multiply(v);
            output.add(transformedVertex);
        }

        return output;
    }
}
