package objects;

import hitboxes.BoxHitbox;
import math.Vector3;
import meshes.EllipseMesh;
import rendering.Mesh;
import utility.Globals;

import java.awt.*;

public class Ball extends Entity {
    private Vector3 velocity;
    private final double radius = 0.2;

    public Ball() {
        super(Color.YELLOW, Color.ORANGE);
        Mesh boxMesh = new EllipseMesh(radius, 10, 10);
        this.setMesh(boxMesh);

        double size = radius * 1.5;
        setHitbox(new BoxHitbox(getTransform().getPosition(), new Vector3(size, size, size)));

        // Zufällige Anfangsgeschwindigkeit setzen
        setRandomSpeed();
    }

    private double randomSpeed() {
        return Globals.randomSpeed(0.03, 0.05);
    }

    private void setRandomSpeed() {
        velocity = new Vector3(randomSpeed(), randomSpeed(), randomSpeed());
    }

    public void reset() {
        this.getTransform().setPosition(new Vector3(0, 0, 0));
        setRandomSpeed();
    }

    public boolean paddleHit(BoxHitbox[] paddles) {
        for (BoxHitbox paddle : paddles) {
            if (getHitbox().intersects(paddle)) {
                // jeden hit plus 0.001 speed
                velocity.x += Math.signum(velocity.x) * 0.001;
                velocity.y += Math.signum(velocity.y) * 0.001;
                velocity.z += Math.signum(velocity.z) * 0.001;
                velocity.z = -velocity.z; // Richtung ändern

                System.out.println(velocity);

                return true;
            }
        }
        return false;
    }

    public void move() {
        // Position basierend auf der Geschwindigkeit aktualisieren
        this.getTransform().setPosition(this.getTransform().getPosition().add(velocity));

        // Einfache Kollisionserkennung mit den Wänden
        if (this.getTransform().getPosition().x > 2 - radius || this.getTransform().getPosition().x < -2 + radius) {
            velocity.x = -velocity.x;
        }
        if (this.getTransform().getPosition().y > 1 - radius || this.getTransform().getPosition().y < -1 + radius) {
            velocity.y = -velocity.y;
        }

        getHitbox().setPosition(getTransform().getPosition());
    }

    // Debugging
    public String toString() {
        return "[Ball] Pos: " + getTransform().getPosition() + " Speed: " + velocity;
    }
}
