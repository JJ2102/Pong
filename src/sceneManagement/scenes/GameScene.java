package scenemanagement.scenes;

import enums.Difficulty;
import enums.OverlayType;
import hitboxes.BoxHitbox;
import math.Vector2;
import math.Vector3;
import objects.*;
import rendering.Camera;
import rendering.Renderer;
import scenemanagement.GameWindow;
import utility.Cooldown;
import utility.Countdown;
import utility.Globals;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

public class GameScene extends Scene {
    // Difficulty
    private Difficulty gameDifficulty = Difficulty.MEDIUM;

    // FPS
    private long lastFrameTime = System.currentTimeMillis();
    private int fps = 0;

    // Renderer
    private final Renderer renderer;
    private final Camera camera;

    // Objekte
    private final Player player;
    private final Enemy aiPlayer;
    private final Box box;
    private final Ball ball;

    private final SevenSegmentDisplay scoreDisplay;

    // Hitboxes
    private final BoxHitbox goalPlayerHitbox;
    private final BoxHitbox goalAIHitbox;

    // Hit Cooldown
    private final Cooldown hitCooldown;

    // Score
    private int playerScore = 0;
    private int aiScore = 0;
    private enum PlayerType { PLAYER, AI }
    private final int winningScore = 4;

    // Scoring
    private final Countdown countdown;
    private enum GameState { PLAYING, COUNTING_DOWN }
    private GameState gameState = GameState.COUNTING_DOWN;

    // Positionen
    private final double boxDepth = 1.5;
    private final double playerPositionZ = -boxDepth + 0.2;
    private Vector3 mousePosition = new Vector3(0, 0, playerPositionZ); // Aktuelle Mausposition

    public GameScene(GameWindow window) {
        super(window);
        setCursor(Globals.getInvisibleCursor());

        // Renderer initialisieren
        renderer = new Renderer(getWidth(), getHeight());
        camera = new Camera();
        double cameraPositionZ = -boxDepth - 1;
        camera.setPosition(new Vector3(0, 0, cameraPositionZ)); // Kamera hinter der Box platzieren

        // Box und Ball initialisieren
        box = new Box(boxDepth);
        ball = new Ball();

        // Score Display initialisieren
        scoreDisplay = new SevenSegmentDisplay();
        scoreDisplay.getTransform().setScale(new Vector3(0.5, 0.5, 0.5));
        scoreDisplay.getTransform().setPosition(new Vector3(box.getSize().x - 0.1, 0, -0.5));
        scoreDisplay.getTransform().setRotation(new Vector3(0, Math.toRadians(90), 0));

        // 3 Sekunden Countdown erstellen
        countdown = new Countdown(3000); // 3 Sekunden
        countdown.onTick(_ -> repaint());
        countdown.onFinish(() -> {
            gameState = GameState.PLAYING;
            window.showOverlay(OverlayType.SHATTERED_GLASS, false);
        });

        // Hit Cooldown initialisieren
        hitCooldown = new Cooldown(120); // 120 ms Cooldown


        // Hitboxes für Tore
        Vector3 boxSize = box.getSize();
        Vector3 hitboxSize = new Vector3(boxSize.x * 2, boxSize.y * 2, 0);
        goalPlayerHitbox = new BoxHitbox(new Vector3(0, 0, -boxDepth), hitboxSize);
        goalAIHitbox = new BoxHitbox(new Vector3(0, 0, boxDepth), hitboxSize);

        // Spieler-Panel an 0, 0 Initialisieren
        player = new Player(new Vector3(0, 0, playerPositionZ));

        // KI-Panel an spiegelverkehrte Position initialisieren
        Vector3 playerPosition = player.getTransform().getPosition();
        Vector3 aiPosition = new Vector3(playerPosition.x, playerPosition.y, -playerPositionZ);
        aiPlayer = new Enemy(aiPosition, box.getSize());
    }

    public void update() {
        if (window.isDebug()) calculateFPS();

        player.moveTo(mousePosition);
        // leichte Kamera bewegung basierend auf Mausposition, um mehr Dynamik zu erzeugen
        Vector3 targetCameraPosition = mousePosition.divide(15);
        Vector3 cameraPosition = camera.getPosition().lerp(targetCameraPosition, 0.5);
        camera.setPosition(new Vector3(cameraPosition.x, cameraPosition.y, camera.getPosition().z));

        // Nur Spieler kann sich während des Countdowns bewegen, Ball und KI pausieren
        if (gameState == GameState.COUNTING_DOWN) return;

        BoxHitbox[] paddleHitboxes = new BoxHitbox[]{player.getHitbox(), aiPlayer.getHitbox()};
        // Paddle kolision prüfen
        if (hitCooldown.isReady()) {
            if (ball.paddleHit(paddleHitboxes)) {
                window.getSoundManager().playSoundEffect("pong");
                hitCooldown.trigger();
            }
        }
        // Ball bewegen
        ball.move();

        // Tore prüfen
        if(goalPlayerHitbox.intersects(ball.getHitbox())) { // Punkt für KI
            addPoint(PlayerType.AI);
        }
        if(goalAIHitbox.intersects(ball.getHitbox())) { // Punkt für Spieler
            addPoint(PlayerType.PLAYER);
        }

        // KI bewegen
        aiPlayer.move(ball.getTransform().getPosition(), gameDifficulty.getValue());
    }

    private void addPoint(PlayerType scorer) {
        if(scorer == PlayerType.AI) {
            aiScore++;
            Vector2 ballScreenPosition = renderer.worldToScreen(ball.getTransform().getPosition(), camera);
            window.getShatteredGlassOverlay().generateShatter((int) ballScreenPosition.x, (int) ballScreenPosition.y, window.getWidth(), window.getHeight());
            window.showOverlay(OverlayType.SHATTERED_GLASS, true);
        } else {
            playerScore++;
        }
        window.getSoundManager().playSoundEffect("score");
        scoreDisplay.setScore(aiScore, playerScore);
        ball.reset();

        // Überprüfen, ob jemand gewonnen hat
        if (aiScore == winningScore || playerScore == winningScore) {
            if (aiScore == winningScore) {
                window.toggleOverlay(OverlayType.LOSE);
                window.getSoundManager().playSoundEffect("lose");
            } else {
                window.toggleOverlay(OverlayType.WIN);
                window.getSoundManager().playSoundEffect("win");
            }
            return;
        }
        aiPlayer.reset();

        // Countdown starten
        gameState = GameState.COUNTING_DOWN;
        countdown.restart();
    }

    public void reset() {
        playerScore = 0;
        aiScore = 0;
        scoreDisplay.setScore(aiScore, playerScore);
        ball.reset();
        aiPlayer.reset();
    }

    // Laufzeit
    @Override
    public void onPause() {
        if (getTimer().isRunning()) getTimer().stop();
        if (countdown.isRunning()) countdown.stop();
    }

    @Override
    public void onResume() {
        if (!getTimer().isRunning()) getTimer().start();
        if (gameState == GameState.COUNTING_DOWN && !countdown.isRunning()) {
            countdown.start();
        }
    }

    public void restart() {
        reset();
        gameState = GameState.COUNTING_DOWN;
        countdown.restart();
        onResume();
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        renderer.updateSize(getWidth(), getHeight());
        renderer.renderEntity(g2d, box, camera);
        renderer.renderEntity(g2d, aiPlayer, camera);
        renderer.renderEntity(g2d, scoreDisplay, camera);
        renderer.renderEntity(g2d, ball, camera);
        renderer.renderEntity(g2d, player, camera);

        if (window.isDebug()) {
            // Debug: Ball Hitbox zeichnen
            renderer.renderBoxHitbox(g2d, ball.getHitbox(), camera, Color.BLUE);
            renderer.renderBoxHitbox(g2d, aiPlayer.getHitbox(), camera, Color.YELLOW);
            renderer.renderBoxHitbox(g2d, goalAIHitbox, camera, Color.RED);
            renderer.renderBoxHitbox(g2d, player.getHitbox(), camera, Color.YELLOW);
            renderer.renderBoxHitbox(g2d, goalPlayerHitbox, camera, Color.RED);

            // FPS anzeigen
            g2d.setFont(new Font("Monospace", Font.BOLD, 20));
            g2d.setColor(Color.GREEN);
            g2d.drawString("FPS: " + fps, 10, 20); // Oben links
        }

        // Scores zeichnen
        g2d.setFont(Globals.getMainFont(36));
        g2d.setColor(Color.GREEN);
        String scoreText = playerScore + " : " + aiScore;
        FontMetrics fm = g2d.getFontMetrics(); // Font-Metriken holen
        int textWidth = fm.stringWidth(scoreText); // Breite des Score-Strings
        g2d.drawString(scoreText, (getWidth() - textWidth) / 2, 50);

        // Countdown anzeigen
        if (gameState == GameState.COUNTING_DOWN && countdown.isRunning()) {
            g2d.setFont(Globals.getMainFont(120));
            g2d.setColor(Globals.getFontColor(200));
            String text = String.valueOf(countdown.getRemainingSeconds());

            fm = g2d.getFontMetrics();
            textWidth = fm.stringWidth(text);
            g2d.drawString(text, (getWidth() - textWidth) / 2, getHeight() / 2);
        }
    }

    // ===== KeyListener Methoden =====
    @Override
    public void mouseMoved(MouseEvent e) {
        Vector2 mouseScreenPosition = new Vector2(e.getX(), e.getY());
        mousePosition = renderer.screenToWorld(mouseScreenPosition, playerPositionZ, camera);
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
            window.toggleOverlay(OverlayType.PAUSE);
        }
        if (e.getKeyCode() == KeyEvent.VK_F3) {
            window.toggleDebug();
        }
    }

    // ===== Getter und Setter =====
    public void setDifficulty(Difficulty difficulty) {
        this.gameDifficulty = difficulty;
    }

    // ===== Debug Methoden =====
    private void calculateFPS() {
        long currentTime = System.currentTimeMillis();
        long deltaTime = currentTime - lastFrameTime;
        lastFrameTime = currentTime;

        // FPS aus der Frame-Zeit berechnen
        if (deltaTime > 0) {
            fps = (int) (1000 / deltaTime);
        }
    }
}
