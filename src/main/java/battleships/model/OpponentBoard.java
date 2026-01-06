package battleships.model;

import java.util.*;

public final class OpponentBoard {
    private final char[][] cells = new char[10][10];

    public OpponentBoard() {

        for (char[] row : cells)
            java.util.Arrays.fill(row, '?');
    }

    public void markWater(Coordinate c) {

        cells[c.row()][c.col()] = CellState.WATER.c;
    }

    public void markHit(Coordinate c) {

        cells[c.row()][c.col()] = CellState.SHIP.c;
    }

    public void markSunk(Coordinate start) {
        markHit(start);
        List<Coordinate> ship = new ArrayList<>();
        boolean[][] vis = new boolean[10][10];
        find(start.row(), start.col(), ship, vis);

        for (Coordinate c : ship)
            cells[c.row()][c.col()] = CellState.SHIP.c;

        for (Coordinate c : ship)
            for (int dr=-1; dr<=1; dr++)
                for (int dc=-1; dc<=1; dc++) {
                    int r = c.row()+dr, col = c.col()+dc;
                    if (r>=0 && r<10 && col>=0 && col<10 && cells[r][col]=='?')
                        cells[r][col] = CellState.WATER.c;
                }
    }

    private void find(int r, int c, List<Coordinate> ship, boolean[][] vis) {
        if (r<0 || r>=10 || c<0 || c>=10 || vis[r][c]) return;
        char cell = cells[r][c];
        if (cell != CellState.SHIP.c) return;

        vis[r][c] = true;
        ship.add(new Coordinate(r, c));

        find(r-1, c, ship, vis);
        find(r+1, c, ship, vis);
        find(r, c-1, ship, vis);
        find(r, c+1, ship, vis);
    }

    public void revealAllUnknownAsWater() {

        for (char[] row : cells)
            for (int i = 0; i < 10; i++)
                if (row[i] == '?')
                    row[i] = CellState.WATER.c;
    }

    public String outputBoard() {

        StringBuilder sb = new StringBuilder();
        for (char[] row : cells)
            sb.append(row).append('\n');
        return sb.toString();
    }
}
