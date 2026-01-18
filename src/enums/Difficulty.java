package enums;

public enum Difficulty {
    //TODO: adjust values as necessary
    EASY(0.075),
    MEDIUM(0.1),
    HARD(0.5),
    INSANE(0.9);

    private final double value;

    Difficulty(double value) {
        this.value = value;
    }

    public double getValue() {
        return value;
    }
}
