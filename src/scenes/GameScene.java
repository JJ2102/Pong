package scenes;

import math.Vektor3;
import objekts.Box;
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
    private Panel player;
    private Box box;
    private Vektor3 mousePos = new Vektor3(0,0,0); // Aktuelle Mausposition

    public GameScene(GameWindow window) {
        super(window);
        startScene();
    }

    @Override
    protected void initScene() {
        setCursor(MouseSettings.getInvisibleCursor());

        // Renderer initialisieren
        renderer = new Renderer(getWidth(), getHeight());
        camera = new Camera();
        camera.setPosition(new Vektor3(0, 0, -5));

        // Objekte initialisieren
        player = new Panel(new Vektor3((double) getWidth() /2, (double) getHeight() /2, 0));
        box = new Box();
    }

    public void update() {
        if(window.isPauseActive()) return; // Pausieren, wenn Overlay aktiv ist

        player.setPosition(mousePos);
        repaint();
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        renderer.updateSize(getWidth(), getHeight());
        renderer.renderEntity(g2d, box, camera);
        player.paintMe(g2d);
    }

    // ===== KeyListener Methoden =====
    @Override
    public void mouseMoved(MouseEvent e) {
        double mouseShift = player.getSIZE() / 2.0;
        mousePos = new Vektor3(e.getX() - mouseShift, e.getY() - mouseShift, 0);
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
            window.togglePauseOverlay();
        }
    }
}
