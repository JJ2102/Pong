package sceneManagement.overlays;

import sceneManagement.GameWindow;
import utility.Globals;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

public class Overlay extends JPanel implements KeyListener {
    //TODO: make it possible to close overlay by pressing esc
    protected final GameWindow window;
    private final ArrayList<Component> components = new ArrayList<>();
    private final int transparency;
    protected JLabel titleLabel;
    private Color bgColor = Globals.getBackgroundColor();

    public Overlay(GameWindow window, String title, int transparency) {
        this.window = window;
        setOpaque(false); // Hintergrund transparent
        setLayout(new GridBagLayout()); // Zentrierte Inhalte

        this.transparency = Math.max(0, Math.min(transparency, 255)); // Transparenzwert begrenzen

        titleLabel = new JLabel(title);
        titleLabel.setFont(Globals.getMainFont(72));
        titleLabel.setForeground(Globals.getFontColor());
        components.add(titleLabel);
    }

    protected void setTitle(String title) {
        titleLabel.setText(title);
    }

    protected void addComponent(Component component) {
        components.add(component);
    }

    protected void positionComponents() {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(10, 0, 10, 0);

        int yPos = 0;
        for (Component component : components) {
            gbc.gridy = yPos++;
            add(component, gbc);
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        // Halbtransparenter Hintergrund
        g.setColor(new Color(bgColor.getRed(), bgColor.getGreen(), bgColor.getBlue(), transparency));
        g.fillRect(0, 0, getWidth(), getHeight());
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {

    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}
