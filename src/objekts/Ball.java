package objekts;

import hitboxes.BoxHitbox;
import math.Vektor3;
import meshes.EllipseMesh;
import rendering.Mesh;

import java.awt.*;

public class Ball extends Entity {
    double speedX, speedY, speedZ;
    double radius = 0.2;

    public Ball() {
        super(Color.GREEN);
        Mesh boxMesh = new EllipseMesh(radius, 10, 10);
        this.setMesh(boxMesh);

        hitbox = new BoxHitbox(transform.position, new Vektor3(radius*2, radius*2, radius*2));

        // Speed Setzen
        speedX = randomSpeed();
        speedY = randomSpeed();
        speedZ = randomSpeed();
        System.out.println("Ball speed: " + speedX + ", " + speedY + ", " + speedZ);
    }

    private double randomSpeed() {
        double min = 0.03;
        double max = 0.05;

        // zufällige Richtung: -1 oder +1
        double sign = Math.random() < 0.5 ? -1 : 1;

        // zufällige Geschwindigkeit im Bereich [min, max]
        double magnitude = min + Math.random() * (max - min);

        return sign * magnitude;
    }

    public void reset() {
        this.transform.position = new math.Vektor3(0,0,0);
        speedX = randomSpeed();
        speedY = randomSpeed();
        speedZ = randomSpeed();
    }

    public void move() {
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
        if (this.transform.position.z > 3 - radius || this.transform.position.z < -3 + radius) {
            speedZ = -speedZ;
        }

        hitbox.setPosition(transform.position);
    }


}
