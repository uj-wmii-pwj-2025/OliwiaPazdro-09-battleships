package battleships.game;

import battleships.model.*;
import java.io.*;
import java.util.*;

public final class Board {
    private final int rows, cols;
    private final CellState[][] cells;
    private final List<Ship> ships = new ArrayList<>();

    private static final int[][] DIRECTIONS = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};

    public Board(int rows, int cols) {

        this.rows = rows; this.cols = cols;
        this.cells = new CellState[rows][cols];
        for (int r = 0; r < rows; r++)
            for (int c = 0; c < cols; c++)
                cells[r][c] = CellState.WATER;
    }

    public static Board fromString(String map) {

        if (!map.contains("\n"))
            map = map.replaceAll("(.{10})", "$1\n");

        String[] lines = map.split("\\R");
        if (lines.length != 10)
            throw new IllegalArgumentException("Mapa musi mieć 10 wierszy, ma:" + lines.length);
        Board b = new Board(10, 10);

        for (int r = 0; r < 10; r++) {
            String row = lines[r];
            if (row.length() != 10)
                throw new IllegalArgumentException("Wiersz " + r + " ma zla dlugosc");

            for (int c = 0; c < 10; c++) {
                char ch = row.charAt(c);
                if (ch == '#')
                    b.cells[r][c] = CellState.SHIP;
                else if (ch == '.')
                    b.cells[r][c] = CellState.WATER;
                else
                    throw new IllegalArgumentException("nieprawidłowy znak w wierszu " + r + ", kolumnie " + c);
            }
        }
        b.buildShipsFromCells();
        return b;
    }

    public static Board fromFile(String path) throws Exception {

        StringBuilder sb = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            String line;
            while ((line = br.readLine()) != null)
                sb.append(line).append("\n");
        }
        return Board.fromString(sb.toString());
    }

    private void buildShipsFromCells() {

        boolean[][] visited = new boolean[rows][cols];

        for (int r = 0; r < rows; r++)
            for (int c = 0; c < cols; c++) {
                if (!visited[r][c] && cells[r][c] == CellState.SHIP) {
                    Ship ship = new Ship();
                    ArrayList<int[]> stack = new ArrayList<>();
                    stack.add(new int[]{r, c});
                    visited[r][c] = true;

                    while (!stack.isEmpty()) {
                        int[] p = stack.removeLast();
                        int pr = p[0], pc = p[1];
                        ship.addSegment(new Coordinate(pr, pc));

                        for (int[] d : DIRECTIONS) {
                            int nr = pr + d[0], nc = pc + d[1];
                            if (nr >= 0 && nr < rows && nc >= 0 && nc < cols &&
                                    !visited[nr][nc] && cells[nr][nc] == CellState.SHIP) {
                                visited[nr][nc] = true;
                                stack.add(new int[]{nr, nc});
                            }
                        }
                    }
                    ships.add(ship);
                }
            }
    }


    public Result applyShot(Coordinate c) {

        boolean hitAny = false;
        boolean sunkNow = false;

        for (Ship s : ships)
            if (s.occupies(c)) {
                hitAny = true;
                s.hit(c);
                sunkNow = s.isSunk();
                break;
            }

        if (!hitAny) {
            if (cells[c.row()][c.col()] == CellState.WATER)
                cells[c.row()][c.col()] = CellState.MISS_MARK;
            return Result.MISS;
        } else {
            cells[c.row()][c.col()] = CellState.HIT_MARK;
            if (sunkNow) {
                boolean allSunk = ships.stream().allMatch(Ship::isSunk);
                return allSunk ? Result.LAST_SUNK : Result.SUNK;
            } else {
                return Result.HIT;
            }
        }
    }

    public String outputBoard() {

        StringBuilder sb = new StringBuilder();
        for (int r=0;r<rows;r++) {
            for (int c=0;c<cols;c++)
                sb.append(cells[r][c].c);
            sb.append('\n');
        }
        return sb.toString();
    }
}
