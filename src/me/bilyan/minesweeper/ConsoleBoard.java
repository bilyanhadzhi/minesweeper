package me.bilyan.minesweeper;

import me.bilyan.minesweeper.exceptions.InvalidBoardPositionException;

import java.util.Random;

public class ConsoleBoard implements Board {
    private final int rowsCount;
    private final int colsCount;
    private final int minesCount;

    private int revealedSafeTilesCount;
    private boolean hasRevealedMine;

    private static final int[] ROW_DIFFERENCE = {1, 1, 1, 0, -1, -1, -1, 0};
    private static final int[] COL_DIFFERENCE = {-1, 0, 1, 1, 1, 0, -1, -1};

    private Tile[][] state;

    public ConsoleBoard(Difficulty difficulty) {
        this.rowsCount = difficulty.getRowsCount();
        this.colsCount = difficulty.getColsCount();
        this.minesCount = difficulty.getMinesCount();

        this.revealedSafeTilesCount = 0;
    }

    @Override
    public void render() {
        System.out.print("    ");
        for (int col = 0; col < colsCount; col++) {
            System.out.printf("%2d ", col);
        }
        System.out.println();

        for (int row = 0; row < rowsCount; row++) {
            System.out.printf("%-4d", row);

            for (int col = 0; col < colsCount; col++) {
                char toPrint;
                Tile tile = state[row][col];

                if (!tile.isRevealed()) {
                    toPrint = '–';
                } else if (!tile.isMine()) {
                    int neighboringMinesCount = getNeighboringMinesCount(new Pair(row, col));

                    if (neighboringMinesCount ==  0) {
                        toPrint = '.';
                    } else {
                        toPrint = (char)(neighboringMinesCount + (int)'0');
                    }
                } else {
                    toPrint = '*';
                }

                System.out.printf("%2c ", toPrint);
            }
            System.out.println();
        }
    }

    @Override
    public void revealTile(Pair coordinates) throws InvalidBoardPositionException {
        if (!isValidBoardPosition(coordinates)) {
            throw new InvalidBoardPositionException("Trying to reveal tile at invalid position");
        }

        if (state == null) {
            generateInitialState(coordinates);
        }

        Tile tile = state[coordinates.first()][coordinates.second()];
        if (tile.isRevealed()) {
            return;
        }

        if (tile.isMine()) {
            // reveal all mines
            hasRevealedMine = true;

            revealAllMineTiles();
            return;
        }

        // reveal current tile
        tile.setRevealed(true);
        revealedSafeTilesCount++;

        int neighboringMines = getNeighboringMinesCount(coordinates);

        if (neighboringMines == 0) {
            for (int i = 0; i < ROW_DIFFERENCE.length; i++) {
                Pair neighborCoordinates =
                        new Pair(coordinates.first() + ROW_DIFFERENCE[i],
                                coordinates.second() + COL_DIFFERENCE[i]);

                if (!isValidBoardPosition(neighborCoordinates)) {
                    continue;
                }

                Tile neighbor = state[neighborCoordinates.first()][neighborCoordinates.second()];

                if (!neighbor.isRevealed()) {
                    revealTile(neighborCoordinates);
                }
            }
        }
    }

    @Override
    public int getRevealedSafeTilesCount() {
        return revealedSafeTilesCount;
    }

    @Override
    public boolean hasRevealedMine() {
        return hasRevealedMine;
    }

    @Override
    public int getAllTilesCount() {
        return rowsCount * colsCount;
    }

    private void revealAllMineTiles() {
        for (int row = 0; row < rowsCount; row++) {
            for (int col = 0; col < colsCount; col++) {
                if (state[row][col].isMine()) {
                    state[row][col].setRevealed(true);
                }
            }
        }
    }

    private void generateInitialState(Pair excluding) {
        int minesLeftToGenerate = minesCount;

        Random random = new Random();

        state = new Tile[rowsCount][colsCount];
        for (int row = 0; row < rowsCount; row++) {
            for (int col = 0; col < colsCount; col++) {
                state[row][col] = new Tile();
            }
        }

        while (minesLeftToGenerate > 0) {
            Pair toAdd = new Pair(random.nextInt(rowsCount), random.nextInt(colsCount));

            if (state[toAdd.first()][toAdd.second()].isMine() || toAdd.equals(excluding)) {
                continue;
            }

            minesLeftToGenerate--;
            state[toAdd.first()][toAdd.second()].setMine(true);
        }
    }

    private int getNeighboringMinesCount(Pair coordinates) {

        int minesFound = 0;

        for (int i = 0; i < ROW_DIFFERENCE.length; i++) {
            Pair neighborPosition =
                    new Pair(coordinates.first() + ROW_DIFFERENCE[i],
                             coordinates.second() + COL_DIFFERENCE[i]);

            if (isValidBoardPosition(neighborPosition)) {
                Tile neighbor = state[neighborPosition.first()][neighborPosition.second()];

                if (neighbor.isMine()) {
                    minesFound++;
                }
            }
        }

        return minesFound;
    }

    private boolean isValidBoardPosition(Pair position) {
        return position.first() >= 0 && position.first() < rowsCount
                && position.second() >= 0 && position.second() < colsCount;
    }
}
