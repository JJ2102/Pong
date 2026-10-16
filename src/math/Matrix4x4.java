package math;

import rendering.Transform;

/**
 * Die Matrix4x4-Klasse repräsentiert eine 4x4-Matrix, die in der 3D-Grafik für Transformationen verwendet wird.
 * Sie bietet Methoden zur Erstellung von Transformationsmatrizen (Translation, Rotation, Skalierung) und zur
 * Multiplikation von Matrizen und Vektoren.
 */
public class Matrix4x4 {
    private final double[][] matrix = new double[4][4]; // 2D-Array speichert die 4x4 Matrix-Werte

    /**
     * Gibt eine neue 4x4 Einheitsmatrix zurück.
     * <pre>
     * ⎡ 1  0  0  0 ⎤
     * ⎢ 0  1  0  0 ⎥
     * ⎢ 0  0  1  0 ⎥
     * ⎣ 0  0  0  1 ⎦
     * </pre>
     *
     * @return eine 4x4 Einheitsmatrix
     */
    public static Matrix4x4 getUnitMatrix() {
        Matrix4x4 unitMatrix = new Matrix4x4(); // neue Matrix erstellen
        // Hauptdiagonale auf 1 setzen
        unitMatrix.matrix[0][0] = 1;
        unitMatrix.matrix[1][1] = 1;
        unitMatrix.matrix[2][2] = 1;
        unitMatrix.matrix[3][3] = 1;
        return unitMatrix;
    }

    // ===== Matrix-Generatoren =====
    /**
     * Erstellt eine Transformationsmatrix
     * aus den Positions-, Rotations- und Skalierungswerten eines {@link Transform}-Objekts.
     * <p>
     * Ergibt sich aus der Multiplikation {@code Translation * Rotation * Scale}.
     *
     * @param transform {@link Transform} eines 3D-Objekts
     * @return erstellte Transformationsmatrix
     */
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

    /**
     * Erstellt eine View-Matrix für die Kamera basierend auf ihrer Position im 3D-Raum.
     * <pre>
     * ⎡ 1  0  0  -Cx ⎤
     * ⎢ 0  1  0  -Cy ⎥
     * ⎢ 0  0  1  -Cz ⎥
     * ⎣ 0  0  0   1  ⎦
     * </pre>
     *
     * @param cameraPosition {@link Vector3} Position der Kamera im 3D-Raum
     * @return erstellte View-Matrix für die Kamera
     */
    public static Matrix4x4 getViewMatrix(Vector3 cameraPosition) {
        // Die Kamera kann nur verschoben werden, daher ist die View-Matrix
        // schlicht die Gegenbewegung zu ihrer Position
        return getTranslationMatrix(-cameraPosition.getX(), -cameraPosition.getY(), -cameraPosition.getZ());
    }

    /**
     * Erstellt eine Translationsmatrix für die Verschiebung eines Objekts im 3D-Raum.
     * <pre>
     * ⎡ 1  0  0  Tx ⎤
     * ⎢ 0  1  0  Ty ⎥
     * ⎢ 0  0  1  Tz ⎥
     * ⎣ 0  0  0  1  ⎦
     * </pre>
     *
     * @param tx Verschiebung entlang der X-Achse
     * @param ty Verschiebung entlang der Y-Achse
     * @param tz Verschiebung entlang der Z-Achse
     * @return erstellte Translationsmatrix
     */
    private static Matrix4x4 getTranslationMatrix(double tx, double ty, double tz) {
        Matrix4x4 translationMatrix = getUnitMatrix(); // startet mit der Einheitsmatrix
        // Verschiebung-Werte in die vierte Spalte eintragen
        translationMatrix.setValue(0, 3, tx);
        translationMatrix.setValue(1, 3, ty);
        translationMatrix.setValue(2, 3, tz);
        return translationMatrix;
    }

    /**
     * Erstellt eine Rotationsmatrix für die Rotation eines Objekts im 3D-Raum.
     * Die Rotationen werden in der Reihenfolge Z, Y, X angewendet.
     * <pre>
     * Rotation um X-Achse:
     * ⎡ 1  0      0      0 ⎤
     * ⎢ 0  cosθ  -sinθ   0 ⎥
     * ⎢ 0  sinθ   cosθ   0 ⎥
     * ⎣ 0  0      0      1 ⎦
     *
     * Rotation um Y-Achse:
     * ⎡ cosφ   0   sinφ   0 ⎤
     * ⎢ 0      1    0     0 ⎥
     * ⎢ -sinφ  0   cosφ   0 ⎥
     * ⎣ 0      0    0     1 ⎦
     *
     * Rotation um Z-Achse:
     * ⎡ cosψ -sinψ   0    0 ⎤
     * ⎢ sinψ  cosψ   0    0 ⎥
     * ⎢ 0      0     1    0 ⎥
     * ⎣ 0      0     0    1 ⎦
     * </pre>
     *
     * @param rotationX Rotation um die X-Achse in Radiant
     * @param rotationY Rotation um die Y-Achse in Radiant
     * @param rotationZ Rotation um die Z-Achse in Radiant
     * @return erstellte Rotationsmatrix
     */
    private static Matrix4x4 getRotationMatrix(double rotationX, double rotationY, double rotationZ) {
        Matrix4x4 rotX = getUnitMatrix();
        rotX.setValue(1, 1, Math.cos(rotationX));
        rotX.setValue(1, 2, -Math.sin(rotationX));
        rotX.setValue(2, 1, Math.sin(rotationX));
        rotX.setValue(2, 2, Math.cos(rotationX));

        Matrix4x4 rotY = getUnitMatrix();
        rotY.setValue(0, 0, Math.cos(rotationY));
        rotY.setValue(0, 2, Math.sin(rotationY));
        rotY.setValue(2, 0, -Math.sin(rotationY));
        rotY.setValue(2, 2, Math.cos(rotationY));

        Matrix4x4 rotZ = getUnitMatrix();
        rotZ.setValue(0, 0, Math.cos(rotationZ));
        rotZ.setValue(0, 1, -Math.sin(rotationZ));
        rotZ.setValue(1, 0, Math.sin(rotationZ));
        rotZ.setValue(1, 1, Math.cos(rotationZ));

        // Multiplikation der Rotationsmatrizen in der Reihenfolge Z, Y, X
        return rotZ.multiply(rotY).multiply(rotX);
    }

    /**
     * Erstellt eine Skalierungsmatrix für die Skalierung eines Objekts im 3D-Raum.
     * <pre>
     * ⎡ Sx  0   0   0 ⎤
     * ⎢ 0   Sy  0   0 ⎥
     * ⎢ 0   0   Sz  0 ⎥
     * ⎣ 0   0   0   1 ⎦
     * </pre>
     *
     * @param sx Skalierungsfaktor entlang der X-Achse
     * @param sy Skalierungsfaktor entlang der Y-Achse
     * @param sz Skalierungsfaktor entlang der Z-Achse
     * @return erstellte Skalierungsmatrix
     */
    private static Matrix4x4 getScalingMatrix(double sx, double sy, double sz) {
        Matrix4x4 scalingMatrix = getUnitMatrix(); // startet mit der Einheitsmatrix
        // Skalierungsfaktoren auf der Hauptdiagonale setzen
        scalingMatrix.setValue(0, 0, sx);
        scalingMatrix.setValue(1, 1, sy);
        scalingMatrix.setValue(2, 2, sz);
        return scalingMatrix;
    }

    /**
     * Erstellt eine Projektionsmatrix für die 3D-Projektion.
     * <pre>
     * ⎡ zoom  0   0  0 ⎤
     * ⎢  0  zoom  0  0 ⎥
     * ⎢  0   0   1  0 ⎥
     * ⎣  0   0   1  0 ⎦
     * </pre>
     *
     * @param zoomFaktor der die Breite des Sichtfeldes bestimmt
     * @return erstellte Projektionsmatrix
     */
    public static Matrix4x4
    getProjectionMatrix(double zoomFaktor) {
        Matrix4x4 p = new Matrix4x4();

        // FOV-Werte setzen
        p.setValue(0, 0, zoomFaktor);
        p.setValue(1, 1, zoomFaktor);

        // Z- und W-Werte für Perspektive setzen
        p.setValue(2, 2, 1);
        p.setValue(3, 2, 1);

        return p;
    }

    // ===== Matrix-Operationen =====
    /**
     * Multipliziert diese Matrix mit einer anderen Matrix.
     *
     * @param matrix die rechte Matrix des Produkts
     * @return eine neue Matrix mit dem Ergebnis. {@code this} und {@code matrix} bleiben unverändert
     */
    public Matrix4x4 multiply(Matrix4x4 matrix) {
        Matrix4x4 result = new Matrix4x4();
        // Jeder Eintrag ist das Skalarprodukt aus Zeile row von this und Spalte col von matrix
        for (int row = 0; row < 4; row++) {
            for (int col = 0; col < 4; col++) {
                result.matrix[row][col] = 0;
                for (int k = 0; k < 4; k++) {
                    result.matrix[row][col] += this.matrix[row][k] * matrix.matrix[k][col];
                }
            }
        }
        return result;
    }

    /**
     * Multipliziert die aktuelle Matrix mit einem Vektor.
     * <p>
     * Der Vektor wird als Punkt mit {@code w = 1} behandelt. Ergibt sich ein {@code w} ungleich 0 und 1,
     * wird das Ergebnis durch {@code w} geteilt (perspektivische Division).
     *
     * @param vector {@link Vector3}, der transformiert wird
     * @return neuer, mit der Matrix multiplizierter Vektor
     */
    public Vector3 multiply(Vector3 vector) {
        // neue Komponenten inklusive homogener Koordinaten berechnen
        double x = vector.getX() * matrix[0][0] + vector.getY() * matrix[0][1] + vector.getZ() * matrix[0][2] + matrix[0][3];
        double y = vector.getX() * matrix[1][0] + vector.getY() * matrix[1][1] + vector.getZ() * matrix[1][2] + matrix[1][3];
        double z = vector.getX() * matrix[2][0] + vector.getY() * matrix[2][1] + vector.getZ() * matrix[2][2] + matrix[2][3];
        double w = vector.getX() * matrix[3][0] + vector.getY() * matrix[3][1] + vector.getZ() * matrix[3][2] + matrix[3][3];

        // Normalisierung, falls nötig
        if (w != 0 && w != 1) {
            return new Vector3(x / w, y / w, z / w);
        }
        return new Vector3(x, y, z);
    }

    // ===== Hilfsmethoden =====
    /**
     * Setzt den Wert an der angegebenen Position in der Matrix.
     *
     * @param row Zeilenindex (0-3)
     * @param col Spaltenindex (0-3)
     * @param value Wert, der gesetzt werden soll
     */
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

