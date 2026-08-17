package sound;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

// Lädt und verwaltet alle Soundeffekte und die Hintergrundmusik des Spiels
public class SoundManager {
    // Name -> Soundeffekt-Clip; Map aller Soundeffekte
    private final Map<String, Clip> soundEffects = new HashMap<>();
    // Name -> HintergrundMusik-Clip; Map aller Musik Clips
    private final Map<String, Clip> backgroundMusic = new HashMap<>();

    private final SoundSettings settings;

    private Clip currentBackgroundMusic; // aktuelle HintergrundMusik

    public SoundManager() {
        settings = new SoundSettings();
    }

    // erstellt einen Clip aus einer Audiodatei
    private Clip createClip(String path) {
        File file = new File(path); // holt sich das File zum Path
        if (!file.exists()) {
            throw new IllegalArgumentException("Sound: File Not Found: " + path);
        }

        try {
            AudioInputStream sound = AudioSystem.getAudioInputStream(file); // Audiodatei laden
            Clip clip = AudioSystem.getClip(); // Clip erstellen
            clip.open(sound); // Clip mit Audiodaten füllen
            return clip;
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            throw new IllegalStateException("Sound: Error loading file: " + path, e);
        }
    }

    // ===== Volume =====
    // setzt die Lautstärke für einen Clip
    private void setVolume(Clip clip, float volume) {
        if (clip == null) {
            return;
        }
        try {
            // Lautstärkeregler holen
            FloatControl gain = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
            float decibel = volumeToDecibel(volume, gain.getMinimum(), gain.getMaximum());
            gain.setValue(decibel); // Lautstärke setzen
        } catch (IllegalArgumentException ignored) {
            // MASTER_GAIN nicht verfügbar ⇾ nichts tun
        }
    }

    // Konvertiert das Prozentuale Volumen in Decibel
    private float volumeToDecibel(float volume, float min, float max) {
        float decibel = (float) (20.0 * Math.log10(volume)); // dB-Wert berechnen
        return Math.clamp(decibel, min, max); // dB-Wert begrenzen
    }

    // ===== Soundeffekte =====
    // Soundeffekt laden
    public void loadSoundEffect(String name, String path) {
        Clip clip = createClip(path); // clip erstellen
        setVolume(clip, settings.getEffectsVolume()); // Lautstärke auf Einstellungen setzen
        soundEffects.put(name, clip); // Clip in die HashMap speichern
    }

    // Soundeffekt abspielen
    public void playSoundEffect(String name) {
        Clip clip = soundEffects.get(name); // clip aus der soundeffekt Map holen
        if (clip == null) { // falls clip nicht existiert abbrechen
            return;
        }

        // Pausiere, den Clip, falls er bereits läuft, um Überlappungen zu vermeiden
        if (clip.isRunning()) {
            clip.stop();
        }

        setVolume(clip, settings.getEffectsVolume()); // Lautstärke auf Einstellungen setzen
        clip.setFramePosition(0); // Clip von vorne beginnen
        clip.start(); // abspielen
    }

    // ===== Hintergrundmusik =====
    // Hintergrundmusik laden
    public void loadBackgroundMusic(String name, String path) {
        Clip clip = createClip(path); // clip erstellen
        setVolume(clip, settings.getMusicVolume()); // Lautstärke auf Einstellungen setzen
        backgroundMusic.put(name, clip); // Clip in die HashMap speichern
    }

    // Hintergrundmusik abspielen
    public void playBackgroundMusic(String name) {
        Clip clip = backgroundMusic.get(name); // Clip laden
        if (clip == null) { // falls clip nicht existiert abbrechen
            return;
        }

        // Stoppe und schließe aktuelle Musik sauber
        stopBackgroundMusic();

        setVolume(clip, settings.getMusicVolume()); // Lautstärke auf Einstellungen setzen
        clip.setFramePosition(0); // Setze die Position auf den Anfang
        clip.loop(Clip.LOOP_CONTINUOUSLY); // Schleife die Musik endlos
        clip.start(); // Starte die Musik
        currentBackgroundMusic = clip; // Setze die aktuelle Hintergrundmusik
    }

    // Hintergrundmusik stoppen
    public void stopBackgroundMusic() {
        if (currentBackgroundMusic != null) { // falls musik läuft
            if (currentBackgroundMusic.isRunning()) {
                currentBackgroundMusic.stop(); // stoppe die Musik
            }
            currentBackgroundMusic.close(); // schließe den clip sauber
            currentBackgroundMusic = null; // Setze aktuelle Hintergrundmusik auf null
        }
    }

    // ===== Hilfsmethoden =====
    // geht alle Clips durch und setzt die Lautstärke auf die aktuellen Einstellungen
    public void applyVolumes() {
        for (Clip clip : soundEffects.values()) {
            setVolume(clip, settings.getEffectsVolume());
        }
        for (Clip clip : backgroundMusic.values()) {
            setVolume(clip, settings.getMusicVolume());
        }
        if (currentBackgroundMusic != null) {
            setVolume(currentBackgroundMusic, settings.getMusicVolume());
        }
    }

    // ===== Getter und Setter =====
    // setzt die Lautstärke für Soundeffekte
    public void setEffectsVolume(float volume) {
        settings.setEffectsVolume(volume);
        applyVolumes();
    }

    // setzt die Lautstärke für die Hintergrundmusik
    public void setMusicVolume(float volume) {
        settings.setMusicVolume(volume);
        applyVolumes();
    }

    public SoundSettings getSoundSettings() {
        return settings;
    }
}
