package objekts.animation2D;

import enums.Direction;
import math.Vektor2;

import java.awt.*;

public class Objekt2D {
    protected final int width;
    protected final int height;
    protected Vektor2 pos;
    protected Vektor2 vel;

    public Objekt2D(Dimension windowSize, int width, int height) {
        this.width = width;
        this.height = height;
        this.pos = new Vektor2((double) windowSize.width / 2, (double) windowSize.height / 2);
    }

    public Objekt2D(Vektor2 pos, int width, int height) {
        this.width = width;
        this.height = height;
        this.pos = pos;
    }

    public Direction isOut(Dimension windowSize) {
        boolean outLeft   = pos.x <= width;
        boolean outRight  = pos.x >= windowSize.width  - width;
        boolean outTop    = pos.y <= height;
        boolean outBottom = pos.y >= windowSize.height - height;

        if (outLeft || outRight) {
            return Direction.X;
        } else if (outTop || outBottom) {
            return Direction.Y;
        } else  {
            return Direction.NONE;
        }
    }


    public void setPos(Vektor2 pos) {
        this.pos = pos;
    }

    public Vektor2 getPos() {
        return this.pos;
    }

    public void setVel(Vektor2 vel) {
        this.vel = vel;
    }

    public Vektor2 getVel() {
        return this.vel;
    }

    public int getHeight() {
        return height;
    }
}
