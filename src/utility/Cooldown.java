package utility;

/**
 * Kleine Zeitsperre, die verhindert, dass eine Aktion zu schnell hintereinander ausgelöst wird.
 */
public class Cooldown {
    private final long durationMs; // Sperrzeit in Millisekunden
    private long nextAllowed; // Zeitpunkt (in ms), ab dem die Aktion wieder erlaubt ist

    /**
     * Legt die Sperrzeit fest; der Cooldown ist sofort einsatzbereit.
     *
     * @param durationMs Sperrzeit in Millisekunden
     */
    public Cooldown(long durationMs) {
        this.durationMs = durationMs;
        this.nextAllowed = 0;
    }

    /**
     * Prüft, ob die Sperrzeit abgelaufen ist und die Aktion wieder erlaubt ist.
     *
     * @return {@code true}, wenn die Aktion ausgeführt werden darf, sonst {@code false}
     */
    public boolean isReady() {
        return System.currentTimeMillis() >= nextAllowed;
    }

    /**
     * Startet die Sperre neu; wird nach dem Ausführen der Aktion aufgerufen.
     */
    public void trigger() {
        nextAllowed = System.currentTimeMillis() + durationMs; // Ab jetzt wieder für durationMs ms gesperrt
    }
}
