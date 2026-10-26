package sound;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * Lädt und verwaltet alle Soundeffekte und die Hintergrundmusik des Spiels.
 * Die Lautstärken werden aus den zugehörigen {@link SoundSettings} übernommen.
 */
public class SoundManager {
    // Name → Soundeffekt-Clip
    private final Map<String, Clip> soundEffects = new HashMap<>();
    // Name → Hintergrundmusik-Clip
    private final Map<String, Clip> backgroundMusic = new HashMap<>();

    private final SoundSettings settings;

    private Clip currentBackgroundMusic; // Aktuell laufende Hintergrundmusik oder null

    /**
     * Erstellt einen SoundManager mit Standard-Lautstärkeeinstellungen.
     */
    public SoundManager() {
        settings = new SoundSettings();
    }

    /**
     * Erstellt einen geöffneten Clip aus einer Audiodatei im Classpath.
     *
     * @param resourcePath Pfad der Audiodatei im Classpath (z. B. {@code "/sounds/pong.wav"})
     * @return geöffneter Clip mit den geladenen Audiodaten
     * @throws IllegalArgumentException wenn die Ressource nicht gefunden wird
     * @throws IllegalStateException wenn die Audiodatei nicht gelesen oder kein Clip geöffnet werden kann
     */
    private Clip createClip(String resourcePath) {
        InputStream stream = SoundManager.class.getResourceAsStream(resourcePath);
        if (stream == null) {
            throw new IllegalArgumentException("Sound nicht gefunden: " + resourcePath);
        }

        // AudioSystem braucht mark/reset → Stream puffern, sonst IOException bei Jar-Streams
        try (InputStream buffered = new BufferedInputStream(stream)) {
            AudioInputStream sound = AudioSystem.getAudioInputStream(buffered);
            Clip clip = AudioSystem.getClip();
            clip.open(sound);
            return clip;
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            throw new IllegalStateException("Sound konnte nicht geladen werden: " + resourcePath, e);
        }
    }

    // ===== Lautstärke =====

    /**
     * Setzt die Lautstärke für einen Clip.
     * <p>
     * Ist der Clip {@code null} oder unterstützt er keine {@code MASTER_GAIN}-Steuerung, passiert nichts.
     *
     * @param clip Clip, dessen Lautstärke gesetzt werden soll
     * @param volume Lautstärke als Anteil von 0.0 bis 1.0
     */
    private void setVolume(Clip clip, float volume) {
        if (clip == null) {
            return;
        }
        try {
            FloatControl gain = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
            float decibel = volumeToDecibel(volume, gain.getMinimum(), gain.getMaximum());
            gain.setValue(decibel);
        } catch (IllegalArgumentException ignored) {
            // MASTER_GAIN nicht verfügbar → nichts tun
        }
    }

    /**
     * Konvertiert eine prozentuale Lautstärke in Dezibel.
     *
     * @param volume Lautstärke als Anteil von 0.0 bis 1.0
     * @param min kleinster zulässiger Dezibelwert
     * @param max größter zulässiger Dezibelwert
     * @return Dezibelwert, begrenzt auf den Bereich [{@code min}, {@code max}]
     */
    private float volumeToDecibel(float volume, float min, float max) {
        float decibel = (float) (20.0 * Math.log10(volume));
        return Math.clamp(decibel, min, max);
    }

    // ===== Soundeffekte =====

    /**
     * Lädt einen Soundeffekt und speichert ihn unter dem angegebenen Namen.
     *
     * @param name Name, unter dem der Soundeffekt später abgespielt wird
     * @param path Pfad der Audiodatei im Classpath
     * @throws IllegalArgumentException wenn die Ressource nicht gefunden wird
     * @throws IllegalStateException wenn die Audiodatei nicht geladen werden kann
     */
    public void loadSoundEffect(String name, String path) {
        Clip clip = createClip(path);
        setVolume(clip, settings.getEffectsVolume());
        soundEffects.put(name, clip);
    }

    /**
     * Spielt einen zuvor geladenen Soundeffekt von vorne ab.
     * <p>
     * Läuft der Effekt bereits, wird er gestoppt und neu gestartet. Unbekannte Namen werden ignoriert.
     *
     * @param name Name des Soundeffekts
     */
    public void playSoundEffect(String name) {
        Clip clip = soundEffects.get(name);
        if (clip == null) {
            return;
        }

        // Bereits laufenden Clip stoppen, um Überlappungen zu vermeiden
        if (clip.isRunning()) {
            clip.stop();
        }

        setVolume(clip, settings.getEffectsVolume());
        clip.setFramePosition(0); // Clip von vorne beginnen
        clip.start();
    }

    // ===== Hintergrundmusik =====

    /**
     * Lädt eine Hintergrundmusik und speichert sie unter dem angegebenen Namen.
     *
     * @param name Name, unter dem die Musik später abgespielt wird
     * @param path Pfad der Audiodatei im Classpath
     * @throws IllegalArgumentException wenn die Ressource nicht gefunden wird
     * @throws IllegalStateException wenn die Audiodatei nicht geladen werden kann
     */
    public void loadBackgroundMusic(String name, String path) {
        Clip clip = createClip(path);
        setVolume(clip, settings.getMusicVolume());
        backgroundMusic.put(name, clip);
    }

    /**
     * Spielt eine zuvor geladene Hintergrundmusik in Endlosschleife ab.
     * <p>
     * Eine bereits laufende Hintergrundmusik wird vorher gestoppt und geschlossen.
     * Unbekannte Namen werden ignoriert.
     *
     * @param name Name der Hintergrundmusik
     */
    public void playBackgroundMusic(String name) {
        Clip clip = backgroundMusic.get(name);
        if (clip == null) {
            return;
        }

        stopBackgroundMusic();

        setVolume(clip, settings.getMusicVolume());
        clip.setFramePosition(0); // Musik von vorne beginnen
        clip.loop(Clip.LOOP_CONTINUOUSLY);
        clip.start();
        currentBackgroundMusic = clip;
    }

    /**
     * Stoppt die aktuelle Hintergrundmusik und schließt ihren Clip.
     * <p>
     * Läuft keine Hintergrundmusik, passiert nichts.
     */
    public void stopBackgroundMusic() {
        if (currentBackgroundMusic != null) {
            if (currentBackgroundMusic.isRunning()) {
                currentBackgroundMusic.stop();
            }
            currentBackgroundMusic.close();
            currentBackgroundMusic = null;
        }
    }

    // ===== Hilfsmethoden =====

    /**
     * Geht alle Clips durch und setzt ihre Lautstärke auf die aktuellen Einstellungen.
     */
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

    /**
     * Setzt die Lautstärke für Soundeffekte und wendet sie sofort auf alle Clips an.
     *
     * @param volume neue Lautstärke als Anteil von 0.0 bis 1.0
     */
    public void setEffectsVolume(float volume) {
        settings.setEffectsVolume(volume);
        applyVolumes();
    }

    /**
     * Setzt die Lautstärke für die Hintergrundmusik und wendet sie sofort auf alle Clips an.
     *
     * @param volume neue Lautstärke als Anteil von 0.0 bis 1.0
     */
    public void setMusicVolume(float volume) {
        settings.setMusicVolume(volume);
        applyVolumes();
    }

    public SoundSettings getSoundSettings() {
        return settings;
    }
}
