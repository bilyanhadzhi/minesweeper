package me.bilyan.minesweeper;

import me.bilyan.minesweeper.exceptions.InvalidBoardPositionException;

public class Game {
    private final IOHandler ioHandler = new IOHandler();
    private Board board;
    private Difficulty difficulty;

    public void run() {
        Difficulty difficulty = ioHandler.getDifficultyFromInput();

        this.difficulty = difficulty;
        this.board = new ConsoleBoard(difficulty);

        while (true) {
            Pair coordinatesInput = ioHandler.getCoordinatesFromInput();

            try {
                executeTurn(coordinatesInput);
                board.render();
            } catch (InvalidBoardPositionException e) {
                ioHandler.printInvalidInputMessage();
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

    private void executeTurn(Pair coordinates) throws InvalidBoardPositionException {
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
