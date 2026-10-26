package enums;

/**
 * Enum, das jeder Schwierigkeit eine Zahl zuordnet,
 * die den Anteil der Bewegung des CPU-Paddles pro Tick angibt.
 */
public enum Difficulty {
    EASY(0.08),
    MEDIUM(0.1),
    HARD(0.2),
    INSANE(0.9);

    private final double value;

    /**
     * Initialisiert die Schwierigkeitsstufe mit ihrem Bewegungsanteil.
     *
     * @param value der Wert, der die Schwierigkeit repräsentiert
     */
    Difficulty(double value) {
        this.value = value;
    }

    /**
     * Gibt den Bewegungsanteil des CPU-Paddles pro Tick zurück.
     *
     * @return Anteil der Bewegung pro Tick (0 bis 1)
     */
    public double getValue() {
        return value;
    }
}
