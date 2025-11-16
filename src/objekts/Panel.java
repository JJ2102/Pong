package objekts;

import math.Vektor3;

import java.awt.*;
import java.awt.geom.Rectangle2D;

public class Panel {
    Vektor3 position;

    public Panel(Vektor3 position) {
        this.position = position;
    }

    public void paintMe(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;

        Rectangle2D panel = new Rectangle2D.Double(position.x, position.y, 200, 100);

        g2d.draw(panel);
        g2d.fill(panel);
    }
}
