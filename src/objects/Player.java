package objects;

import math.Vector3;

import java.awt.Color;

/**
 * Stellt das vom Spieler gesteuerte Paddle dar.
 */
public class Player extends Paddle {
    /**
     * Initialisiert den Spieler an der Startposition mit festgelegten Farben.
     *
     * @param position Startposition des Spielers
     */
    public Player(Vector3 position) {
        super(position, new Color(0, 0, 255, 100), Color.CYAN);
    }

    /**
     * Bewegt den Spieler an die angegebene Position und aktualisiert die Hitbox.
     *
     * @param position neue Position des Spielers
     */
    public void moveTo(Vector3 position) {
        getTransform().setPosition(position);
        getHitbox().setPosition(getTransform().getPosition());
    }
}
