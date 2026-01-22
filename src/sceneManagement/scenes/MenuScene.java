package sceneManagement.scenes;

import enums.EnumOverlays;
import enums.EnumScenes;
import math.Vektor3;
import objekts.Ball;
import sceneManagement.GameWindow;
import utility.Button;
import utility.Globals;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.geom.Ellipse2D;

public class MenuScene extends ButtonScene {
    // TODO: ADD PADDELS

    private final int radius = 20;
    private Vektor3 pos;
    private Vektor3 vel;

    public MenuScene(GameWindow window) {
        super(window, "Pong 3D");
        // TODO: COLOR-UPDATE

        // Set Ball Values
        pos = new Vektor3((double) window.getSIZE().width / 2, (double) window.getSIZE().height / 2, 0);
        vel = new Vektor3(Globals.randomSpeed(3, 5), Globals.randomSpeed(3, 5), 0);

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
        pos.x += vel.x;
        pos.y += vel.y;
        if (pos.x <= radius || pos.x >= window.getWidth() - radius) {
            vel.x = -vel.x;
        }
        if (pos.y <= radius || pos.y >= window.getHeight() - radius) {
            vel.y = -vel.y;
        }
    };

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        Ellipse2D.Double ball = new Ellipse2D.Double(pos.x, pos.y, radius*2, radius*2);

        g2d.setColor(Color.WHITE);
        g2d.draw(ball);
        g2d.fill(ball);
    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_ENTER -> window.setCurrentScene(EnumScenes.GAME);
            case KeyEvent.VK_ESCAPE -> System.exit(0);
        }
    }
}
