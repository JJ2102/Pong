package math;

public class Vektor2 {
    public double x;
    public double y;

    public Vektor2(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double getX() {
        return x;
    }
    public double getY() {
        return y;
    }

    public Vektor2 add(Vektor2 v) {
        this.x += v.x;
        this.y += v.y;
        return this;
    }

    public Vektor2 multiply(int faktor) {
        this.x *= faktor;
        this.y *= faktor;
        return this;
    }
}
