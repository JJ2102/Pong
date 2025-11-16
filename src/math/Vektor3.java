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

    public static Vektor3 up() {
        return new Vektor3(0, 1, 0);
    }

    @Override
    public String toString() {
        return "( " + x + ", " + y + ", " + z + " )";
    }
}
