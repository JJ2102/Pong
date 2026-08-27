package objects.animation2d;

import math.Vector2;

import java.awt.Dimension;
import java.awt.Graphics2D;

// Basisklasse für 2D-Objekte in der Hintergrund-Animation des Menüs
public abstract class Object2D {
    protected final Dimension windowSize; // zeichenbare Fläche, begrenzt alle Objekte
    protected final int width;
    protected final int height;
    protected Vector2 position; // Mittelpunkt des Objekts

    // Startet mittig im Fenster (für Objekte wie den Ball im Menü-Hintergrund)
    protected Object2D(Dimension windowSize, int width, int height) {
        this(new Vector2((double) windowSize.width / 2, (double) windowSize.height / 2), windowSize, width, height);
    }

    // Startet an einer festen Position (für Objekte wie die Paddles im Menü-Hintergrund)
    protected Object2D(Vector2 position, Dimension windowSize, int width, int height) {
        this.windowSize = new Dimension(windowSize);
        this.width = width;
        this.height = height;
        this.position = position;
    }

    // Zeichnet das Objekt auf das übergebene Graphics2D-Objekt
    public abstract void paintMe(Graphics2D g2d);

    // ===== Getter und Setter =====
    // Gibt die aktuelle Position (Mittelpunkt) des Objekts zurück
    public Vector2 getPosition() {
        return this.position;
    }

    // Setzt das Objekt auf eine neue Position
    public void setPosition(Vector2 position) {
        this.position = position;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}
