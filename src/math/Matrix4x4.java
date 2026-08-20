package math;

import rendering.Transform;

// 4x4-Matrix für die Transformationen der 3D-Renderpipeline
public class Matrix4x4 {
    private final double[][] matrix = new double[4][4]; // 2D-Array speichert die 4x4 Matrix-Werte

    // Gibt die Einheitsmatrix zurück
    public static Matrix4x4 getUnitMatrix() {
        /*
         * 1 0 0 0
         * 0 1 0 0
         * 0 0 1 0
         * 0 0 0 1
         */
        Matrix4x4 unitMatrix = new Matrix4x4(); // neue Matrix erstellen
        // Hauptdiagonale auf 1 setzen
        unitMatrix.matrix[0][0] = 1;
        unitMatrix.matrix[1][1] = 1;
        unitMatrix.matrix[2][2] = 1;
        unitMatrix.matrix[3][3] = 1;
        return unitMatrix;
    }

    // ===== Matrix-Generatoren =====
    // Erstellt eine kombinierte Transformationsmatrix (Translation * Rotation * Scale)
    public static Matrix4x4 getTransformationMatrix(Transform transform) {
        // Position, Rotation und Skalierung aus dem Transform-Objekt holen
        Vector3 position = transform.getPosition();
        Vector3 rotation = transform.getRotation();
        Vector3 scale = transform.getScale();

        // Einzelne Transformationsmatrizen erstellen
        Matrix4x4 translationMatrix = getTranslationMatrix(position.getX(), position.getY(), position.getZ());
        Matrix4x4 rotationMatrix = Matrix4x4.getRotationMatrix(rotation.getX(), rotation.getY(), rotation.getZ());
        Matrix4x4 scaleMatrix = Matrix4x4.getScalingMatrix(scale.getX(), scale.getY(), scale.getZ());

        // Model-Matrix: Alle Objekt-Transformationen (Translation * Rotation * Scale)
        return translationMatrix.multiply(rotationMatrix).multiply(scaleMatrix);
    }

    // Erstellt die View-Matrix der Kamera (invertierte Rotation * invertierte Translation)
    public static Matrix4x4 getViewMatrix(Transform transform) {
        Vector3 position = transform.getPosition();
        Vector3 rotation = transform.getRotation();

        // Rotationsmatrizen sind orthogonal, daher ist die Transponierte gleich der Inversen
        Matrix4x4 inverseRotation = getRotationMatrix(rotation.getX(), rotation.getY(), rotation.getZ()).transpose();
        // Gegenbewegung zur Kameraposition
        Matrix4x4 inverseTranslation = getTranslationMatrix(-position.getX(), -position.getY(), -position.getZ());

        // Reihenfolge ist entscheidend: erst gegen die Position verschieben, dann gegen die Rotation drehen
        return inverseRotation.multiply(inverseTranslation);
    }

    // Erstellt eine Translations-matrix für die Verschiebung
    private static Matrix4x4 getTranslationMatrix(double tx, double ty, double tz) {
        // Quelle 1
        /* Translation matrix:
        * 1 0 0 Tx
        * 0 1 0 Ty
        * 0 0 1 Tz
        * 0 0 0 1
        */
        Matrix4x4 translationMatrix = getUnitMatrix(); // startet mit der Einheitsmatrix
        // Verschiebung-Werte in die vierte Spalte eintragen
        translationMatrix.setValue(0, 3, tx);
        translationMatrix.setValue(1, 3, ty);
        translationMatrix.setValue(2, 3, tz);
        return translationMatrix;
    }

    // Erstellt eine Rotationsmatrix aus den Rotationswinkeln (X, Y, Z)
    private static Matrix4x4 getRotationMatrix(double rotationX, double rotationY, double rotationZ) {
        // Quelle 1
        /* Rotation matrix X:
         * 1 0    0     0
         * 0 cos  -sin  0
         * 0 sin  cos   0
         * 0 0    0     1
         */
        Matrix4x4 rotX = getUnitMatrix();
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
        Matrix4x4 rotY = getUnitMatrix();
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
        Matrix4x4 rotZ = getUnitMatrix();
        rotZ.setValue(0, 0, Math.cos(rotationZ));
        rotZ.setValue(0, 1, -Math.sin(rotationZ));
        rotZ.setValue(1, 0, Math.sin(rotationZ));
        rotZ.setValue(1, 1, Math.cos(rotationZ));

        // Kombiniere die Rotationen: Rz * Ry * Rx (achten auf die Reihenfolge)
        return rotZ.multiply(rotY).multiply(rotX);
    }

    // Erstellt eine Skalierungsmatrix
    private static Matrix4x4 getScalingMatrix(double sx, double sy, double sz) {
        // Quelle 1
        /* Scaling matrix:
         * Sx 0  0  0
         * 0  Sy 0  0
         * 0  0  Sz 0
         * 0  0  0  1
         */
        Matrix4x4 scalingMatrix = getUnitMatrix(); // startet mit der Einheitsmatrix
        // Skalierungsfaktoren auf der Hauptdiagonale setzen
        scalingMatrix.setValue(0, 0, sx);
        scalingMatrix.setValue(1, 1, sy);
        scalingMatrix.setValue(2, 2, sz);
        return scalingMatrix;
    }

    // Erstellt eine Projektionsmatrix für die 3D-Projektion
    public static Matrix4x4 getProjectionMatrix(double fov) {
        Matrix4x4 p = new Matrix4x4();

        // FOV-Werte setzen
        p.setValue(0, 0, fov);
        p.setValue(1, 1, fov);

        // Z- und W-Werte für Perspektive setzen
        p.setValue(2, 2, 1);
        p.setValue(3, 2, 1);

        return p;
    }

    // ===== Matrix-Operationen =====
    // Multipliziert zwei 4x4 Matrizen
    public Matrix4x4 multiply(Matrix4x4 m) {
        // Quelle 3
        Matrix4x4 result = new Matrix4x4(); // Ergebnismatrix erstellen
        // Zeilen und Spalten durchgehen und Matrixprodukt berechnen
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

    // Transponiert die Matrix (vertauscht Zeilen und Spalten)
    public Matrix4x4 transpose() {
        Matrix4x4 result = new Matrix4x4();
        for (int row = 0; row < 4; row++) {
            for (int col = 0; col < 4; col++) {
                result.matrix[col][row] = matrix[row][col];
            }
        }
        return result;
    }

    // Multipliziert die Matrix mit einem 3D-Vektor
    public Vector3 multiply(Vector3 v) {
        // Quelle 2
        // neue Komponenten inklusive homogener Koordinaten berechnen
        double x = v.getX() * matrix[0][0] + v.getY() * matrix[0][1] + v.getZ() * matrix[0][2] + matrix[0][3];
        double y = v.getX() * matrix[1][0] + v.getY() * matrix[1][1] + v.getZ() * matrix[1][2] + matrix[1][3];
        double z = v.getX() * matrix[2][0] + v.getY() * matrix[2][1] + v.getZ() * matrix[2][2] + matrix[2][3];
        double w = v.getX() * matrix[3][0] + v.getY() * matrix[3][1] + v.getZ() * matrix[3][2] + matrix[3][3];

        // Normalisierung, falls nötig
        if (w != 0 && w != 1) {
            return new Vector3(x / w, y / w, z / w);
        }
        return new Vector3(x, y, z);
    }

    // ===== Hilfsmethoden =====
    // Setzt einen Wert an einer bestimmten Position in der Matrix
    public void setValue(int row, int col, double value) {
        matrix[row][col] = value;
    }

    // Gibt die Matrix als formatierte Zeichenkette für die Konsole zurück
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

