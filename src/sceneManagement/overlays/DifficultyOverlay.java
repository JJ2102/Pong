package scenemanagement.overlays;

import enums.Difficulty;
import enums.OverlayType;
import scenemanagement.GameWindow;
import utility.Button;

// Overlay zur Auswahl des Schwierigkeitsgrads, bevor eine Runde gestartet wird
public class DifficultyOverlay extends Overlay {
    // Erstellt die drei Schwierigkeits-Buttons sowie einen Abbrechen-Button
    public DifficultyOverlay(GameWindow window) {
        super(window, "Select Difficulty", 200, true); // pausiert die Szene darunter

        // Buttons
        Button easyButton = new Button("Easy");
        Button mediumButton = new Button("Medium");
        Button hardButton = new Button("Hard");
        Button cancelButton = new Button("Cancel");

        // Aktionen
        // Jeder Button startet das Spiel mit dem passenden Schwierigkeitsgrad
        easyButton.addActionListener(_ -> startGame(Difficulty.EASY));
        mediumButton.addActionListener(_ -> startGame(Difficulty.MEDIUM));
        hardButton.addActionListener(_ -> startGame(Difficulty.HARD));
        cancelButton.addActionListener(_ -> window.toggleOverlay(OverlayType.DIFFICULTY)); // blendet das Overlay wieder aus

        addComponent(easyButton);
        addComponent(mediumButton);
        addComponent(hardButton);
        addComponent(cancelButton);

        positionComponents(); // ordnet Titel und Buttons untereinander an
    }

    // Schließt das Overlay, übergibt den gewählten Schwierigkeitsgrad an die Spielszene und startet das Spiel
    private void startGame(Difficulty difficulty) {
        window.toggleOverlay(OverlayType.DIFFICULTY);
        window.getGameScene().setDifficulty(difficulty);
        window.startGame();
    }
}
