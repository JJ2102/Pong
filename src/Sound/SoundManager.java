package Sound;

import javax.sound.sampled.*;
import java.io.DataInput;
import java.io.File;
import java.util.HashMap;

public class SoundManager {
    HashMap<String, Clip> soundEffekts = new HashMap<>();
    HashMap<String, Clip> backgroundMusik = new HashMap<>();
    private Clip currentBackgroundMusik;

    private Clip createClip(String path) {
        Clip clip = null;
        try {
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File(path)); // AudioInputStream mit rohdaten erstellen
            DataLine.Info info = new DataLine.Info(Clip.class, audioInputStream.getFormat()); // Clip Info erstellen
            clip = (Clip) AudioSystem.getLine(info); // Leeren Clip erstellen
            clip.open(audioInputStream); // Clip mit Sounddaten füllen
        } catch (Exception e) {
            e.printStackTrace();
        }
        return clip;
    }

    private void setVolume(Clip clip, float volume) {
        if (clip != null) {
            FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
            float dB = (float) (20.0 * Math.log10(volume));
            gainControl.setValue(dB);
        }
    }

    // Soundeffekt laden
    public void loadSoundEffekt(String name, String path) {
        try {
            Clip clip = createClip(path);
            soundEffekts.put(name, clip); // Clip in die HashMap speichern
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Soundeffekt abspielen
    public void playSoundEffekt(String name) {
        Clip clip = soundEffekts.get(name);
        if (clip != null) {
            clip.setFramePosition(0);
            clip.start();
        }
    }

    // Hintergrundmusik laden
    public void loadBackgroundMusik(String name, String path) {
        try {
            Clip clip = createClip(path);
            backgroundMusik.put(name, clip); // SourceDataLine in die HashMap speichern
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Hintergrundmusik abspielen
    public void playBackgroundMusik(String name) {
        Clip clip = backgroundMusik.get(name);
        if (clip != null) {
            if (clip.isRunning()) {
                clip.stop(); // Stoppe die Musik, wenn sie bereits läuft
            }
            setVolume(clip, 0.1f);
            clip.setFramePosition(0); // Setze die Position auf den Anfang
            clip.start(); // Starte die Musik
            clip.loop(Clip.LOOP_CONTINUOUSLY); // Schleife die Musik endlos
            currentBackgroundMusik = clip;
        }
    }

    // Hintergrundmusik stoppen
    public void stopBackgroundMusik() {
        if (currentBackgroundMusik != null && currentBackgroundMusik.isRunning()) {
            currentBackgroundMusik.stop();
            currentBackgroundMusik.close();
        }
    }
}
