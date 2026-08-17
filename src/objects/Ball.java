package objects;

import hitboxes.BoxHitbox;
import math.Vector3;
import meshes.EllipseMesh;
import rendering.Mesh;
import utility.Globals;

import java.awt.Color;

// Repräsentiert den 3D-Spielball im Raum
public class Ball extends Entity {
    private static final double RADIUS = 0.2;

    private Vector3 velocity;

    // Initialisiert den Ball mit seinem Mesh, seiner Hitbox und einer zufälligen Startgeschwindigkeit
    public Ball() {
        super(Color.YELLOW, Color.ORANGE);
        Mesh boxMesh = new EllipseMesh(RADIUS, 10, 10);
        setMesh(boxMesh);

        double size = RADIUS * 1.5;
        setHitbox(new BoxHitbox(getTransform().getPosition(), new Vector3(size, size, size)));

        // Zufällige Anfangsgeschwindigkeit setzen
        setRandomSpeed();
    }

    // Generiert einen zufälligen Geschwindigkeitswert
    private double randomSpeed() {
        return Globals.randomSpeed(0.03, 0.05);
    }

    // Setzt die Geschwindigkeit des Balls in alle Richtungen auf Zufallswerte
    private void setRandomSpeed() {
        velocity = new Vector3(randomSpeed(), randomSpeed(), randomSpeed());
    }

    // Setzt den Ball auf den Ursprung zurück und vergibt eine neue Geschwindigkeit
    public void reset() {
        getTransform().setPosition(new Vector3(0, 0, 0));
        setRandomSpeed();
    }

    // Prüft auf Kollision mit den übergebenen Paddles und ändert bei einem Treffer die Z-Richtung
    public boolean paddleHit(BoxHitbox[] paddles) {
        for (BoxHitbox paddle : paddles) {
            if (getHitbox().intersects(paddle)) {
                // jeden hit plus 0.001 speed
                velocity.setX(velocity.getX() + Math.signum(velocity.getX()) * 0.001);
                velocity.setY(velocity.getY() + Math.signum(velocity.getY()) * 0.001);
                velocity.setZ(velocity.getZ() + Math.signum(velocity.getZ()) * 0.001);
                velocity.setZ(-velocity.getZ()); // Richtung ändern

                return true;
            }
        }
        return false;
    }

    // Bewegt den Ball und lässt ihn an den seitlichen Wänden abprallen
    public void move() {
        // Position basierend auf der Geschwindigkeit aktualisieren
        getTransform().setPosition(getTransform().getPosition().add(velocity));

        // Einfache Kollisionserkennung mit den Wänden
        Vector3 position = getTransform().getPosition();
        if (position.getX() > 2 - RADIUS || position.getX() < -2 + RADIUS) {
            velocity.setX(-velocity.getX());
        }
        if (position.getY() > 1 - RADIUS || position.getY() < -1 + RADIUS) {
            velocity.setY(-velocity.getY());
        }

        getHitbox().setPosition(getTransform().getPosition());
    }

    // Liefert eine Debug-Ausgabe des Balls
    @Override
    public String toString() {
        return "[Ball] Pos: " + getTransform().getPosition() + " Speed: " + velocity;
    }
}
