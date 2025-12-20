package objekts;

import meshes.Ellipse;
import rendering.Mesh;

import java.awt.*;

public class Ball extends Entity {
    double speedX = 0.05; // 0.02
    double speedY = 0.05; // 0.015
    double speedZ = 0.05; // 0.01

    double radius = 0.2;

    public Ball() {
        super(Color.GREEN);
        Mesh boxMesh = new Ellipse(radius, 10, 10);
        this.setMesh(boxMesh);
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
    }


}
