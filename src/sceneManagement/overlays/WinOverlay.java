package sceneManagement.overlays;

import enums.EnumOverlays;
import sceneManagement.GameWindow;
import utility.Button;

public class WinOverlay extends Overlay {
    public WinOverlay(GameWindow window, EnumOverlays overlayType) {
        super(window, overlayType == EnumOverlays.LOSE ? "You Lose!" : "You Win!", 150, true);

        // Buttons
        Button playAgainBtn = new Button("Play Again");
        Button menuBtn = new Button("Back to Menu");

        // Aktionen
        playAgainBtn.addActionListener(_ -> {
            window.toggleOverlay(overlayType);
            window.getGameScene().restart();
        });
        menuBtn.addActionListener(_ -> {
            window.toggleOverlay(overlayType);
            window.returnToMenu();
        });

        addComponent(playAgainBtn);
        addComponent(menuBtn);

        positionComponents();
    }
}
