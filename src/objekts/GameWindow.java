package objekts;

import enums.Scenes;
import scenes.GameScene;
import scenes.MenuScene;

import javax.swing.*;
import java.awt.*;

public class GameWindow extends JFrame {
    private final CardLayout cardLayout;
    private final JPanel mainPanel;
    private Scenes currentScene;

    public GameWindow(Dimension size) {
        this.setTitle("Pong 3D");

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);
        this.add(mainPanel);

        initScenes(); // Szenen initialisieren
        showScene();  // Anfangsszene anzeigen

        // JFrame Einstellungen
        this.setSize(size);
        this.setUndecorated(true);
        this.setResizable(false);
        this.setVisible(true);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    private void initScenes() {
        currentScene = Scenes.MENU; // Standard-Szene festlegen

        // GameScene
        GameScene gameScene = new GameScene(this);
        mainPanel.add(gameScene, "GameScene");

        // MenuScene
        MenuScene menuScene = new MenuScene(this);
        mainPanel.add(menuScene, "MenuScene");
    }


    public void showScene() {
        switch (currentScene) {
            case GAME -> cardLayout.show(mainPanel, "GameScene");
            case MENU -> cardLayout.show(mainPanel, "MenuScene");
        }
    }

    // Getters und Setters
    public void setCurrentScene(Scenes currentScene) {
        this.currentScene = currentScene;
        showScene();
    }


}
