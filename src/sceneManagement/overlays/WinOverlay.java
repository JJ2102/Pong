package scenemanagement.overlays;

import enums.OverlayType;
import scenemanagement.GameWindow;
import utility.Button;

public class WinOverlay extends Overlay {
    public WinOverlay(GameWindow window, OverlayType overlayType) {
        super(window, overlayType == OverlayType.LOSE ? "You Lose!" : "You Win!", 150, true);

        // Buttons
        Button playAgainButton = new Button("Play Again");
        Button menuButton = new Button("Back to Menu");

        // Aktionen
        playAgainButton.addActionListener(_ -> {
            window.toggleOverlay(overlayType);
            window.getGameScene().restart();
        });
        menuButton.addActionListener(_ -> {
            window.toggleOverlay(overlayType);
            window.returnToMenu();
        });

        addComponent(playAgainButton);
        addComponent(menuButton);

        positionComponents();
    }
}
