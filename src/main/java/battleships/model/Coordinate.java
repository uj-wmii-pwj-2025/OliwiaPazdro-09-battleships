package battleships.model;

public record Coordinate(int row, int col) {
    public Coordinate {

        if (row < 0 || row > 9 || col < 0 || col > 9)
            throw new IllegalArgumentException("Poza zakresem: " + row + "," + col);
    }

    public static Coordinate parse(String s) {

        s = s.trim().toUpperCase();
        if (s.length() < 2)
            throw new IllegalArgumentException("Złe współrzędne: " + s);
        char r = s.charAt(0);

        if (r < 'A' || r > 'J')
            throw new IllegalArgumentException("Zły wiersz: " + r);
        int row = r - 'A';
        int col = Integer.parseInt(s.substring(1)) - 1;

        if (col < 0 || col > 9)
            throw new IllegalArgumentException("Zła kolumna: " + (col + 1));
        return new Coordinate(row, col);
    }

    @Override
    public String toString() {
        return "" + (char) ('A' + row) + (col + 1);
    }
}
