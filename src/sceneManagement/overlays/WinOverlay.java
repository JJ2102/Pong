package sceneManagement.overlays;

import enums.EnumOverlays;
import sceneManagement.GameWindow;
import utility.Button;

public class WinOverlay extends Overlay {
    EnumOverlays overlayType = EnumOverlays.WIN;

    public WinOverlay(GameWindow window, EnumOverlays overlayType) {
        super(window, "NULL", 150);

        this.overlayType = overlayType;
        if (overlayType == EnumOverlays.LOSE) {
            setTitle("You Lose!");
        } else {
            setTitle("You Win!");
        }

        // Buttons
        Button playAgainBtn = new Button("Play Again");
        Button menuBtn = new Button("Back to Menu");

        // Aktionen
        playAgainBtn.addActionListener(_ -> {
            window.toggleOverlay(overlayType, true);
            window.getGameScene().restart();
        });
        menuBtn.addActionListener(_ -> {
            window.toggleOverlay(overlayType, true);
            window.returnToMenu();
            System.out.println(window.getGameScene().isRunning());
        });

        addComponent(playAgainBtn);
        addComponent(menuBtn);

        positionComponents();
    }
}
