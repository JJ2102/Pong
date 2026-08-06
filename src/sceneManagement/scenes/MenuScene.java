package scenemanagement.scenes;

import enums.OverlayType;
import enums.SceneType;
import math.Vector2;
import objects.animation2d.Ball2D;
import objects.animation2d.Paddle2D;
import scenemanagement.GameWindow;
import utility.Button;

import java.awt.*;
import java.awt.event.KeyEvent;

public class MenuScene extends ButtonScene {
    private final Ball2D ball;

    private final Paddle2D p1;
    private final Paddle2D p2;

    public MenuScene(GameWindow window) {
        super(window, "Pong 3D");

        ball = new Ball2D(window.getWindowSize(), 20);
        p1 = new Paddle2D(new Vector2(10, (double) window.getWindowSize().height / 2));
        p2 = new Paddle2D(new Vector2(window.getWindowSize().width - 10, (double) window.getWindowSize().height / 2));

        // Buttons
        Button startButton = new Button("Start Game");
        Button settingsButton = new Button("Settings");
        Button infoButton = new Button("Information");
        Button exitButton = new Button("Exit");

        // Aktionen
        startButton.addActionListener(_ -> window.toggleOverlay(OverlayType.DIFFICULTY));
        infoButton.addActionListener(_ -> window.toggleOverlay(OverlayType.INFO));
        settingsButton.addActionListener(_ -> window.setCurrentScene(SceneType.SETTINGS));
        exitButton.addActionListener(_ -> System.exit(0));

        addComponent(startButton);
        addComponent(settingsButton);
        addComponent(infoButton);
        addComponent(exitButton);

        positionComponents();
    }

    @Override
    protected void update() {
        ball.move();
        p1.move(ball.getPosition().y);
        p2.move(ball.getPosition().y);
        switch (ball.isOut(window.getWindowSize())) {
            case X -> ball.switchXDirection();
            case Y -> ball.switchYDirection();
            default -> {}
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        p1.paintMe(g2d);
        p2.paintMe(g2d);
        ball.paintMe(g2d);
    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_ENTER -> window.setCurrentScene(SceneType.GAME);
            case KeyEvent.VK_ESCAPE -> System.exit(0);
        }
    }
}
