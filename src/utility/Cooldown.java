package utility;

// Kleine Zeitsperre, die verhindert, dass eine Aktion zu schnell hintereinander ausgelöst wird
public class Cooldown {
    private final long duration; // Sperrzeit in Millisekunden
    private long nextAllowed; // Zeitpunkt (in ms), ab dem die Aktion wieder erlaubt ist

    // Legt die Sperrzeit fest, startet aber sofort einsatzbereit (nextAllowed = 0)
    public Cooldown(long durationMs) {
        this.duration = durationMs;
        this.nextAllowed = 0;
    }

    // Prüft, ob die Sperrzeit abgelaufen ist und die Aktion wieder erlaubt ist
    public boolean isReady() {
        return System.currentTimeMillis() >= nextAllowed;
    }

    // Startet die Sperre neu, wird nach dem Ausführen der Aktion aufgerufen
    public void trigger() {
        nextAllowed = System.currentTimeMillis() + duration; // ab jetzt wieder für duration ms gesperrt
    }
}
