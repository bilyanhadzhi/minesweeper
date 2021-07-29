package me.bilyan.minesweeper;

import me.bilyan.minesweeper.exceptions.InvalidBoardPositionException;

public class Game {
    private final IOHandler ioHandler;
    private Board board;
    private Difficulty difficulty;

    public Game() {
        this.ioHandler = new IOHandler();
    }

    public void run() {
        Difficulty difficulty = ioHandler.getDifficultyFromInput();

        this.difficulty = difficulty;
        this.board = new ConsoleBoard(difficulty);

        while (true) {
            IntPair coordinatesInput = ioHandler.getCoordinatesFromInput();

            try {
                executeTurn(coordinatesInput);
                board.render();
            } catch (InvalidBoardPositionException e) {
                ioHandler.printInvalidInputMessage();
                continue;
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
