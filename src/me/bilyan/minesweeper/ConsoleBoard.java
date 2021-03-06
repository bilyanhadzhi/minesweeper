package me.bilyan.minesweeper;

import me.bilyan.minesweeper.exceptions.InvalidBoardPositionException;

import java.io.PrintStream;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class ConsoleBoard implements Board {
    // used when iterating over the neighbors of a tile
    private static final int[] ROW_DIFFERENCE = {1, 1, 1, 0, -1, -1, -1, 0};
    private static final int[] COL_DIFFERENCE = {-1, 0, 1, 1, 1, 0, -1, -1};

    private final Difficulty difficulty;

    private Tile[][] state;

    // used when determining if game is over
    private int revealedSafeTilesCount;
    private boolean hasRevealedMine;

    // used when revealing all mine tiles once game is lost
    private final Set<IntPair> mineTileCoordinates;

    // used when initializing mine positions,
    // added as dependency for testability
    private final Random random;

    // used when rendering the board
    private final PrintStream output;

    public ConsoleBoard(Difficulty difficulty, Random random, PrintStream output) {
        this.difficulty = difficulty;

        this.revealedSafeTilesCount = 0;
        this.mineTileCoordinates = new HashSet<>();
        this.random = random;
        this.output = output;

        generateInitialState();
    }

    @Override
    public void render() {
        // print column numbers
        output.append("    ");

        for (int col = 0; col < difficulty.getColsCount(); col++) {
            output.append(String.format("%2d ", col));
        }
        output.append(System.lineSeparator());

        // print board itself
        for (int row = 0; row < difficulty.getRowsCount(); row++) {

            // print row number
            output.append(String.format("%-4d", row));

            for (int col = 0; col < difficulty.getColsCount(); col++) {
                char toPrint = getCharacterForTile(new IntPair(row, col));

                output.append(String.format("%2c ", toPrint));
            }
            output.append(System.lineSeparator());
        }
    }

    @Override
    public void revealTile(IntPair coordinates) throws InvalidBoardPositionException {
        if (!isValidBoardPosition(coordinates)) {
            throw new InvalidBoardPositionException("Trying to reveal tile at invalid position");
        }

        if (revealedSafeTilesCount == 0 && state[coordinates.first()][coordinates.second()].isMine()) {
            moveMineFromTile(coordinates);
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

    private void moveMineFromTile(IntPair coordinates) throws InvalidBoardPositionException {
        if (!isValidBoardPosition(coordinates)) {
            throw new InvalidBoardPositionException("Trying to move mine from invalid position");
        }

        if (!state[coordinates.first()][coordinates.second()].isMine()) {
            return;
        }

        IntPair generated;
        do {
            generated = new IntPair(random.nextInt(difficulty.getRowsCount()), random.nextInt(difficulty.getColsCount()));
        } while (mineTileCoordinates.contains(generated));

        state[coordinates.first()][coordinates.second()].setMine(false);
        state[generated.first()][generated.second()].setMine(true);

        mineTileCoordinates.remove(coordinates);
        mineTileCoordinates.add(generated);
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
        return difficulty.getRowsCount() * difficulty.getColsCount();
    }

    @Override
    public Difficulty getDifficulty() {
        return difficulty;
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

    private void generateInitialState() {
        int minesLeftToGenerate = difficulty.getMinesCount();

        state = new Tile[difficulty.getRowsCount()][difficulty.getColsCount()];
        for (int row = 0; row < difficulty.getRowsCount(); row++) {
            for (int col = 0; col < difficulty.getColsCount(); col++) {
                state[row][col] = new Tile();
            }
        }

        while (minesLeftToGenerate > 0) {
            IntPair toAdd = new IntPair(random.nextInt(difficulty.getRowsCount()), random.nextInt(difficulty.getColsCount()));

            if (state[toAdd.first()][toAdd.second()].isMine()) {
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
        return position.first() >= 0 && position.first() < difficulty.getRowsCount()
                && position.second() >= 0 && position.second() < difficulty.getColsCount();
    }
}
