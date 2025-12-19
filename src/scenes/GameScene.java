package scenes;

import math.Vektor3;
import meshes.Cube;
import objekts.Box;
import objekts.Entity;
import objekts.Panel;
import rendering.Mesh;
import rendering.Renderer;
import utility.MouseSettings;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

public class GameScene extends Scene {
    // Renderer
    private Renderer renderer;

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
        Dimension size = window.getSize();
        renderer = new Renderer(size.width, size.height);

        // Objekte initialisieren
        player = new Panel(new Vektor3(size.getWidth()/2, size.getHeight()/2, 0));
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
        renderer.renderEntity(g2d, box);
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
