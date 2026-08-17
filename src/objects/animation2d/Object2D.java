package objects.animation2d;

import math.Vector2;

import java.awt.Dimension;

// Basisklasse für 2D-Objekte in der Hintergrund-Animation des Menüs
public class Object2D {
    protected final int width;
    protected final int height;
    protected Vector2 velocity;
    private Vector2 position;

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

    // Gibt die aktuelle Position des Objekts zurück
    public Vector2 getPosition() {
        return this.position;
    }

    // Setzt das Objekt auf eine neue Position
    public void setPosition(Vector2 position) {
        this.position = position;
    }
}
