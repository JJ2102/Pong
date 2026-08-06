package math;

public class Vector2 {
    public double x; // X-Koordinate
    public double y; // Y-Koordinate

    // Konstruktor zur Initialisierung des 2D-Vektors
    public Vector2(double x, double y) {
        this.x = x;
        this.y = y;
    }

    // ===== Getter und Setter =====
    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    // ===== Vektor-Operationen =====
    // Addiert einen anderen 2D-Vektor zu diesem Vektor hinzu
    public Vector2 add(Vector2 v) {
        this.x += v.x;
        this.y += v.y;
        return this;
    }
}

