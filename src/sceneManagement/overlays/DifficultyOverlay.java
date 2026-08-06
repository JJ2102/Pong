package scenemanagement.overlays;

import enums.Difficulty;
import enums.OverlayType;
import scenemanagement.GameWindow;
import utility.Button;

public class DifficultyOverlay extends Overlay {
    public DifficultyOverlay(GameWindow window) {
        super(window, "Select Difficulty", 200, true);

        // Buttons
        Button easyButton = new Button("Easy");
        Button mediumButton = new Button("Medium");
        Button hardButton = new Button("Hard");
        Button cancelButton = new Button("Cancel");

        // Aktionen
        easyButton.addActionListener(_ -> startGame(Difficulty.EASY));
        mediumButton.addActionListener(_ -> startGame(Difficulty.MEDIUM));
        hardButton.addActionListener(_ -> startGame(Difficulty.HARD));
        cancelButton.addActionListener(_ -> window.toggleOverlay(OverlayType.DIFFICULTY));

        addComponent(easyButton);
        addComponent(mediumButton);
        addComponent(hardButton);
        addComponent(cancelButton);

        positionComponents();
    }

    private void startGame(Difficulty difficulty) {
        window.toggleOverlay(OverlayType.DIFFICULTY);
        window.getGameScene().setDifficulty(difficulty);
        window.startGame();
    }
}
