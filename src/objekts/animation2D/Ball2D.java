package objekts.animation2D;

import math.Vektor2;
import utility.Globals;

import java.awt.*;
import java.awt.geom.Ellipse2D;

public class Ball2D extends Objekt2D {
    public Ball2D(Dimension windowSize) {
        super(windowSize, 20, 20);
        vel = new Vektor2(Globals.randomSpeed(3, 5), Globals.randomSpeed(3, 5));
    }

    public void switchXDirection() {
        vel.x = vel.x * -1;
    }

    public void switchYDirection() {
        vel.y = vel.y * -1;
    }

    public void paintMe(Graphics2D g2d) {
        int posX = (int) (pos.getX() - width / 2);
        int  posY = (int) (pos.getY() - height / 2);

        Ellipse2D.Double ball = new Ellipse2D.Double(posX, posY, width*2, height*2);

        g2d.setColor(Color.WHITE);
        g2d.draw(ball);
        g2d.fill(ball);
    }
}
