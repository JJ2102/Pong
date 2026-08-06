package math;

public class Vector3 {
    public double x; // X-Koordinate
    public double y; // Y-Koordinate
    public double z; // Z-Koordinate

    // Konstruktor zur Initialisierung des 3D-Vektors
    public Vector3(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    // ===== Vektor-Operationen =====
    // Invertiert die Richtung des Vektors (negiert alle Komponenten)
    public Vector3 invert() {
        return new Vector3(-x, -y, -z);
    }

    // Teilt alle Komponenten des Vektors durch einen Skalar
    public Vector3 divide(double divisor) {
        return new Vector3(x / divisor, y / divisor, z / divisor);
    }

    // Addiert einen anderen 3D-Vektor zu diesem Vektor und gibt das Ergebnis zurück
    public Vector3 add(Vector3 other) {
        return new Vector3(x + other.x, y + other.y, z + other.z);
    }

    // Erzeugt den Standard-Up-Vektor (0, 1, 0)
    public static Vector3 up() {
        return new Vector3(0, 1, 0);
    }

    // Führt eine lineare Interpolation (Leap) zwischen diesem Vektor und einem Zielvektor durch
    public Vector3 leap(Vector3 other, double percent) {
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

