package me.bilyan.minesweeper;

import me.bilyan.minesweeper.exceptions.InvalidBoardPositionException;
import me.bilyan.minesweeper.exceptions.UninitializedBoardException;

import java.io.PrintStream;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class ConsoleBoard implements Board {
    // used when iterating over the neighbors of a tile
    private static final int[] ROW_DIFFERENCE = {1, 1, 1, 0, -1, -1, -1, 0};
    private static final int[] COL_DIFFERENCE = {-1, 0, 1, 1, 1, 0, -1, -1};

    private final int rowsCount;
    private final int colsCount;
    private final int minesCount;

    private Tile[][] state;

    // used when determining if game is over
    private int revealedSafeTilesCount;
    private boolean hasRevealedMine;

    // used when revealing all mine tiles once game is lost
    private final Set<IntPair> mineTileCoordinates;

    // used when initializing mine positions,
    // added as dependency for testability
    private final Random random;

    public ConsoleBoard(Difficulty difficulty, Random random) {
        this.rowsCount = difficulty.getRowsCount();
        this.colsCount = difficulty.getColsCount();
        this.minesCount = difficulty.getMinesCount();

        this.revealedSafeTilesCount = 0;
        this.mineTileCoordinates = new HashSet<>();
        this.random = random;
    }

    @Override
    public void render(PrintStream printStream) throws UninitializedBoardException {
        if (state == null) {
            throw new UninitializedBoardException("Trying to initialize board that does not exist");
        }

        // print column numbers
        printStream.append("    ");

        for (int col = 0; col < colsCount; col++) {
            printStream.append(String.format("%2d ", col));
        }
        printStream.append(System.lineSeparator());

        // print board itself
        for (int row = 0; row < rowsCount; row++) {

            // print row number
            printStream.append(String.format("%-4d", row));

            for (int col = 0; col < colsCount; col++) {
                char toPrint = getCharacterForTile(new IntPair(row, col));

                printStream.append(String.format("%2c ", toPrint));
            }
            printStream.append(System.lineSeparator());
        }
    }

    @Override
    public void revealTile(IntPair coordinates) throws InvalidBoardPositionException {
        if (!isValidBoardPosition(coordinates)) {
            throw new InvalidBoardPositionException("Trying to reveal tile at invalid position");
        }

        // the state is not generated until the first tile revealed
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
                IntPair neighborCoordinates =
                        new IntPair(coordinates.first() + ROW_DIFFERENCE[i],
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

    private char getCharacterForTile(IntPair tileCoordinates) {
        Tile tile = state[tileCoordinates.first()][tileCoordinates.second()];

        char toPrint;
        if (!tile.isRevealed()) {
            toPrint = Tile.TILE_SYMBOL_UNREVEALED;
        } else if (!tile.isMine()) {
            int neighboringMinesCount = getNeighboringMinesCount(tileCoordinates);

            if (neighboringMinesCount == 0) {
                toPrint = Tile.TILE_SYMBOL_EMPTY;
            } else {
                toPrint = (char)(neighboringMinesCount + (int)'0');
            }
        } else {
            toPrint = Tile.TILE_SYMBOL_MINE;
        }

        return toPrint;
    }

    private void revealAllMineTiles() {
        for (IntPair mineTile : mineTileCoordinates) {
            state[mineTile.first()][mineTile.second()].setRevealed(true);
        }
    }

    private void generateInitialState(IntPair excluding) {
        int minesLeftToGenerate = minesCount;

        state = new Tile[rowsCount][colsCount];
        for (int row = 0; row < rowsCount; row++) {
            for (int col = 0; col < colsCount; col++) {
                state[row][col] = new Tile();
            }
        }

        while (minesLeftToGenerate > 0) {
            IntPair toAdd = new IntPair(random.nextInt(rowsCount), random.nextInt(colsCount));

            if (state[toAdd.first()][toAdd.second()].isMine() || toAdd.equals(excluding)) {
                continue;
            }

            minesLeftToGenerate--;
            state[toAdd.first()][toAdd.second()].setMine(true);
            mineTileCoordinates.add(new IntPair(toAdd.first(), toAdd.second()));
        }
    }

    private int getNeighboringMinesCount(IntPair coordinates) {

        int minesFound = 0;

        for (int i = 0; i < ROW_DIFFERENCE.length; i++) {
            IntPair neighborPosition =
                    new IntPair(coordinates.first() + ROW_DIFFERENCE[i],
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

    private boolean isValidBoardPosition(IntPair position) {
        return position.first() >= 0 && position.first() < rowsCount
                && position.second() >= 0 && position.second() < colsCount;
    }
}
