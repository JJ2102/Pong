package sceneManagement.scenes;

import enums.EnumScenes;
import sceneManagement.GameWindow;
import utility.Button;

import java.awt.*;
import java.awt.event.KeyEvent;

public class MenuScene extends ButtonScene {

    public MenuScene(GameWindow window) {
        super(window, "Pong 3D");

        // Buttons
        Button startButton = new Button("Start Game");
        Button settingsButton = new Button("Settings");
        Button exitButton = new Button("Exit");

        // Aktionen
        startButton.addActionListener(_ -> window.toggleDifficultyOverlay());
        settingsButton.addActionListener(_ -> window.setCurrentScene(EnumScenes.SETTINGS));
        exitButton.addActionListener(_ -> System.exit(0));

        addComponent(startButton);
        addComponent(settingsButton);
        addComponent(exitButton);

        positionComponents();
    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_ENTER -> window.setCurrentScene(EnumScenes.GAME);
            case KeyEvent.VK_ESCAPE -> System.exit(0);
        }
    }
}
