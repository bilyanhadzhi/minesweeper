package me.bilyan.minesweeper.exceptions;

public class UninitializedBoardException extends Exception {
    public UninitializedBoardException() {
    }

    public UninitializedBoardException(String message) {
        super(message);
    }
}
