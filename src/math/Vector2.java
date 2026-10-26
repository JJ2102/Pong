package math;

/**
 * Stellt einen Vektor oder Punkt im zweidimensionalen Raum dar.
 */
public class Vector2 {
    private double x; // X-Koordinate
    private double y; // Y-Koordinate

    /**
     * Initialisiert den 2D-Vektor.
     *
     * @param x X-Koordinate
     * @param y Y-Koordinate
     */
    public Vector2(double x, double y) {
        this.x = x;
        this.y = y;
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

    // ===== Vektor-Operationen =====

    /**
     * Addiert einen anderen 2D-Vektor zu diesem Vektor und gibt das Ergebnis zurück.
     *
     * @param other anderer 2D-Vektor
     * @return neuer Vektor mit der Summe der Komponenten
     */
    public Vector2 add(Vector2 other) {
        return new Vector2(x + other.x, y + other.y);
    }

    // ===== Hilfsmethoden =====

    @Override
    public String toString() {
        return "( " + x + ", " + y + " )";
    }
}
