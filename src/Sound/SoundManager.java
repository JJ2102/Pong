package Sound;

import javax.sound.sampled.*;
import java.io.DataInput;
import java.io.File;
import java.util.HashMap;

public class SoundManager {
    HashMap<String, Clip> soundEffekts = new HashMap<>();
    HashMap<String, SourceDataLine> backgroundMusik = new HashMap<>();

    // Soundeffekt laden
    public void loadSoundEffekt(String name, String path) {
        try {
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File(path)); // AudioInputStream mit rohdaten erstellen
            DataLine.Info info = new DataLine.Info(Clip.class, audioInputStream.getFormat()); // Clip Info erstellen
            Clip clip = (Clip) AudioSystem.getLine(info); // Leeren Clip erstellen
            clip.open(audioInputStream); // Clip mit Sounddaten füllen
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
}
