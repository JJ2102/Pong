package scenes;

import enums.Scenes;
import objekts.GameWindow;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;

public class MenuScene extends Scene {

    public MenuScene(GameWindow window) {
        super(window);
    }

    @Override
    protected void initScene() {
        setLayout(new FlowLayout(FlowLayout.CENTER, 20, 200));

        // Start Button
        JButton startButton = new JButton("Start Game");
        startButton.addActionListener(_ -> window.setCurrentScene(Scenes.GAME));
        add(startButton);

        // Exit Button
        JButton exitButton = new JButton("Exit");
        exitButton.addActionListener(_ -> System.exit(0));
        add(exitButton);
    }

    @Override
    protected void update() {}

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        // Menü zeichnen (Hintergrund, Titel, etc.)
        g.setFont(new Font("Arial", Font.BOLD, 48));
        g.drawString("Pong 3D", getWidth() / 2 - 100, 100);
    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_ENTER -> window.setCurrentScene(Scenes.GAME);
            case KeyEvent.VK_ESCAPE -> System.exit(0);
        }
    }
}
