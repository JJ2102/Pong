package sceneManagement.overlays;

import enums.EnumOverlays;
import sceneManagement.GameWindow;
import utility.Button;

public class PauseOverlay extends Overlay {
    public PauseOverlay(GameWindow window) {
        super(window, "Paused", 150);

        // Buttons
        Button resumeBtn = new Button("Resume");
        Button menuBtn = new Button("Back to Menu");
        Button quitBtn = new Button("Quit Game");

        // Aktionen
        resumeBtn.addActionListener(_ -> window.toggleOverlay(EnumOverlays.PAUSE, true));
        menuBtn.addActionListener(_ -> {
            window.toggleOverlay(EnumOverlays.PAUSE, true);
            window.returnToMenu();
        });
        quitBtn.addActionListener(_ -> System.exit(0));

        addComponent(resumeBtn);
        addComponent(menuBtn);
        addComponent(quitBtn);

        positionComponents();
    }
}
