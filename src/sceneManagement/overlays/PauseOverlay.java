package scenemanagement.overlays;

import enums.OverlayType;
import scenemanagement.GameWindow;
import utility.Button;

public class PauseOverlay extends Overlay {
    public PauseOverlay(GameWindow window) {
        super(window, "Paused", 150, true);

        // Buttons
        Button resumeButton = new Button("Resume");
        Button menuButton = new Button("Back to Menu");
        Button quitButton = new Button("Quit Game");

        // Aktionen
        resumeButton.addActionListener(_ -> window.toggleOverlay(OverlayType.PAUSE));
        menuButton.addActionListener(_ -> {
            window.toggleOverlay(OverlayType.PAUSE);
            window.returnToMenu();
        });
        quitButton.addActionListener(_ -> System.exit(0));

        addComponent(resumeButton);
        addComponent(menuButton);
        addComponent(quitButton);

        positionComponents();
    }
}
