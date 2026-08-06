package objects;

import math.Vector3;

import java.awt.*;

public class Player extends Paddle {
    public Player(Vector3 position) {
        super(position, new Color(0, 0, 255, 100), new Color(0, 255, 255));
    }

    public void moveTo(Vector3 position) {
        getTransform().setPosition(position);
        getHitbox().setPosition(getTransform().getPosition());
    }
}
