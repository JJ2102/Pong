package objects;

import math.Vector3;

import java.awt.Color;

// Repräsentiert das vom Spieler gesteuerte Paddle
public class Player extends Paddle {
    // Initialisiert den Spieler an der Startposition mit festgelegten Farben
    public Player(Vector3 position) {
        super(position, new Color(0, 0, 255, 100), new Color(0, 255, 255));
    }

    // Bewegt den Spieler an die angegebene Position und aktualisiert die Hitbox
    public void moveTo(Vector3 position) {
        getTransform().setPosition(position);
        getHitbox().setPosition(getTransform().getPosition());
    }
}
