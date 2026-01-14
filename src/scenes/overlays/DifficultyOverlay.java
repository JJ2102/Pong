package scenes.overlays;

import enums.Difficulty;
import enums.EnumScenes;
import scenes.GameWindow;
import utility.Button;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class DifficultyOverlay extends JPanel implements KeyListener {
    private final GameWindow window;

    public DifficultyOverlay(GameWindow window) {
        this.window = window;
        setOpaque(false); // Hintergrund transparent
        setLayout(new GridBagLayout()); // Zentrierte Inhalte

        // Titel-Label
        JLabel title = new JLabel("Select Difficulty");
        title.setFont(new Font("Arial", Font.BOLD, 72));
        title.setForeground(Color.WHITE);

        // Buttons
        Button easyBtn = new Button("Easy");
        Button mediumBtn = new Button("Medium");
        Button hardBtn = new Button("Hard");
        Button cancelBtn = new Button("Cancel");

        // Aktionen
        easyBtn.addActionListener(_ -> startGame(Difficulty.EASY));
        mediumBtn.addActionListener(_ -> startGame(Difficulty.MEDIUM));
        hardBtn.addActionListener(_ -> startGame(Difficulty.HARD));

        cancelBtn.addActionListener(_ -> window.toggleDifficultyOverlay());

        // Layout mit GridBagConstraints
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(10, 0, 10, 0);

        gbc.gridy = 0;
        add(title, gbc);

        gbc.gridy = 1;
        add(easyBtn, gbc);

        gbc.gridy = 2;
        add(mediumBtn, gbc);

        gbc.gridy = 3;
        add(hardBtn, gbc);

        gbc.gridy = 4;
        add(cancelBtn, gbc);
    }

    private void startGame(Difficulty difficulty) {
        window.toggleDifficultyOverlay();
        window.getGameScene().setDifficulty(difficulty);
        window.setCurrentScene(EnumScenes.GAME);
        window.getGameScene().startScene();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        // Halbtransparenter Hintergrund
        g.setColor(new Color(0, 0, 0, 200));
        g.fillRect(0, 0, getWidth(), getHeight());
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
            window.toggleDifficultyOverlay();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}
