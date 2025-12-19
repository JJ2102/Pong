package objekts;

import math.Vektor3;

import java.awt.*;
import java.awt.geom.Rectangle2D;

public class Panel {
    Vektor3 position;
    int SIZE = 100;

    public Panel(Vektor3 position) {
        this.position = position;
    }

    public void setPosition(Vektor3 position) {
        this.position = position;
    }

    public Vektor3 getPosition() {
        return position;
    }

    public int getSIZE() {
        return SIZE;
    }

    public void paintMe(Graphics2D g) {


        Rectangle2D panel = new Rectangle2D.Double(position.x, position.y, SIZE, SIZE);

        g.setColor(new Color(0, 0, 0, 150));
        g.draw(panel);
        g.fill(panel);
    }
}
