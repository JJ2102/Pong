package objects;

import math.Vector3;
import meshes.SphereMesh;
import objects.hitboxes.BoxHitbox;
import utility.Globals;

import java.awt.Color;

/**
 * Stellt den 3D-Spielball im Raum dar.
 * Der Ball bewegt sich mit konstanter Geschwindigkeit, prallt an den seitlichen Wänden der Box ab
 * und kehrt bei einem Paddle-Treffer seine Z-Richtung um.
 */
public class Ball extends Entity {
    private static final double RADIUS = 0.2;

    // Geschwindigkeit
    private static final double MIN_SPEED = 0.03;
    private static final double MAX_SPEED = 0.05;
    private static final double SPEED_INCREASE_PER_HIT = 0.001; // Zuwachs pro Paddle-Treffer und Achse

    // Grenzen für den Mittelpunkt des Balls, damit sein Rand genau die Wand berührt
    private final double maxX;
    private final double maxY;

    private Vector3 velocity;

    /**
     * Initialisiert den Ball mit seinem Mesh, seiner Hitbox und einer zufälligen Startgeschwindigkeit.
     *
     * @param boxSize Größe der Spielfeld-Box, aus der die Wandgrenzen berechnet werden
     */
    public Ball(Vector3 boxSize) {
        super(Color.YELLOW, Color.ORANGE, new SphereMesh(RADIUS, 10, 10));

        // Wandgrenzen aus der tatsächlichen Boxgröße ableiten, die Wand steht bei der halben Ausdehnung
        Vector3 halfBoxSize = boxSize.divide(2);
        this.maxX = halfBoxSize.getX() - RADIUS;
        this.maxY = halfBoxSize.getY() - RADIUS;

        // Die Hitbox umschließt den Ball genau: halbe Kantenlänge entspricht dem Radius
        double size = RADIUS * 2;
        setHitbox(new BoxHitbox(getTransform().getPosition(), new Vector3(size, size, size), Color.BLUE));

        // Zufällige Anfangsgeschwindigkeit setzen
        setRandomSpeed();
    }

    /**
     * Erzeugt einen zufälligen Geschwindigkeitswert zwischen {@code MIN_SPEED} und {@code MAX_SPEED}
     * mit zufälligem Vorzeichen.
     *
     * @return zufällige Geschwindigkeit für eine Achse
     */
    private double randomSpeed() {
        return Globals.randomSpeed(MIN_SPEED, MAX_SPEED);
    }

    /**
     * Setzt die Geschwindigkeit des Balls auf allen drei Achsen auf Zufallswerte.
     */
    private void setRandomSpeed() {
        velocity = new Vector3(randomSpeed(), randomSpeed(), randomSpeed());
    }

    /**
     * Setzt den Ball auf den Ursprung zurück und vergibt eine neue zufällige Geschwindigkeit.
     */
    public void reset() {
        getTransform().setPosition(new Vector3(0, 0, 0));
        setRandomSpeed();
    }

    /**
     * Prüft auf Kollision mit den übergebenen Paddles.
     * <p>
     * Bei einem Treffer wird der Betrag der Geschwindigkeit auf jeder Achse um {@code SPEED_INCREASE_PER_HIT}
     * erhöht und die Z-Richtung umgekehrt.
     *
     * @param paddles Hitboxen der Paddles, mit denen die Kollision geprüft wird
     * @return {@code true}, wenn der Ball ein Paddle getroffen hat, sonst {@code false}
     */
    public boolean paddleHit(BoxHitbox[] paddles) {
        for (BoxHitbox paddle : paddles) {
            if (getHitbox().intersects(paddle)) {
                // Jeder Treffer macht den Ball etwas schneller, die Richtung bleibt dabei erhalten
                velocity.setX(velocity.getX() + Math.signum(velocity.getX()) * SPEED_INCREASE_PER_HIT);
                velocity.setY(velocity.getY() + Math.signum(velocity.getY()) * SPEED_INCREASE_PER_HIT);
                velocity.setZ(velocity.getZ() + Math.signum(velocity.getZ()) * SPEED_INCREASE_PER_HIT);
                velocity.setZ(-velocity.getZ()); // Z-Richtung umkehren

                return true;
            }
        }
        return false;
    }

    /**
     * Bewegt den Ball um seine Geschwindigkeit weiter, lässt ihn an den seitlichen Wänden abprallen
     * und zieht die Hitbox nach.
     */
    public void move() {
        // Position basierend auf der Geschwindigkeit aktualisieren
        getTransform().setPosition(getTransform().getPosition().add(velocity));

        // Einfache Kollisionserkennung mit den Wänden
        Vector3 position = getTransform().getPosition();
        if (isOut(-maxX, maxX, position.getX())) {
            velocity.setX(-velocity.getX());
        }
        if (isOut(-maxY, maxY, position.getY())) {
            velocity.setY(-velocity.getY());
        }

        getHitbox().setPosition(getTransform().getPosition());
    }

    /**
     * Prüft, ob ein Wert außerhalb des Bereichs zwischen {@code min} und {@code max} liegt.
     *
     * @param min untere Grenze
     * @param max obere Grenze
     * @param value zu prüfender Wert
     * @return {@code true}, wenn der Wert kleiner als {@code min} oder größer als {@code max} ist,
     *         sonst {@code false}
     */
    private boolean isOut(double min, double max, double value) {
        return value > max || value < min;
    }

    @Override
    public String toString() {
        return "[Ball] Pos: " + getTransform().getPosition() + " Speed: " + velocity;
    }
}
