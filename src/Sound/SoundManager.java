package sound;

import javax.sound.sampled.*;
import java.io.File;
import java.util.HashMap;

public class SoundManager {
    private final HashMap<String, Clip> soundEffects = new HashMap<>(); // Name -> Soundeffekt-Clip; Map aller Soundeffekte
    private final HashMap<String, Clip> backgroundMusic = new HashMap<>(); // Name -> HintergrundMusik-Clip; Map aller Musik Clips
    private Clip currentBackgroundMusic; // aktuelle HintergrundMusik

    private final SoundSettings settings;

    public SoundManager() {
        settings = new SoundSettings();
    }

    // erstellt einen Clip aus einer Audiodatei
    private Clip createClip(String path) {
        try {
            File file = new File(path); // holt sich das File zum Path
            if (file.exists()) { // falls das File existiert
                AudioInputStream sound = AudioSystem.getAudioInputStream(file); // Audiodatei laden
                Clip clip = AudioSystem.getClip(); // Clip erstellen
                clip.open(sound); // Clip mit Audiodaten füllen
                return clip;
            } else {
                throw new RuntimeException("Sound: File Not Found: " + path);
            }
        } catch (Exception e) {
            throw new RuntimeException("Sound: Error loading file: " + path, e);
        }
    }

    // ===== Volume =====
    // setzt die Lautstärke für einen Clip
    private void setVolume(Clip clip, float volume) {
        if (clip == null) return;
        try {
            FloatControl gain = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN); // Lautstärkeregler holen
            float dB = volumeToDB(volume, gain.getMinimum(), gain.getMaximum()); // Lautstärke in dB umrechnen
            gain.setValue(dB); // Lautstärke setzen
        } catch (IllegalArgumentException ignored) {
            // MASTER_GAIN nicht verfügbar ⇾ nichts tun
        }
    }

    // Konvertiert das Prozentuale Volumen in Decibel
    private float volumeToDB(float volume, float min, float max) {
        float dB = (float) (20.0 * Math.log10(volume)); // dB-Wert berechnen
        return Math.clamp(dB, min, max); // dB-Wert begrenzen
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
        if (clip == null) return; // falls clip nicht existiert abrechen

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
        if (clip == null) return; // falls clip nicht existiert abrechen

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
            if (currentBackgroundMusic.isRunning()) currentBackgroundMusic.stop(); // stoppe die Musik
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
        if (currentBackgroundMusic != null) setVolume(currentBackgroundMusic, settings.getMusicVolume());
    }

    // setzt die Lautstärke für Soundeffekte und Hintergrundmusik
    public void setEffectsVolume(float v) {
        settings.setEffectsVolume(v);
        applyVolumes();
    }

    public void setMusicVolume(float v) {
        settings.setMusicVolume(v);
        applyVolumes();
    }

    public SoundSettings getSoundSettings() {
        return settings;
    }
}
