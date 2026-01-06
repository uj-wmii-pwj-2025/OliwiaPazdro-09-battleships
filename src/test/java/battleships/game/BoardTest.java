package battleships.game;

import battleships.model.CellState;
import battleships.model.Coordinate;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BoardTest {

    @Test
    void emptyBoardShotIsMiss() {
        Board b = new Board(10,10);
        Result r = b.applyShot(new Coordinate(0,0));
        assertEquals(Result.MISS, r);
    }

    @Test
    void shotOnSingleSegmentShipIsLastSunk() {
        String map = "#.........\n" +
                "..........\n".repeat(9);
        Board b = Board.fromString(map);
        Result r = b.applyShot(new Coordinate(0,0));
        assertEquals(Result.LAST_SUNK, r);
    }

    @Test
    void shotMarksCellAsMiss() {
        Board b = new Board(10,10);
        Coordinate c = new Coordinate(1,1);
        b.applyShot(c);
        String out = b.outputBoard();
        assertEquals(CellState.MISS_MARK.c, out.charAt(12));
    }


    @Test
    void shotMarksCellAsHit() {
        String map = ".#........\n" +
                "..........\n".repeat(9);
        Board b = Board.fromString(map);
        Coordinate c = new Coordinate(0,1);
        b.applyShot(c);
        assertEquals(CellState.HIT_MARK.c, b.outputBoard().charAt(1));
    }

    @Test
    void fromStringThrowsOnInvalidRowLength() {
        String badMap = "#####\n" + "..........\n".repeat(9);
        assertThrows(IllegalArgumentException.class, () -> Board.fromString(badMap));
    }

    @Test
    void outputBoardHasCorrectDimensions() {
        Board b = new Board(10,10);
        String out = b.outputBoard();
        String[] lines = out.split("\n");
        assertEquals(10, lines.length);
        assertEquals(10, lines[0].length());
    }

    @Test
    void shotOnMultipleShips() {
        String map = "..#.......\n" +
                "#......#..\n" +
                "#..#......\n" +
                "..##......\n" +
                "......##..\n" +
                ".##.......\n" +
                ".........#\n" +
                "..##...#..\n" +
                ".##....#.#\n" +
                ".......#..";
        Result last = null;
        Board b = Board.fromString(map);
        for (int r = 0; r < 10; r++)
            for (int c = 0; c < 10; c++)
                if (map.charAt(r * 11 + c) == '#')
                   last = b.applyShot(new Coordinate(r, c));
        assertEquals(Result.LAST_SUNK, last);
    }

}
