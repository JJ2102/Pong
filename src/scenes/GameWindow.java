package scenes;

import enums.Scenes;

import javax.swing.*;
import java.awt.*;

public class GameWindow extends JFrame {
    private Scenes currentScene;
    private final Dimension SIZE;
    private final SceneManager sceneManager;

    private PauseOverlay pauseOverlay;

    public GameWindow(Dimension size) {
        super("Pong 3D"); // Fenstertitel
        SIZE = size; // Fenstergröße speichern

        // SceneManager mit LayeredPane initialisieren
        sceneManager = new SceneManager(SIZE);
        this.setContentPane(sceneManager.getLayeredPane());

        initScenes(); // Szenen initialisieren
        setDefaultWindowOptions();
        sceneManager.setScene(currentScene); // Startszene anzeigen
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void initScenes() {
        // Scenes Erstellen und registrieren
        MenuScene menuScene = new MenuScene(this);
        GameScene gameScene = new GameScene(this);

        sceneManager.registerScene(Scenes.MENU, menuScene);
        sceneManager.registerScene(Scenes.GAME, gameScene);

        // Overlay erstellen (wird über SceneManager angezeigt/verdeckt)
        pauseOverlay = new PauseOverlay(this);

        currentScene = Scenes.MENU; // Standard-Szene festlegen
    }

    private void setDefaultWindowOptions() {
        this.setUndecorated(true);
        this.setResizable(false);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    // Szene-Pause Overlay Methoden
    public void togglePauseOverlay() {
        if (!sceneManager.isOverlayVisible(pauseOverlay)) {
            sceneManager.showOverlay(pauseOverlay);
        } else {
            sceneManager.hideOverlay(pauseOverlay);
        }
    }

    // Getters und Setters
    public void setCurrentScene(Scenes scene) {
        this.currentScene = scene;
        sceneManager.setScene(scene);
    }

    public Scenes getCurrentScene() {
        return currentScene;
    }

    public Dimension getSIZE() {
        return SIZE;
    }
}
