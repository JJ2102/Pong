package scenes;

import enums.Difficulty;
import hitboxes.BoxHitbox;
import math.Vektor3;
import math.Vertex;
import objekts.*;
import rendering.Camera;
import rendering.Renderer;
import utility.MouseSettings;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

public class GameScene extends Scene {
    // Difficulty
    Difficulty gameDifficulty = Difficulty.MEDIUM;

    // Debug
    public static boolean DEBUG_MODE = false;

    // FPS
    private long lastFrameTime = System.currentTimeMillis();
    private int fps = 0;

    // Renderer
    private Renderer renderer;
    private Camera camera;

    // Objekte
    private Player player;
    private Enemy aiPlayer;
    private Box box;

    private Ball ball;

    // Score
    public int playerScore = 0;
    public int aiScore = 0;

    // Positionen
    double playerPosZ;
    double cameraPosZ;
    private Vektor3 mousePos = new Vektor3(0,0,playerPosZ); // Aktuelle Mausposition

    public GameScene(GameWindow window) {
        super(window);
    }

    @Override
    protected void initScene() {
        setCursor(MouseSettings.getInvisibleCursor());

        double boxDepth = 1.5;
        cameraPosZ = -boxDepth - 1;
        playerPosZ = -boxDepth + 0.2;

        // Renderer initialisieren
        renderer = new Renderer(getWidth(), getHeight());
        camera = new Camera();
        camera.setPosition(new Vektor3(0, 0, cameraPosZ)); // 0, 0, -4

        // Box und Ball initialisieren
        box = new Box(boxDepth);
        ball = new Ball();

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
        ball.paddleHit(paddleHitboxes); // ToDo: seitliche Überschneidung führt zu glitch fixen
        ball.move();

        // Todo: Mit Hitbox erstätzen
        if(ball.getTransform().position.z < playerPosZ - ball.getRadius()) {
            // Punkt für die KI
            addPointToAI();
            ball.reset();
        } else if(ball.getTransform().position.z > -playerPosZ + ball.getRadius()) {
            // Punkt für den Spieler
            addPointToPlayer();
            ball.reset();
        }

        aiPlayer.move(ball.getTransform().position, gameDifficulty.getValue());
    }

    public void addPointToPlayer() {
        playerScore++;
    }

    public void addPointToAI() {
        aiScore++;
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        renderer.updateSize(getWidth(), getHeight());
        renderer.renderEntity(g2d, box, camera);
        renderer.renderEntity(g2d, aiPlayer, camera);
        renderer.renderEntity(g2d, ball, camera);
        renderer.renderEntity(g2d, player, camera);

        if (DEBUG_MODE) {
            // Debug: Ball Hitbox zeichnen
            renderer.renderBoxHitbox(g2d, ball.getHitbox(), camera, Color.YELLOW);
            renderer.renderBoxHitbox(g2d, aiPlayer.getHitbox(), camera, Color.YELLOW);
            renderer.renderBoxHitbox(g2d, player.getHitbox(), camera, Color.YELLOW);

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
            DEBUG_MODE = !DEBUG_MODE;
        }
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
