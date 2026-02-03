package math;

public class Vektor3 {
    public double x;
    public double y;
    public double z;

    public Vektor3(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Vektor3 divide(double divisor) {
        return new Vektor3(x / divisor, y / divisor, z / divisor);
    }

    public Vektor3 multiply(Vektor3 other) {
        return new Vektor3(x * other.x, y * other.y, z * other.z);
    }

    public double length() {
        return Math.sqrt(x * x + y * y + z * z); // Pythagoras im 3D-Raum
    }

    public static Vektor3 up() {
        return new Vektor3(0, 1, 0);
    }

    public Vektor3 lerp(Vektor3 other, double percent) {
        double newX = this.x + (other.x - this.x) * percent;
        double newY = this.y + (other.y - this.y) * percent;
        double newZ = this.z + (other.z - this.z) * percent;
        return new Vektor3(newX, newY, newZ);
    }

    @Override
    public String toString() {
        return "( " + x + ", " + y + ", " + z + " )";
    }
}
