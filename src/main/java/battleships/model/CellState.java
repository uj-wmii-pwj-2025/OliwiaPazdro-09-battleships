package battleships.model;

public enum CellState {
    WATER('.'),
    SHIP('#'),
    MISS_MARK('~'),
    HIT_MARK('@');

    public final char c;
    CellState(char c) {
        this.c = c;
    }
}
