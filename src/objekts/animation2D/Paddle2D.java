package objekts.animation2D;

import math.Vektor2;

import java.awt.*;

public class Paddle2D extends Objekt2D {
    public Paddle2D(Vektor2 pos, Vektor2 vel) {
        super(pos, 10, 30);
    }

    public void paintMe(Graphics2D g2d) {
        int posX = (int) (pos.getX() - width / 2);
        int  posY = (int) (pos.getY() - height / 2);

        Rectangle r = new Rectangle(posX, posY, width, height);
        g2d.setColor(Color.WHITE);
        g2d.draw(r);
        g2d.fill(r);
    }
}
