package sceneManagement.scenes;

import sceneManagement.GameWindow;
import utility.Button;
import utility.InfoLabel;
import utility.Slider;

import javax.swing.*;

public class SettingsScene extends ButtonScene {
    public SettingsScene(GameWindow window) {
        super(window, "Settings");

        int effectsVolume = (int) (window.getSoundManager().getSoundSettings().getEffectsVolume() * 100); // Effekte Lautstärke in %
        int musicVolume = (int) (window.getSoundManager().getSoundSettings().getMusicVolume() * 100); // Musik Lautstärke in %

        InfoLabel volumeEffectsLabel = new InfoLabel("Volume Effects:");
        Slider volumeEffectsSlider = new Slider(0, 100, effectsVolume);
        volumeEffectsSlider.addChangeListener(_ -> {
            float vol = volumeEffectsSlider.getValue() / 100f;
            window.getSoundManager().setEffectsVolume(vol);
            window.getSoundManager().playSoundEffekt("pong");
        });

        InfoLabel volumeMusicLabel = new InfoLabel("Volume Music:");
        Slider volumeMusicSlider = new Slider(0, 100, musicVolume);
        volumeMusicSlider.addChangeListener(_ -> {
            float vol = volumeMusicSlider.getValue() / 100f;
            window.getSoundManager().setMusicVolume(vol);
        });

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
