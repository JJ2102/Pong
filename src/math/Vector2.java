package math;

// Vektor im zweidimensionalen Raum
public class Vector2 {
    private double x; // X-Koordinate
    private double y; // Y-Koordinate

    // Konstruktor zur Initialisierung des 2D-Vektors
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
    // Addiert einen anderen 2D-Vektor zu diesem Vektor und gibt das Ergebnis zurück
    public Vector2 add(Vector2 other) {
        return new Vector2(x + other.x, y + other.y);
    }

    // ===== Hilfsmethoden =====
    // Gibt den Vektor als formatierte Zeichenkette zurück
    @Override
    public String toString() {
        return "( " + x + ", " + y + " )";
    }
}
