package sceneManagement.overlays;

import enums.Difficulty;
import sceneManagement.GameWindow;
import utility.Button;

import java.awt.event.KeyEvent;

public class DifficultyOverlay extends Overlay {
    public DifficultyOverlay(GameWindow window) {
        super(window, "Select Difficulty", 200);

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

        addButton(easyBtn);
        addButton(mediumBtn);
        addButton(hardBtn);
        addButton(cancelBtn);

        positionComponents();
    }

    private void startGame(Difficulty difficulty) {
        window.toggleDifficultyOverlay();
        window.getGameScene().setDifficulty(difficulty);
        window.startGame();
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
            System.out.println("Difficulty selection canceled.");
            window.toggleDifficultyOverlay();
        }
    }
}
