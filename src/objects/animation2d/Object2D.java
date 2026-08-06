package objects.animation2d;

import math.Vector2;

import java.awt.*;

public class Object2D {
    protected final int width;
    protected final int height;
    private Vector2 position;
    protected Vector2 velocity;

    // Startet mittig im Fenster (für Objekte wie den Ball im Menü-Hintergrund)
    public Object2D(Dimension windowSize, int width, int height) {
        this.width = width;
        this.height = height;
        this.position = new Vector2((double) windowSize.width / 2, (double) windowSize.height / 2);
    }

    // Startet an einer festen Position (für Objekte wie die Paddles im Menü-Hintergrund)
    public Object2D(Vector2 position, int width, int height) {
        this.width = width;
        this.height = height;
        this.position = position;
    }

    public Vector2 getPosition() {
        return this.position;
    }

    public void setPosition(Vector2 position) {
        this.position = position;
    }
}
