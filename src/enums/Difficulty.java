package enums;

/**
 * Enum, das jeder Schwierigkeit eine Zahl zuordnet,
 * die den Prozentsatz der Bewegung des gegnerischen Paddels pro Tick angibt.
 */
public enum Difficulty {
    EASY(0.08),
    MEDIUM(0.1),
    HARD(0.2),
    INSANE(0.9);

    private final double value;

    /**
     * Konstruktor für die Schwierigkeitsstufen.
     *
     * @param value der Wert, der die Schwierigkeit repräsentiert
     */
    Difficulty(double value) {
        this.value = value;
    }

    /**
     * Gibt den Bewegungsanteil des gegnerischen Paddels pro Tick zurück.
     *
     * @return Anteil der Bewegung pro Tick (0 bis 1)
     */
    public double getValue() {
        return value;
    }
}
