package me.bilyan.minesweeper;

import me.bilyan.minesweeper.exceptions.InvalidBoardPositionException;

public class Game {
    private final IOHandler ioHandler;
    private final Board board;

    public Game(Board board, IOHandler ioHandler) {
        this.ioHandler = ioHandler;
        this.board = board;
    }

    public void run() {
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
        return board.getRevealedSafeTilesCount() + board.getDifficulty().getMinesCount()
                == board.getAllTilesCount();
    }
}
