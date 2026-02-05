package math;

public class Matrix4x4 {
    public double[][] matrix = new double[4][4];

    public static Matrix4x4 getMatrix() {
        Matrix4x4 translationMatrix = new Matrix4x4();
        translationMatrix.matrix[0][0] = 1;
        translationMatrix.matrix[1][1] = 1;
        translationMatrix.matrix[2][2] = 1;
        translationMatrix.matrix[3][3] = 1;
        return translationMatrix;
    }

    // ===== Matrix Generators =====
    public static Matrix4x4 getTranslationMatrix(double tx, double ty, double tz) {
        /* Translation matrix:
        * 1 0 0 Tx
        * 0 1 0 Ty
        * 0 0 1 Tz
        * 0 0 0 1
        */
        Matrix4x4 translationMatrix = getMatrix();
        translationMatrix.setValue(0, 3, tx);
        translationMatrix.setValue(1, 3, ty);
        translationMatrix.setValue(2, 3, tz);
        return translationMatrix;
    }

    public static Matrix4x4 getRotationMatrix(double rotationX, double rotationY, double rotationZ) {
        /* Rotation matrix X:
         * 1 0    0     0
         * 0 cos  -sin  0
         * 0 sin  cos   0
         * 0 0    0     1
         */
        Matrix4x4 rotX = getMatrix();
        rotX.setValue(1, 1, Math.cos(rotationX));
        rotX.setValue(1, 2, -Math.sin(rotationX));
        rotX.setValue(2, 1, Math.sin(rotationX));
        rotX.setValue(2, 2, Math.cos(rotationX));

        /* Rotation matrix Y:
         * cos 0 sin 0
         * 0   1 0   0
         * -sin0 cos 0
         * 0   0 0   1
         */
        Matrix4x4 rotY = getMatrix();
        rotY.setValue(0, 0, Math.cos(rotationY));
        rotY.setValue(0, 2, Math.sin(rotationY));
        rotY.setValue(2, 0, -Math.sin(rotationY));
        rotY.setValue(2, 2, Math.cos(rotationY));

        /* Rotation matrix Z:
         * cos -sin 0 0
         * sin cos  0 0
         * 0   0    1 0
         * 0   0    0 1
         */
        Matrix4x4 rotZ = getMatrix();
        rotZ.setValue(0, 0, Math.cos(rotationZ));
        rotZ.setValue(0, 1, -Math.sin(rotationZ));
        rotZ.setValue(1, 0, Math.sin(rotationZ));
        rotZ.setValue(1, 1, Math.cos(rotationZ));

        // Kombiniere die Rotationen: Rz * Ry * Rx (achten auf die Reihenfolge)
        return rotZ.multiply(rotY).multiply(rotX);
    }

    public static Matrix4x4 getScalingMatrix(double sx, double sy, double sz) {
        /* Scaling matrix:
         * Sx 0  0  0
         * 0  Sy 0  0
         * 0  0  Sz 0
         * 0  0  0  1
         */
        Matrix4x4 scalingMatrix = getMatrix();
        scalingMatrix.setValue(0, 0, sx);
        scalingMatrix.setValue(1, 1, sy);
        scalingMatrix.setValue(2, 2, sz);
        return scalingMatrix;
    }


    // ===== Matrix Operations =====
    public Matrix4x4 multiply(Matrix4x4 m) {
        Matrix4x4 result = new Matrix4x4();
        for (int row = 0; row < 4; row++) {
            for (int col = 0; col < 4; col++) {
                result.matrix[row][col] = 0;
                for (int k = 0; k < 4; k++) {
                    result.matrix[row][col] += matrix[row][k] * m.matrix[k][col];
                }
            }
        }
        return result;
    }

    public Vektor3 multiply(Vektor3 v) {
        double x = v.x * matrix[0][0] + v.y * matrix[0][1] + v.z * matrix[0][2] + matrix[0][3];
        double y = v.x * matrix[1][0] + v.y * matrix[1][1] + v.z * matrix[1][2] + matrix[1][3];
        double z = v.x * matrix[2][0] + v.y * matrix[2][1] + v.z * matrix[2][2] + matrix[2][3];
        double w = v.x * matrix[3][0] + v.y * matrix[3][1] + v.z * matrix[3][2] + matrix[3][3];

        if (w != 0 && w != 1) { // Normalisierung, falls nötig
            return new Vektor3(x / w, y / w, z / w);
        }
        return new Vektor3(x, y, z);
    }

    // ==== Utility Methods =====
    public void setValue(int row, int col, double value) {
        matrix[row][col] = value;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 4; i++) {
            sb.append("| ");
            for (int j = 0; j < 4; j++) {
                sb.append(String.format("%8.3f ", matrix[i][j]));
            }
            sb.append("|\n");
        }
        return sb.toString();
    }
}
