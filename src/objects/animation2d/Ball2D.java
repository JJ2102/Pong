package objects.animation2d;

import enums.Direction;
import math.Vector2;
import utility.Globals;

import java.awt.*;
import java.awt.geom.Ellipse2D;

public class Ball2D extends Object2D {
    public Ball2D(Dimension windowSize) {
        super(windowSize, 30, 30);
        velocity = new Vector2(Globals.randomSpeed(3, 5), Globals.randomSpeed(3, 5));
    }

    public void switchXDirection() {
        velocity.x = velocity.x * -1;
    }

    public void switchYDirection() {
        velocity.y = velocity.y * -1;
    }

    public void move() {
        setPosition(getPosition().add(velocity));
    }

    // Prüft, ob der Ball die Paddles (links/rechts) oder den oberen/unteren Rand erreicht hat;
    // die Offsets (+20/-30) sorgen dafür, dass er bereits kurz vor den Paddles abprallt
    public Direction isOut(Dimension windowSize) {
        boolean outLeft   = getPosition().x <= width + 20;
        boolean outRight  = getPosition().x >= windowSize.width - width - 30;
        boolean outTop    = getPosition().y <= height;
        boolean outBottom = getPosition().y >= windowSize.height - height;

        if (outLeft || outRight) {
            return Direction.X;
        } else if (outTop || outBottom) {
            return Direction.Y;
        } else {
            return Direction.NONE;
        }
    }

    public void paintMe(Graphics2D g2d) {
        int posX = (int) (getPosition().getX() - (double) width / 2);
        int posY = (int) (getPosition().getY() - (double) height / 2);

        Ellipse2D.Double ball = new Ellipse2D.Double(posX, posY, width*2, height*2);

        g2d.setColor(Color.WHITE);
        g2d.draw(ball);
        g2d.fill(ball);
    }
}
