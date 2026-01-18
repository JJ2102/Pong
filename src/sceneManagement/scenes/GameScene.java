package sceneManagement.scenes;

import enums.Difficulty;
import hitboxes.BoxHitbox;
import math.Vektor3;
import math.Vertex;
import objekts.*;
import objekts.SevenSegmentDisplay;
import rendering.Camera;
import rendering.Renderer;
import sceneManagement.GameWindow;
import utility.MouseSettings;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

public class GameScene extends Scene {
    // Difficulty
    Difficulty gameDifficulty = Difficulty.MEDIUM;

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

    // Score
    public int playerScore = 0;
    public int aiScore = 0;
    private enum PlayerType { PLAYER, AI }

    // Positionen
    private final double boxDepth = 1.5;
    private final double playerPosZ = -boxDepth + 0.2;
    double cameraPosZ = -boxDepth - 1;
    private Vektor3 mousePos = new Vektor3(0,0,playerPosZ); // Aktuelle Mausposition

    public GameScene(GameWindow window) {
        super(window);
        setCursor(MouseSettings.getInvisibleCursor());

        // Renderer initialisieren
        renderer = new Renderer(getWidth(), getHeight());
        camera = new Camera();
        camera.setPosition(new Vektor3(0, 0, cameraPosZ)); // 0, 0, cameraPosZ

        // Box und Ball initialisieren
        box = new Box(boxDepth);
        ball = new Ball();

        // Score Display initialisieren
        scoreDisplay = new SevenSegmentDisplay();
        scoreDisplay.getTransform().scale = new Vektor3(0.5, 0.5, 0.5);
        scoreDisplay.getTransform().position = new Vektor3(box.getSize().x - 0.1, 0, -0.5);
        scoreDisplay.getTransform().rotation = new Vektor3(0, Math.toRadians(90), 0);

        // Hitboxes für Tore
        Vektor3 boxSize = box.getSize();
        Vektor3 hitboxSize = new Vektor3(boxSize.x * 2, boxSize.y * 2, 0);
        goalPlayerHitbox = new BoxHitbox(new Vektor3(0, 0, -boxDepth), hitboxSize);
        goalAIHitbox = new BoxHitbox(new Vektor3(0, 0, boxDepth), hitboxSize);

        // Spieler-Panel an 0, 0 Initialisieren
        player = new Player(new Vektor3(0,0,playerPosZ));

        // KI-Panel an spiegelverkehrte Position initialisieren
        Vektor3 playerPos = player.getTransform().position;
        Vektor3 aiPos = new Vektor3(playerPos.x, playerPos.y, -playerPosZ);
        aiPlayer = new Enemy(aiPos, box.getSize());
    }

    public void update() {
        calculateFPS();

        player.moveTo(mousePos);
        Vektor3 targetCameraPos = mousePos.divide(15);
        Vektor3 cameraPos = camera.getPosition().lerp(targetCameraPos, 0.5);
        camera.setPosition(new Vektor3(cameraPos.x, cameraPos.y, cameraPosZ));

        BoxHitbox[] paddleHitboxes = new BoxHitbox[]{player.getHitbox(), aiPlayer.getHitbox()};
        // Ball bewegen
        if (ball.paddleHit(paddleHitboxes)) { // ToDo: seitliche Überschneidung führt zu glitch fixen
            window.getSoundManager().playSoundEffekt("pong");
        }
        ball.move();

        // Tore prüfen
        if(goalPlayerHitbox.intersects(ball.getHitbox())) { // Punkt für KI
            addPoint(PlayerType.AI);
        }
        if(goalAIHitbox.intersects(ball.getHitbox())) { // Punkt für Spieler
            addPoint(PlayerType.PLAYER);
        }

        aiPlayer.move(ball.getTransform().position, gameDifficulty.getValue());
    }

    private void addPoint(PlayerType scorer) {
        //TODO: Spielende bei 9 Punkten
        if(scorer == PlayerType.AI) {
            aiScore++;
        } else {
            playerScore++;
        }
        scoreDisplay.setScore(aiScore, playerScore);

        // Überprüfen ob jemand gewonnen hat
        if (aiScore == 9) {
            // TODO: Lose Screen
            window.getSoundManager().playSoundEffekt("lose");
            window.returnToMenu();
        } else if (playerScore == 9) {
            // TODO: Win Screen
            window.getSoundManager().playSoundEffekt("win");
            window.returnToMenu();
        } else {
            window.getSoundManager().playSoundEffekt("score");
            ball.reset();
        }
    }

    public void reset() {
        playerScore = 0;
        aiScore = 0;
        scoreDisplay.setScore(aiScore, playerScore);
        ball.reset();
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
            renderer.renderBoxHitbox(g2d, ball.getHitbox(), camera, Color.YELLOW);
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
        g2d.setFont(new Font("Arial", Font.BOLD, 36));
        g2d.setColor(Color.WHITE);
        String scoreText = playerScore + " : " + aiScore;
        FontMetrics fm = g2d.getFontMetrics(); // Font-Metriken holen
        int textWidth = fm.stringWidth(scoreText); // Breite des Score-Strings
        g2d.drawString(scoreText, (getWidth() - textWidth) / 2, 50);
    }

    // ===== KeyListener Methoden =====
    @Override
    public void mouseMoved(MouseEvent e) {
        Vertex mouseScreenPos = new Vertex(e.getX(), e.getY());
        mousePos = renderer.screenToWorld(mouseScreenPos, playerPosZ, camera);
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
            window.togglePauseOverlay();
        }
        if (e.getKeyCode() == KeyEvent.VK_F3) {
            window.toggleDebug();
        }
    }

    // ===== Getter und Setter =====
    public void setDifficulty(Difficulty difficulty) {
        this.gameDifficulty = difficulty;
    }

    public boolean isRunning() {
        return timer != null && timer.isRunning();
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
