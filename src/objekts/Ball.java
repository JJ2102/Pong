package objekts;

import hitboxes.BoxHitbox;
import math.Vektor3;
import meshes.EllipseMesh;
import rendering.Mesh;
import utility.Globals;

import java.awt.*;

public class Ball extends Entity {
    double speedX, speedY, speedZ;

    private final double radius = 0.2;

    public Ball() {
        super(Color.YELLOW, Color.ORANGE);
        Mesh boxMesh = new EllipseMesh(radius, 10, 10);
        this.setMesh(boxMesh);

        double size = radius * 1.5;
        hitbox = new BoxHitbox(transform.position, new Vektor3(size, size, size));

        // Speed Setzen
        speedX = randomSpeed();
        speedY = randomSpeed();
        speedZ = randomSpeed();
        System.out.println("Ball speed: " + speedX + ", " + speedY + ", " + speedZ);
    }

    private double randomSpeed() {
        return Globals.randomSpeed(0.03, 0.05);
    }

    public void reset() {
        this.transform.position = new math.Vektor3(0,0,0);
        speedX = randomSpeed();
        speedY = randomSpeed();
        speedZ = randomSpeed();
    }

    public boolean paddleHit(BoxHitbox[] paddles) {
        for (BoxHitbox paddle : paddles) {
            if (hitbox.intersects(paddle)) {
                speedX += Math.signum(speedX) * 0.001;
                speedY += Math.signum(speedY) * 0.001;
                speedZ += Math.signum(speedZ) * 0.001;
                speedZ = -speedZ;

                System.out.println("New ball speed: " + speedX + ", " + speedY + ", " + speedZ);

                return true;
            }
        }
        return false;
    }

    public void move() {
        // Position basierend auf der Geschwindigkeit aktualisieren
        this.transform.position.x += speedX;
        this.transform.position.y += speedY;
        this.transform.position.z += speedZ;

        // Einfache Kollisionserkennung mit den Wänden
        if (this.transform.position.x > 2 - radius || this.transform.position.x < -2 + radius) {
            speedX = -speedX;
        }
        if (this.transform.position.y > 1 - radius || this.transform.position.y < -1 + radius) {
            speedY = -speedY;
        }

        hitbox.setPosition(transform.position);
    }

    // Getter und Setter
    public double getRadius() {
        return radius;
    }

    public Vektor3 getSpeed() {
        return new Vektor3(speedX, speedY, speedZ);
    }
}
