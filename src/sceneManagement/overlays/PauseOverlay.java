package sceneManagement.overlays;

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
        resumeBtn.addActionListener(_ -> window.togglePauseOverlay());
        menuBtn.addActionListener(_ -> {
            window.togglePauseOverlay();
            window.returnToMenu();
        });
        quitBtn.addActionListener(_ -> System.exit(0));

        addButton(resumeBtn);
        addButton(menuBtn);
        addButton(quitBtn);

        positionComponents();
    }
}
