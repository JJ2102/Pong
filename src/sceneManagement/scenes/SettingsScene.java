package sceneManagement.scenes;

import sceneManagement.GameWindow;
import utility.Button;
import utility.InfoLabel;
import utility.Slider;

import javax.swing.*;

public class SettingsScene extends ButtonScene {
    public SettingsScene(GameWindow window) {
        super(window, "Settings");

        InfoLabel volumeEffectsLabel = new InfoLabel("Volume Effects:");
        Slider volumeEffectsSlider = new Slider(0, 100, 50);

        InfoLabel volumeMusicLabel = new InfoLabel("Volume Music:");
        Slider volumeMusicSlider = new Slider(0, 100, 50);

        Button exitButton = new Button("Back to Menu");

        exitButton.addActionListener(_ -> window.returnToMenu());

        addComponent(volumeEffectsLabel);
        addComponent(volumeEffectsSlider);

        addComponent(volumeMusicLabel);
        addComponent(volumeMusicSlider);

        addComponent(exitButton);

        positionComponents();
    }
}
