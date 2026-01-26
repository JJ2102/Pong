package sceneManagement.overlays;

import enums.EnumOverlays;
import sceneManagement.GameWindow;
import utility.Button;

public class WinOverlay extends Overlay {
    public WinOverlay(GameWindow window, String titel) {
        super(window, titel, 150);

        // Buttons
        Button playAgainBtn = new Button("Play Again");
        Button menuBtn = new Button("Back to Menu");

        // Aktionen
        playAgainBtn.addActionListener(_ -> {
            window.toggleOverlay(EnumOverlays.WIN, true);
            window.getGameScene().restart();
        });
        menuBtn.addActionListener(_ -> {
            window.toggleOverlay(EnumOverlays.WIN, true);
            window.returnToMenu();
            System.out.println(window.getGameScene().isRunning());
        });

        addComponent(playAgainBtn);
        addComponent(menuBtn);

        positionComponents();
    }
}
