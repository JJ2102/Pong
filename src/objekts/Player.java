package objekts;

import hitboxes.BoxHitbox;
import math.Vektor3;

import java.awt.*;

public class Player extends Panel {
    public Player(Vektor3 position) {
        super(position, new Color(0, 0, 255, 100), new Color(0, 255, 255));
    }

    public void moveTo(Vektor3 position) {
        transform.position = position;
        hitbox.setPosition(transform.position);
    }
}
