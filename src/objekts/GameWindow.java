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
        super("Pong 3D");

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);
        this.add(mainPanel);

        initScenes(); // Szenen initialisieren
        showScene();  // Anfangsszene anzeigen

        // JFrame Einstellungen
        this.setSize(size);
        this.setUndecorated(true);
        this.setResizable(false);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);
    }

    private void initScenes() {
        // MenuScene
        MenuScene menuScene = new MenuScene(this);
        mainPanel.add(menuScene, Scenes.MENU.name());

        // GameScene
        GameScene gameScene = new GameScene(this);
        mainPanel.add(gameScene, Scenes.GAME.name());

        currentScene = Scenes.MENU; // Standard-Szene festlegen
    }


    public void showScene() {
        cardLayout.show(mainPanel, currentScene.name());
    }

    // Getters und Setters
    public void setCurrentScene(Scenes currentScene) {
        this.currentScene = currentScene;
        showScene();
    }


}
