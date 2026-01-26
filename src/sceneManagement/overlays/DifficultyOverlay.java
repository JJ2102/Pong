package sceneManagement.overlays;

import enums.Difficulty;
import enums.EnumOverlays;
import sceneManagement.GameWindow;
import utility.Button;

import java.awt.event.KeyEvent;

public class DifficultyOverlay extends Overlay {
    public DifficultyOverlay(GameWindow window) {
        super(window, "Select Difficulty", 200, true);

        // Buttons
        Button easyBtn = new Button("Easy");
        Button mediumBtn = new Button("Medium");
        Button hardBtn = new Button("Hard");
        Button cancelBtn = new Button("Cancel");

        // Aktionen
        easyBtn.addActionListener(_ -> startGame(Difficulty.EASY));
        mediumBtn.addActionListener(_ -> startGame(Difficulty.MEDIUM));
        hardBtn.addActionListener(_ -> startGame(Difficulty.HARD));
        cancelBtn.addActionListener(_ -> window.toggleOverlay(EnumOverlays.DIFFICULTY));

        addComponent(easyBtn);
        addComponent(mediumBtn);
        addComponent(hardBtn);
        addComponent(cancelBtn);

        positionComponents();
    }

    private void startGame(Difficulty difficulty) {
        window.toggleOverlay(EnumOverlays.DIFFICULTY);
        window.getGameScene().setDifficulty(difficulty);
        window.startGame();
    }
}
