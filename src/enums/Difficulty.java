package enums;

// Schwierigkeit des Spiels
public enum Difficulty {
    EASY(0.08),
    MEDIUM(0.1),
    HARD(0.2),
    INSANE(0.9);

    private final double value;

    Difficulty(double value) {
        this.value = value;
    }

    public double getValue() {
        return value;
    }
}
