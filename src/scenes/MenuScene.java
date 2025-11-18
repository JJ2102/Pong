package scenes;

import enums.Scenes;
import objekts.GameWindow;
import utility.Button;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;

public class MenuScene extends Scene {

    public MenuScene(GameWindow window) {
        super(window);
    }

    @Override
    protected void initScene() {
        setLayout(new GridBagLayout()); // Zentrierte Inhalte

        // Titel-Label
        JLabel title = new JLabel("Pong 3D");
        title.setFont(new Font("Arial", Font.BOLD, 72));
        title.setForeground(Color.BLACK);

        // Start Button
        Button startButton = new Button("Start Game");
        startButton.addActionListener(_ -> window.setCurrentScene(Scenes.GAME));
        add(startButton);

        // Exit Button
        Button exitButton = new Button("Exit");
        exitButton.addActionListener(_ -> System.exit(0));
        add(exitButton);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(20, 0, 20, 0);

        gbc.gridy = 0;
        add(title, gbc);

        gbc.gridy = 1;
        add(startButton, gbc);

        gbc.gridy = 2;
        add(exitButton, gbc);
    }

    @Override
    protected void update() {}

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_ENTER -> window.setCurrentScene(Scenes.GAME);
            case KeyEvent.VK_ESCAPE -> System.exit(0);
        }
    }
}
