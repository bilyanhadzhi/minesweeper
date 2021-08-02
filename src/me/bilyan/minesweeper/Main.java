package me.bilyan.minesweeper;

import java.util.Random;

public class Main {

    public static void main(String[] args) {
        IOHandler ioHandler = new ConsoleIOHandler(System.in, System.out);

        Difficulty difficulty = ioHandler.getDifficultyFromInput();
        Board board = new ConsoleBoard(difficulty, new Random(), System.out);

        Game game = new Game(board, ioHandler);
        game.run();
    }
}
