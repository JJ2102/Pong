package scenes;

import math.Vektor3;
import math.Vertex;
import objekts.*;
import objekts.Panel;
import rendering.Camera;
import rendering.Renderer;
import utility.MouseSettings;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

public class GameScene extends Scene {
    // Renderer
    private Renderer renderer;
    private Camera camera;

    // Objekte
    private Player player;
    private Enemy aiPlayer;
    private Box box;
    private Ball ball;

    // Positionen
    final double playerPosZ = -2.8;
    private Vektor3 mousePos = new Vektor3(0,0,playerPosZ); // Aktuelle Mausposition

    public GameScene(GameWindow window) {
        super(window);
    }

    @Override
    protected void initScene() {
        setCursor(MouseSettings.getInvisibleCursor());

        // Renderer initialisieren
        renderer = new Renderer(getWidth(), getHeight());
        camera = new Camera();
        camera.setPosition(new Vektor3(0, 0, -4));

        // Spieler-Panel an 0, 0 Initialisieren
        player = new Player(new Vektor3(0,0,playerPosZ));

        // KI-Panel an spiegelverkehrte Position initialisieren
        Vektor3 playerPos = player.getTransform().position;
        Vektor3 aiPos = new Vektor3(playerPos.x, playerPos.y, -playerPosZ);
        aiPlayer = new Enemy(aiPos);

        // Box und Ball initialisieren
        box = new Box();
        ball = new Ball();
    }

    public void update() {
        ball.move();
        player.moveTo(mousePos);

        aiPlayer.move(ball.getTransform().position);
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        renderer.updateSize(getWidth(), getHeight());
        renderer.renderEntity(g2d, box, camera);
        renderer.renderEntity(g2d, aiPlayer, camera);
        renderer.renderEntity(g2d, ball, camera);
        renderer.renderEntity(g2d, player, camera);
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
    }
}
