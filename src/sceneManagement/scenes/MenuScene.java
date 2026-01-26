package sceneManagement.scenes;

import enums.EnumOverlays;
import enums.EnumScenes;
import math.Vektor2;
import objekts.animation2D.Ball2D;
import objekts.animation2D.Paddle2D;
import sceneManagement.GameWindow;
import utility.Button;

import java.awt.*;
import java.awt.event.KeyEvent;

public class MenuScene extends ButtonScene {
    private final Ball2D ball;

    private final Paddle2D p1;
    private final Paddle2D p2;

    public MenuScene(GameWindow window) {
        super(window, "Pong 3D");

        ball = new Ball2D(window.getSIZE());
        p1 = new Paddle2D(new Vektor2(10, (double) window.getSIZE().height / 2));
        p2 = new Paddle2D(new Vektor2(window.getSIZE().width - 10, (double) window.getSIZE().height / 2));

        // Buttons
        Button startButton = new Button("Start Game");
        Button settingsButton = new Button("Settings");
        Button infoButton = new Button("Information");
        Button exitButton = new Button("Exit");

        // Aktionen
        startButton.addActionListener(_ -> window.toggleOverlay(EnumOverlays.DIFFICULTY));
        infoButton.addActionListener(_ -> window.toggleOverlay(EnumOverlays.INFO));
        settingsButton.addActionListener(_ -> window.setCurrentScene(EnumScenes.SETTINGS));
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
        p1.move(ball.getPos().y);
        p2.move(ball.getPos().y);
        switch (ball.isOut(window.getSIZE())) {
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
            case KeyEvent.VK_ENTER -> window.setCurrentScene(EnumScenes.GAME);
            case KeyEvent.VK_ESCAPE -> System.exit(0);
        }
    }
}
