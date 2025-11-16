package scenes;

import objekts.GameWindow;

import javax.swing.*;
import java.awt.*;

public class MenuScene extends Scene {

    public MenuScene(GameWindow window) {
        super(window);
    }

    @Override
    protected void initScene() {
        // Start Button
        JButton startButton = new JButton("Start Game");
        add(startButton);
        startButton.addActionListener(_ -> {
            window.setCurrentScene(enums.Scenes.GAME);
        });

        // Exit Button
        JButton exitButton = new JButton("Exit");
        add(exitButton);
        exitButton.addActionListener(_ -> System.exit(0));
    }

    @Override
    protected void update() {}
}
