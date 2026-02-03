package sceneManagement;

import Sound.SoundManager;
import enums.EnumOverlays;
import enums.EnumScenes;
import sceneManagement.overlays.*;
import sceneManagement.scenes.GameScene;
import sceneManagement.scenes.MenuScene;
import sceneManagement.scenes.SettingsScene;

import javax.swing.*;
import java.awt.*;

public class GameWindow extends JFrame {
    private EnumScenes currentScene;
    private final Dimension SIZE;

    private boolean DEBUG = false;

    // Manager
    private final SceneManager sceneManager;
    private final SoundManager soundManager;

    // Overlays
    private PauseOverlay pauseOverlay;
    private DifficultyOverlay difficultyOverlay;
    private InfoOverlay infoOverlay;
    private WinOverlay winOverlay;
    private WinOverlay loseOverlay;
    private ShatteredGlassOverlay shatteredGlassOverlay;

    // Scenen
    private MenuScene menuScene;
    private GameScene gameScene;
    private SettingsScene settingsScene;

    public GameWindow(Dimension size) {
        super("Pong 3D"); // Fenstertitel
        SIZE = size; // Fenstergröße speichern

        // Manager initialisieren
        sceneManager = new SceneManager(SIZE);
        this.setContentPane(sceneManager.getLayeredPane());

        soundManager = new SoundManager();

        initManagers(); // Szenen initialisieren

        setDefaultWindowOptions();
        sceneManager.setScene(currentScene); // Startszene anzeigen
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void initManagers() {
        // ===== sceneManager =====
        // Scenes Erstellen und registrieren
        menuScene = new MenuScene(this);
        gameScene = new GameScene(this);
        settingsScene = new SettingsScene(this);

        sceneManager.registerScene(EnumScenes.MENU, menuScene);
        sceneManager.registerScene(EnumScenes.GAME, gameScene);
        sceneManager.registerScene(EnumScenes.SETTINGS, settingsScene);

        // Overlay erstellen (wird über SceneManager angezeigt/verdeckt)
        pauseOverlay = new PauseOverlay(this);
        difficultyOverlay = new DifficultyOverlay(this);
        infoOverlay = new InfoOverlay(this);
        winOverlay = new WinOverlay(this, EnumOverlays.WIN);
        loseOverlay = new WinOverlay(this, EnumOverlays.LOSE);
        shatteredGlassOverlay = new ShatteredGlassOverlay(this);

        sceneManager.registerOverlay(EnumOverlays.PAUSE, pauseOverlay);
        sceneManager.registerOverlay(EnumOverlays.DIFFICULTY, difficultyOverlay);
        sceneManager.registerOverlay(EnumOverlays.INFO, infoOverlay);
        sceneManager.registerOverlay(EnumOverlays.WIN, winOverlay);
        sceneManager.registerOverlay(EnumOverlays.LOSE, loseOverlay);
        sceneManager.registerOverlay(EnumOverlays.SHATTERED_GLASS, shatteredGlassOverlay);

        currentScene = EnumScenes.MENU; // Standard-Szene festlegen

        // ===== soundManager =====
        // Soundeffekte laden
        soundManager.loadSoundEffekt("pong", "res/sounds/pong.wav");
        soundManager.loadSoundEffekt("score", "res/sounds/score.wav");
        soundManager.loadSoundEffekt("win", "res/sounds/win.wav");
        soundManager.loadSoundEffekt("lose", "res/sounds/lose.wav");

        // Hintergrundmusik laden und abspielen
        soundManager.loadBackgroundMusik("bg1", "res/musik/BgSong1.wav");
        soundManager.playBackgroundMusik("bg1");
    }

    private void setDefaultWindowOptions() {
        this.setUndecorated(true);
        this.setResizable(false);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    // start / stop Game
    public void startGame() {
        setCurrentScene(EnumScenes.GAME);
    }

    public void returnToMenu() {
        sceneManager.hideOverlay(EnumOverlays.PAUSE);
        sceneManager.hideOverlay(EnumOverlays.WIN);
        sceneManager.hideOverlay(EnumOverlays.LOSE);

        gameScene.reset();
        setCurrentScene(EnumScenes.MENU);
    }

    // Overlay Methoden
    public void toggleOverlay(EnumOverlays overlayID) {
        if (!sceneManager.isOverlayVisible(overlayID)) {
            sceneManager.showOverlay(overlayID);
        } else {
            sceneManager.hideOverlay(overlayID);
        }
    }

    public void showOverlay(EnumOverlays overlayID, boolean show) {
        if (show && !sceneManager.isOverlayVisible(overlayID)) {
            sceneManager.showOverlay(overlayID);
        } else if (sceneManager.isOverlayVisible(overlayID)) {
            sceneManager.hideOverlay(overlayID);
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

    public ShatteredGlassOverlay getShatteredGlassOverlay() {
        return shatteredGlassOverlay;
    }

    public SoundManager getSoundManager() {
        return soundManager;
    }

    public boolean isDebug() {
        return DEBUG;
    }

    public void toggleDebug() {
        this.DEBUG = !this.DEBUG;
    }
}
