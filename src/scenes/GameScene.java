package scenes;

import enums.Scenes;
import math.Vektor3;
import objekts.GameWindow;
import objekts.Panel;
import utility.MouseSettings;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

public class GameScene extends Scene {
    private Panel player;
    private Vektor3 mousePos = new Vektor3(0,0,0); // Aktuelle Mausposition

    public GameScene(GameWindow window) {
        super(window);
        startScene();
    }

    @Override
    protected void initScene() {
        player = new Panel(new Vektor3(100, 100, 0));
        setCursor(MouseSettings.getInvisibleCursor());
    }

    public void update() {
        player.setPosition(mousePos);
    }

    public void paintComponent(java.awt.Graphics g) {
        super.paintComponent(g);
        player.paintMe(g);
    }

    // ===== KeyListener Methoden =====
    @Override
    public void mouseMoved(MouseEvent e) {
        double mouseShift = player.getSIZE() / 2.0;
        mousePos = new Vektor3(e.getX() - mouseShift, e.getY() - mouseShift, 0);
        player.setPosition(mousePos);
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
            window.setCurrentScene(Scenes.MENU);
        }
    }
}
