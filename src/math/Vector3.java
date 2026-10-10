package math;

/**
 * Stellt einen Vektor oder Punkt im dreidimensionalen Raum dar.
 */
public class Vector3 {
    private double x; // X-Koordinate
    private double y; // Y-Koordinate
    private double z; // Z-Koordinate

    /**
     * Konstruktor zur Initialisierung des 3D-Vektors.
     *
     * @param x X-Koordinate
     * @param y Y-Koordinate
     * @param z Z-Koordinate
     */
    public Vector3(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    // ===== Getter und Setter =====
    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getZ() {
        return z;
    }

    public void setZ(double z) {
        this.z = z;
    }

    // ===== Vektor-Operationen =====

    /**
     * Teilt alle Komponenten dieses Vektors durch einen Skalar und gibt das Ergebnis als neuen Vektor zurück.
     *
     * @param divisor skalarer Wert, durch den die Komponenten geteilt werden
     * @return ein neuer Vektor mit den geteilten Komponenten
     * @throws IllegalArgumentException wenn der Divisor 0 ist
     */
    public Vector3 divide(double divisor) {
        if  (divisor == 0.0) {
            throw new IllegalArgumentException("Division durch Null ist nicht erlaubt.");
        }
        return new Vector3(x / divisor, y / divisor, z / divisor);
    }

    /**
     * Addiert einen anderen 3D-Vektor zu diesem Vektor und gibt das Ergebnis zurück.
     *
     * @param other anderer 3D-Vektor
     * @return neuer Vektor mit der Summe der Komponenten
     */
    public Vector3 add(Vector3 other) {
        return new Vector3(x + other.x, y + other.y, z + other.z);
    }

    /**
     * Führt eine lineare Interpolation (Lerp) zwischen diesem Vektor und einem Zielvektor durch.
     *
     * @param other Zielvektor
     * @param percent Interpolationsfaktor
     * @return neuer Vektor mit dem interpolierten Wert
     */
    public Vector3 lerp(Vector3 other, double percent) {
        double newX = this.x + (other.x - this.x) * percent;
        double newY = this.y + (other.y - this.y) * percent;
        double newZ = this.z + (other.z - this.z) * percent;
        return new Vector3(newX, newY, newZ);
    }

    // ===== Hilfsmethoden =====
    @Override
    public String toString() {
        return "( " + x + ", " + y + ", " + z + " )";
    }
}
