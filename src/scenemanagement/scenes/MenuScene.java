package scenemanagement.scenes;

import enums.OverlayType;
import math.Vector2;
import objects.animation2d.Ball2D;
import objects.animation2d.Paddle2D;
import pong.GameWindow;
import utility.Button;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

// Das Hauptmenü des Spiels mit Start-, Info- und Einstellungs-Buttons
public class MenuScene extends ButtonScene {
    private final transient Ball2D ball; // Ball der 2D-Hintergrund-Animation

    private final transient Paddle2D leftPaddle; // Linkes Paddle der Animation
    private final transient Paddle2D rightPaddle; // Rechtes Paddle der Animation

    // Initialisiert das Menü, die Hintergrund-Animation (2D-Pong) und die Buttons
    public MenuScene(GameWindow window) {
        super(window, "Pong 3D");

        // Objekte der Hintergrund-Animation erzeugen, die Paddles kleben am linken bzw. rechten Fensterrand
        Dimension windowSize = window.getWindowSize();
        ball = new Ball2D(windowSize, 50);
        leftPaddle = new Paddle2D(new Vector2(10, (double) windowSize.height / 2), windowSize);
        rightPaddle = new Paddle2D(new Vector2(windowSize.width - 10, (double) windowSize.height / 2), windowSize);

        // Buttons
        Button startButton = new Button("Start Game");
        Button settingsButton = new Button("Settings");
        Button infoButton = new Button("Information");
        Button exitButton = new Button("Exit");

        // Aktionen für Buttons verknüpfen
        // Erst Schwierigkeit wählen, dann startet das Spiel
        startButton.addActionListener(_ -> window.toggleOverlay(OverlayType.DIFFICULTY));
        infoButton.addActionListener(_ -> window.toggleOverlay(OverlayType.INFO));
        settingsButton.addActionListener(_ -> window.showSettings());
        exitButton.addActionListener(_ -> System.exit(0));

        // Buttons in der gewünschten Reihenfolge unter den Titel einreihen
        addComponent(startButton);
        addComponent(settingsButton);
        addComponent(infoButton);
        addComponent(exitButton);

        positionComponents(); // Buttons ausrichten
    }

    // Aktualisiert die 2D-Hintergrund-Animation (Ball und Paddles im Menü)
    @Override
    protected void update() {
        ball.move(leftPaddle, rightPaddle); // Ball bewegt sich und prallt an den Paddles ab

        switch (ball.isOut()) {
            case X -> ball.reset(); // Ball ist an einem Paddle vorbei: neu von der Mitte starten
            case Y -> ball.switchYDirection(); // Prallt an Decke/Boden ab
            default -> {}
        }
    }

    // Zeichnet die 2D-Objekte der Hintergrund-Animation
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        // gestrichelte Linie in der Mitte des Fensters zeichnen
        g2d.setColor(java.awt.Color.WHITE);
        int midX = window.getWindowSize().width / 2;

        Stroke dashed = new BasicStroke(3, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL,
                                0, new float[]{30}, 0);

        g2d.setStroke(dashed);
        g2d.drawLine(midX, 0, midX, window.getWindowSize().height);

        // strich zurücksetzen, damit die Buttons nicht gestrichelt sind
        g2d.setStroke(new BasicStroke());

        // Paddles zuerst, der Ball wird darüber gezeichnet
        leftPaddle.paintMe(g2d);
        rightPaddle.paintMe(g2d);
        ball.paintMe(g2d);
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        leftPaddle.move(e.getY()); // Beide Paddles folgen der Mausbewegung
        rightPaddle.move(e.getY());
    }

    // Tastenkürzel im Menü: Enter startet das Spiel, Escape beendet das Programm
    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
            System.exit(0); // Beenden mit Escape
        }
    }
}
