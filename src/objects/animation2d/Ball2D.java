package objects.animation2d;

import enums.Direction;
import math.Vector2;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;

// Repräsentiert den animierten 2D-Ball im Hintergrund-Menü
public class Ball2D extends Object2D {
    private static final double MIN_SPEED = 10.0;
    private static final double MAX_SPEED = 15.0;
    private static final double MAX_ANGLE = Math.toRadians(45); // Streuung um die Waagerechte

    private final int radius;
    private Vector2 velocity;

    // Initialisiert den Ball mit einer zufälligen Startgeschwindigkeit
    public Ball2D(Dimension windowSize, int size) {
        super(windowSize, size, size);
        this.radius = size / 2;
        this.velocity = getNewVelocity();
    }

    // Setzt den Ball zurück in die Fenstermitte und würfelt eine neue Richtung aus
    public void reset() {
        setPosition(new Vector2((double) windowSize.width / 2, (double) windowSize.height / 2));
        velocity = getNewVelocity();
    }

    // Zufällige Geschwindigkeit: fester Betrag, flacher Winkel nach links oder rechts
    private Vector2 getNewVelocity() {
        double speed = MIN_SPEED + Math.random() * (MAX_SPEED - MIN_SPEED);
        double angle = (Math.random() * 2 - 1) * MAX_ANGLE; // -45° bis +45°
        double directionX = Math.random() < 0.5 ? -1 : 1; // zufällig nach links oder rechts

        return new Vector2(directionX * speed * Math.cos(angle), speed * Math.sin(angle));
    }

    // Kehrt die horizontale Bewegungsrichtung um
    public void switchXDirection() {
        velocity.setX(velocity.getX() * -1);
    }

    // Kehrt die vertikale Bewegungsrichtung um
    public void switchYDirection() {
        velocity.setY(velocity.getY() * -1);
    }

    // Bewegt den Ball einen Schritt weiter und lässt ihn an den Paddles abprallen
    public void move(Paddle2D paddleLeft, Paddle2D paddleRight) {
        setPosition(getPosition().add(velocity));

        bounceOff(paddleLeft);
        bounceOff(paddleRight);
    }

    // Prüft die Überlappung mit einem Paddle (Rechteck gegen Ball-Hüllquadrat) und prallt daran ab
    private void bounceOff(Paddle2D paddle) {
        // Abstand der Mittelpunkte
        double distanceX = getPosition().getX() - paddle.getPosition().getX();
        double distanceY = getPosition().getY() - paddle.getPosition().getY();

        // Wie tief die beiden Rechtecke ineinander stecken; negativ bedeutet "kein Kontakt"
        double overlapX = (double) paddle.getWidth() / 2 + radius - Math.abs(distanceX);
        double overlapY = (double) paddle.getHeight() / 2 + radius - Math.abs(distanceY);

        // Nur abprallen, wenn sich beide Achsen überlappen und der Ball auf das Paddle zufliegt
        // (sonst bleibt er im Paddle hängen und kehrt jeden Frame die Richtung um)
        if (overlapX <= 0 || overlapY <= 0 || distanceX * velocity.getX() >= 0) {
            return;
        }

        switchXDirection();

        // Ball aus dem Paddle herausschieben, damit er im nächsten Schritt nicht erneut auslöst
        setPosition(getPosition().add(new Vector2(Math.signum(distanceX) * overlapX, 0)));
    }

    // Prüft, ob der Ball den linken/rechten oder den oberen/unteren Fensterrand erreicht hat
    public Direction isOut() {
        boolean outLeft = getPosition().getX() - radius <= 0;
        boolean outRight = getPosition().getX() + radius >= windowSize.width;
        boolean outTop = getPosition().getY() - radius <= 0;
        boolean outBottom = getPosition().getY() + radius >= windowSize.height;

        if (outLeft || outRight) {
            return Direction.X;
        } else if (outTop || outBottom) {
            return Direction.Y;
        } else {
            return Direction.NONE;
        }
    }

    // Zeichnet den Ball auf das übergebene Grafics2D-Objekt
    @Override
    public void paintMe(Graphics2D g2d) {
        double posX = getPosition().getX() - radius;
        double posY = getPosition().getY() - radius;

        Ellipse2D.Double ball = new Ellipse2D.Double(posX, posY, width, height);

        g2d.setColor(Color.WHITE);
        g2d.fill(ball);
    }
}
