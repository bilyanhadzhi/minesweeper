package me.bilyan.minesweeper;

import me.bilyan.minesweeper.exceptions.InvalidBoardPositionException;
import me.bilyan.minesweeper.exceptions.UninitializedBoardException;

import java.io.InputStream;
import java.io.PrintStream;
import java.util.Random;

public class Game {
    private final IOHandler ioHandler;
    private Board board;
    private Difficulty difficulty;
    private final PrintStream output;

    public Game(InputStream input, PrintStream output) {
        this.ioHandler = new IOHandler(input, output);
        this.output = output;
    }

    public void run() {
        Difficulty difficulty = ioHandler.getDifficultyFromInput();

        this.difficulty = difficulty;
        this.board = new ConsoleBoard(difficulty, new Random());

        while (true) {
            IntPair coordinatesInput = ioHandler.getCoordinatesFromInput();

            try {
                executeTurn(coordinatesInput);
                board.render(output);
            } catch (InvalidBoardPositionException e) {
                ioHandler.printInvalidInputMessage();
                continue;
            } catch (UninitializedBoardException e) {
                throw new RuntimeException("Called render on uninitialized matrix", e);
            }

            if (isLost()) {
                ioHandler.printEndgameMessage(false);
                break;
            }

            if (isWon()) {
                ioHandler.printEndgameMessage(true);
                break;
            }
        }
    }

    private void executeTurn(IntPair coordinates) throws InvalidBoardPositionException {
        board.revealTile(coordinates);
    }

    private boolean isLost() {
        return board.hasRevealedMine();
    }

    private boolean isWon() {
        return board.getRevealedSafeTilesCount() + difficulty.getMinesCount()
                == board.getAllTilesCount();
    }
}
