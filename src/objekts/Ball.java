package objekts;

import hitboxes.BoxHitbox;
import math.Vektor3;
import meshes.EllipseMesh;
import rendering.Mesh;
import utility.Globals;

import java.awt.*;

public class Ball extends Entity {
    double speedX, speedY, speedZ;
    Vektor3 speed;

    private final double radius = 0.2;

    public Ball() {
        super(Color.YELLOW, Color.ORANGE);
        Mesh boxMesh = new EllipseMesh(radius, 10, 10);
        this.setMesh(boxMesh);

        double size = radius * 1.5;
        hitbox = new BoxHitbox(transform.position, new Vektor3(size, size, size));

        // Speed Setzen
        setRandomSpeed();
    }

    private double randomSpeed() {
        return Globals.randomSpeed(0.03, 0.05);
    }

    private void setRandomSpeed() {
        speed = new Vektor3(randomSpeed(), randomSpeed(), randomSpeed());
    }

    public void reset() {
        this.transform.position = new Vektor3(0,0,0);
        setRandomSpeed();
    }

    public boolean paddleHit(BoxHitbox[] paddles) {
        for (BoxHitbox paddle : paddles) {
            if (hitbox.intersects(paddle)) {
                speed.x += Math.signum(speed.x) * 0.001;
                speed.y += Math.signum(speed.y) * 0.001;
                speed.z += Math.signum(speed.z) * 0.001;
                speed.z = -speed.z;

                return true;
            }
        }
        return false;
    }

    public void move() {
        // Position basierend auf der Geschwindigkeit aktualisieren
        this.transform.position = this.transform.position.add(speed);

        // Einfache Kollisionserkennung mit den Wänden
        if (this.transform.position.x > 2 - radius || this.transform.position.x < -2 + radius) {
            speed.x = -speed.x;
        }
        if (this.transform.position.y > 1 - radius || this.transform.position.y < -1 + radius) {
            speed.y = -speed.y;
        }

        hitbox.setPosition(transform.position);
    }

    // Getter und Setter
    public Vektor3 getSpeed() {
        return new Vektor3(speedX, speedY, speedZ);
    }

    // Debugging
    public String toString() {
        return "[Ball] Pos: " + transform.position + " Speed: " + getSpeed();
    }
}
