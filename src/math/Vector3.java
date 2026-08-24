package math;

// Vektor im dreidimensionalen Raum
public class Vector3 {
    private double x; // X-Koordinate
    private double y; // Y-Koordinate
    private double z; // Z-Koordinate

    // Konstruktor zur Initialisierung des 3D-Vektors
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
    // Teilt alle Komponenten des Vektors durch einen Skalar
    public Vector3 divide(double divisor) {
        return new Vector3(x / divisor, y / divisor, z / divisor);
    }

    // Addiert einen anderen 3D-Vektor zu diesem Vektor und gibt das Ergebnis zurück
    public Vector3 add(Vector3 other) {
        return new Vector3(x + other.x, y + other.y, z + other.z);
    }

    // Führt eine lineare Interpolation (Lerp) zwischen diesem Vektor und einem Zielvektor durch
    public Vector3 lerp(Vector3 other, double percent) {
        double newX = this.x + (other.x - this.x) * percent;
        double newY = this.y + (other.y - this.y) * percent;
        double newZ = this.z + (other.z - this.z) * percent;
        return new Vector3(newX, newY, newZ);
    }

    // ===== Hilfsmethoden =====
    // Gibt den Vektor als formatierte Zeichenkette zurück
    @Override
    public String toString() {
        return "( " + x + ", " + y + ", " + z + " )";
    }
}
