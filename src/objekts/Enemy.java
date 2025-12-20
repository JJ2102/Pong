package objekts;

import math.Vektor3;

import java.awt.*;

public class Enemy extends Panel{
    public Enemy(Vektor3 position) {
        super(position, new Color(255, 0, 0, 100));
    }

    public void move(Vektor3 ballPos) {
        transform.position = new Vektor3(ballPos.x, ballPos.y, transform.position.z);
    }
}
