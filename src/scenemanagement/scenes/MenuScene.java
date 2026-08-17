package scenemanagement.scenes;

import enums.OverlayType;
import enums.SceneType;
import math.Vector2;
import objects.animation2d.Ball2D;
import objects.animation2d.Paddle2D;
import scenemanagement.GameWindow;
import utility.Button;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;

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
        ball = new Ball2D(windowSize, 20);
        leftPaddle = new Paddle2D(new Vector2(10, (double) windowSize.height / 2));
        rightPaddle = new Paddle2D(new Vector2(windowSize.width - 10, (double) windowSize.height / 2));

        // Buttons
        Button startButton = new Button("Start Game");
        Button settingsButton = new Button("Settings");
        Button infoButton = new Button("Information");
        Button exitButton = new Button("Exit");

        // Aktionen für Buttons verknüpfen
        // Erst Schwierigkeit wählen, dann startet das Spiel
        startButton.addActionListener(_ -> window.toggleOverlay(OverlayType.DIFFICULTY));
        infoButton.addActionListener(_ -> window.toggleOverlay(OverlayType.INFO));
        settingsButton.addActionListener(_ -> window.setCurrentScene(SceneType.SETTINGS));
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
        ball.move();
        leftPaddle.move(ball.getPosition().getY()); // Paddles folgen der Y-Position des Balls
        rightPaddle.move(ball.getPosition().getY());

        switch (ball.isOut(window.getWindowSize())) {
            case X -> ball.switchXDirection(); // Prallt an den Paddles (oder Wänden) ab
            case Y -> ball.switchYDirection(); // Prallt an Decke/Boden ab
            default -> {
            }
        }
    }

    // Zeichnet die 2D-Objekte der Hintergrund-Animation
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        // Paddles zuerst, der Ball wird darüber gezeichnet
        leftPaddle.paintMe(g2d);
        rightPaddle.paintMe(g2d);
        ball.paintMe(g2d);
    }

    // Tastenkürzel im Menü: Enter startet das Spiel, Escape beendet das Programm
    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_ENTER -> window.setCurrentScene(SceneType.GAME); // Schnelles Starten mit Enter
            case KeyEvent.VK_ESCAPE -> System.exit(0); // Beenden mit Escape
            default -> {
            }
        }
    }
}
