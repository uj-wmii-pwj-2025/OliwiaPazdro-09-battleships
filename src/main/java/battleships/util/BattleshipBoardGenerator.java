package battleships.util;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BattleshipBoardGenerator implements BattleshipGenerator{
    private final Random random;
    private static int[][] board;
    private static int[][] shipsTypes;
    private final int BOARD_SIZE = 10;
    private final int ATTEMPTS_LIMIT = 1000;
    private static final int[][] directions = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};
    private static final int[][] directionsWithDiagonals = {{-1, 0}, {1, 0}, {0, -1}, {0, 1},
                                                            {-1, -1}, {-1, 1}, {1, -1}, {1, 1}};

    BattleshipBoardGenerator() {
        shipsTypes = new int[][]{{4, 1}, {3,2}, {2,3}, {1,4}};
        board = new int[BOARD_SIZE][BOARD_SIZE];
        random = new Random();
    }

    @Override
    public String generateMap() {
        for (int[] shipsType : shipsTypes)
            for (int j = 0; j < shipsType[1]; j++)
                while (!generateShipPosition(shipsType[0]));

        return outputBoard();
    }

    private boolean generateShipPosition(int type) {
        int row, col;

        do {
            row = random.nextInt(BOARD_SIZE);
            col = random.nextInt(BOARD_SIZE);
        } while (board[row][col] != 0);
        board[row][col] = 1;

        return generateAvailableCellsForShip(new int[]{row, col}, type - 1);
    }

    private boolean generateAvailableCellsForShip(int[] mainPosition, int hmToGenerate) {
        int i, row, col, attemptCount = 0;
        boolean added;
        List<int[]> ship = new ArrayList<>();
        ship.add(mainPosition);

        while(hmToGenerate > 0) {
            added = false;

            while(!added) {
                attemptCount++;
                if(attemptCount > ATTEMPTS_LIMIT) {
                    for (int[] s : ship)
                        board[s[0]][s[1]] = 0;
                    return false;
                }

                i = random.nextInt(4);
                row = ship.getLast()[0] + directions[i][0];
                col = ship.getLast()[1] + directions[i][1];
                if(row < 0 || row >= BOARD_SIZE || col < 0 || col >= BOARD_SIZE)
                    continue;

                if (board[row][col] == 0) {
                    board[row][col] = 1;
                    added = true;
                    ship.add(new int[]{row, col});
                }
            }
            hmToGenerate--;
        }
        markUnavailableCells(ship);
        return true;
    }

    private void markUnavailableCells(List<int[]> ship) {
        int x, y;
        for(int[] coordinates : ship) {
            for(int[] d : directionsWithDiagonals) {
                x = coordinates[0] + d[0];
                y = coordinates[1] + d[1];
                if (x >= 0 && x < BOARD_SIZE && y >= 0 && y < BOARD_SIZE && board[x][y] == 0)
                    board[x][y] = 2;
            }
        }
    }

    private String outputBoard() {
        StringBuilder stringBuilder = new StringBuilder();
        for(int i = 0; i < BOARD_SIZE; i++)
            for (int j = 0; j < BOARD_SIZE; j++)
                if(board[i][j] == 1)
                    stringBuilder.append("#");
                else
                    stringBuilder.append(".");
        return stringBuilder.toString();
    }
}
