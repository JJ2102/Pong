package scenes;

import enums.EnumScenes;
import overlays.PauseOverlay;

import javax.swing.*;
import java.awt.*;

public class GameWindow extends JFrame {
    private EnumScenes currentScene;
    private final Dimension SIZE;
    private final SceneManager sceneManager;

    // Overlays
    private PauseOverlay pauseOverlay;

    // Scenen
    private MenuScene menuScene;
    private GameScene gameScene;

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
        menuScene = new MenuScene(this);
        gameScene = new GameScene(this);

        sceneManager.registerScene(EnumScenes.MENU, menuScene);
        sceneManager.registerScene(EnumScenes.GAME, gameScene);

        // Overlay erstellen (wird über SceneManager angezeigt/verdeckt)
        pauseOverlay = new PauseOverlay(this);

        currentScene = EnumScenes.MENU; // Standard-Szene festlegen
    }

    private void setDefaultWindowOptions() {
        this.setUndecorated(true);
        this.setResizable(false);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    // Szene-Pause Overlay Methoden
    public void togglePauseOverlay() {
        if (!sceneManager.isOverlayVisible(pauseOverlay)) {
            if (currentScene == EnumScenes.GAME && gameScene != null) { // Spielszene pausieren
                gameScene.stopScene();
            }
            sceneManager.showOverlay(pauseOverlay);
        } else {
            sceneManager.hideOverlay(pauseOverlay);
            if (currentScene == EnumScenes.GAME && gameScene != null) { // Spielszene fortsetzen
                gameScene.startScene();
            }
        }
    }

    // Getters und Setters
    public void setCurrentScene(EnumScenes scene) {
        this.currentScene = scene;
        sceneManager.setScene(scene);
    }

    public EnumScenes getCurrentScene() {
        return currentScene;
    }

    public Dimension getSIZE() {
        return SIZE;
    }

    public GameScene getGameScene() {
        return gameScene;
    }
}
