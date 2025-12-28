package objekts;

import math.Vektor3;

import java.awt.*;

public class Enemy extends Panel{
    // Min und Max-Werte für die Bewegung
    private final double MIN_X;
    private final double MAX_X;
    private final double MIN_Y;
    private final double MAX_Y;

    private final double posZ;

    public Enemy(Vektor3 position, Vektor3 boxSize) {
        super(position, new Color(255, 0, 0, 100));

        this.posZ = position.z;

        this.MIN_X = -boxSize.x + xSize;
        this.MAX_X = boxSize.x - xSize;
        this.MIN_Y = -boxSize.y + ySize;
        this.MAX_Y = boxSize.y - ySize;
    }

    private double clamp(double value, double min, double max) {
        return Math.max(min, Math.min(max, value));
    }

    public void move(Vektor3 ballPos, double difficulty) {
        this.transform.position = this.transform.position.lerp(ballPos, difficulty);
        this.transform.position.z = posZ;

        // Begrenze die Position innerhalb der Box
        transform.position.x = clamp(transform.position.x, MIN_X, MAX_X);
        transform.position.y = clamp(transform.position.y, MIN_Y, MAX_Y);

        hitbox.setPosition(transform.position);
    }
}
