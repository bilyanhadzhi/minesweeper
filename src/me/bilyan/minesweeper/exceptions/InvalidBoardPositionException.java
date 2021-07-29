package me.bilyan.minesweeper.exceptions;

public class InvalidBoardPositionException extends Exception {
    public InvalidBoardPositionException() {
        super();
    }

    public InvalidBoardPositionException(String message) {
        super(message);
    }
}
