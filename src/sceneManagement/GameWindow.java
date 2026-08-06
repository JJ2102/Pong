package scenemanagement;

import sound.SoundManager;
import enums.OverlayType;
import enums.SceneType;
import scenemanagement.overlays.*;
import scenemanagement.scenes.GameScene;
import scenemanagement.scenes.MenuScene;
import scenemanagement.scenes.SettingsScene;

import javax.swing.*;
import java.awt.*;

// Hauptfenster des Spiels, verwaltet alle Szenen und Overlays sowie die Soundausgabe
public class GameWindow extends JFrame {
    private SceneType currentScene; // speichert die aktuelle Szene
    private final Dimension windowSize;

    private boolean debug = false; // wird für debugausgaben genutzt

    // Manager
    private final SceneManager sceneManager;
    private final SoundManager soundManager;

    private ShatteredGlassOverlay shatteredGlassOverlay;

    private GameScene gameScene;

    // Initialisiert das Hauptfenster, die Manager und lädt die Ressourcen
    public GameWindow(Dimension windowSize) {
        super("Pong 3D"); // Fenstertitel
        this.windowSize = windowSize; // Fenstergröße speichern

        // Manager initialisieren
        sceneManager = new SceneManager(this.windowSize);
        this.setContentPane(sceneManager.getLayeredPane()); // JLayeredPane als Container setzen, um Ebenen zu ermöglichen

        soundManager = new SoundManager();

        initManagers(); // Szenen und Sound laden

        // Fenster Einstellungen Setzen
        setDefaultWindowOptions();
        sceneManager.setScene(currentScene); // Startszene anzeigen
        pack(); // Passt die Fenstergröße an den Inhalt an
        this.setLocationRelativeTo(null); // Zentriert das Fenster auf dem Bildschirm
        setVisible(true);
    }

    // Erstellt und registriert alle verfügbaren Szenen und Overlays, sowie die Soundeffekte
    private void initManagers() {
        // ===== sceneManager =====
        // Scenen Erstellen und für den SceneManager registrieren
        MenuScene menuScene = new MenuScene(this);
        sceneManager.registerScene(SceneType.MENU, menuScene);

        gameScene = new GameScene(this);
        sceneManager.registerScene(SceneType.GAME, gameScene);

        SettingsScene settingsScene = new SettingsScene(this);
        sceneManager.registerScene(SceneType.SETTINGS, settingsScene);

        // Overlays (für Menüs über dem Spiel) erstellen und registrieren
        PauseOverlay pauseOverlay = new PauseOverlay(this);
        sceneManager.registerOverlay(OverlayType.PAUSE, pauseOverlay);

        DifficultyOverlay difficultyOverlay = new DifficultyOverlay(this);
        sceneManager.registerOverlay(OverlayType.DIFFICULTY, difficultyOverlay);

        InfoOverlay infoOverlay = new InfoOverlay(this);
        sceneManager.registerOverlay(OverlayType.INFO, infoOverlay);

        WinOverlay winOverlay = new WinOverlay(this, OverlayType.WIN);
        sceneManager.registerOverlay(OverlayType.WIN, winOverlay);

        WinOverlay loseOverlay = new WinOverlay(this, OverlayType.LOSE);
        sceneManager.registerOverlay(OverlayType.LOSE, loseOverlay);

        shatteredGlassOverlay = new ShatteredGlassOverlay(this);
        sceneManager.registerOverlay(OverlayType.SHATTERED_GLASS, shatteredGlassOverlay);

        currentScene = SceneType.MENU; // Standard-Szene festlegen

        // ===== soundManager =====
        // Kurze Soundeffekte in den Speicher laden
        soundManager.loadSoundEffect("pong", "res/sounds/pong.wav");
        soundManager.loadSoundEffect("score", "res/sounds/score.wav");
        soundManager.loadSoundEffect("win", "res/sounds/win.wav");
        soundManager.loadSoundEffect("lose", "res/sounds/lose.wav");

        // Hintergrundmusik laden und sofort abspielen (Loopt standardmäßig)
        soundManager.loadBackgroundMusic("bg1", "res/musik/BgSong1.wav");
        soundManager.playBackgroundMusic("bg1"); 
    }

    // Konfiguriert das Fenster (z.B. keine Ränder, nicht vergrößerbar, Schließverhalten)
    private void setDefaultWindowOptions() {
        this.setUndecorated(true); // Versteckt die Windows-Titelleiste
        this.setResizable(false);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Beendet das Programm beim Schließen
    }

    // Startet das Spiel durch Wechsel zur Spielszene
    public void startGame() {
        setCurrentScene(SceneType.GAME);
    }

    // Setzt das Spiel zurück und kehrt zum Hauptmenü zurück
    public void returnToMenu() {
        // Versteckt alle aktiven Spiel-Overlays
        sceneManager.hideOverlay(OverlayType.PAUSE);
        sceneManager.hideOverlay(OverlayType.WIN);
        sceneManager.hideOverlay(OverlayType.LOSE);

        // Setzt Punktestand und Positionen zurück
        gameScene.reset();

        // Zeigt wieder das Hauptmenü an
        setCurrentScene(SceneType.MENU);
    }

    // ===== Overlay Methoden =====
    
    // Schaltet die Sichtbarkeit eines Overlays um (an/aus)
    public void toggleOverlay(OverlayType overlayID) {
        if (!sceneManager.isOverlayVisible(overlayID)) {
            sceneManager.showOverlay(overlayID);
        } else {
            sceneManager.hideOverlay(overlayID);
        }
    }

    // Zeigt oder versteckt ein bestimmtes Overlay explizit
    public void showOverlay(OverlayType overlayID, boolean show) {
        if (show) {
            sceneManager.showOverlay(overlayID); 
        } else {
            sceneManager.hideOverlay(overlayID); 
        }
    }

    // ===== Getter und Setter =====
    public void setCurrentScene(SceneType scene) {
        this.currentScene = scene;
        sceneManager.setScene(scene);
    }

    public Dimension getWindowSize() {
        return windowSize;
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
        return debug;
    }

    public void toggleDebug() {
        this.debug = !this.debug;
    }
}
