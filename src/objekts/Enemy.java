package objekts;

import math.Vektor3;

import java.awt.*;

public class Enemy extends Paddle {
    // Min und Max-Werte für die Bewegung
    private final double MIN_X;
    private final double MAX_X;
    private final double MIN_Y;
    private final double MAX_Y;

    private final double posZ;

    public Enemy(Vektor3 position, Vektor3 boxSize) {
        super(position, Color.RED, new Color(255, 0, 255));

        this.posZ = position.z;

        this.MIN_X = -boxSize.x + xSize;
        this.MAX_X = boxSize.x - xSize;
        this.MIN_Y = -boxSize.y + ySize;
        this.MAX_Y = boxSize.y - ySize;
    }

    private double clamp(double value, double min, double max) {
        return Math.clamp(value, min, max);
    }

    public void move(Vektor3 ballPos, double difficulty) {
        this.transform.setPosition(this.transform.getPosition().lerp(ballPos, difficulty));
        this.transform.getPosition().z = posZ;

        // Begrenze die Position innerhalb der Box
        transform.getPosition().x = clamp(transform.getPosition().x, MIN_X, MAX_X);
        transform.getPosition().y = clamp(transform.getPosition().y, MIN_Y, MAX_Y);

        hitbox.setPosition(transform.getPosition());
    }

    public void reset() {
        transform.setPosition(new Vektor3(0, 0, transform.getPosition().z));
    }
}
