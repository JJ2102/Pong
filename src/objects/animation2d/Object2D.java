package objects.animation2d;

import math.Vector2;

import java.awt.Dimension;
import java.awt.Graphics2D;

/**
 * Basisklasse für 2D-Objekte in der Hintergrund-Animation des Menüs.
 * Jedes Objekt besitzt eine Größe und eine Position, die seinen Mittelpunkt beschreibt.
 */
public abstract class Object2D {
    protected final Dimension windowSize; // zeichenbare Fläche, begrenzt alle Objekte
    protected final int width;
    protected final int height;
    protected Vector2 position; // Mittelpunkt des Objekts

    /**
     * Erstellt ein Objekt, das mittig im Fenster startet (z. B. den Ball im Menü-Hintergrund).
     *
     * @param windowSize Größe der zeichenbaren Fläche
     * @param width Breite des Objekts in Pixeln
     * @param height Höhe des Objekts in Pixeln
     */
    protected Object2D(Dimension windowSize, int width, int height) {
        this(new Vector2((double) windowSize.width / 2, (double) windowSize.height / 2), windowSize, width, height);
    }

    /**
     * Erstellt ein Objekt, das an einer festen Position startet (z. B. die Paddles im Menü-Hintergrund).
     *
     * @param position Startposition (Mittelpunkt) des Objekts
     * @param windowSize Größe der zeichenbaren Fläche, wird kopiert
     * @param width Breite des Objekts in Pixeln
     * @param height Höhe des Objekts in Pixeln
     */
    protected Object2D(Vector2 position, Dimension windowSize, int width, int height) {
        this.windowSize = new Dimension(windowSize);
        this.width = width;
        this.height = height;
        this.position = position;
    }

    /**
     * Zeichnet das Objekt auf das übergebene Graphics2D-Objekt.
     *
     * @param g2d Grafikkontext, auf den gezeichnet wird
     */
    public abstract void paintMe(Graphics2D g2d);

    // ===== Getter und Setter =====
    /**
     * Gibt die aktuelle Position (Mittelpunkt) des Objekts zurück.
     *
     * @return aktuelle Position des Objekts
     */
    public Vector2 getPosition() {
        return this.position;
    }

    /**
     * Setzt das Objekt auf eine neue Position.
     *
     * @param position neue Position (Mittelpunkt) des Objekts
     */
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
