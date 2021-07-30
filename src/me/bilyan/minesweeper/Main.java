package me.bilyan.minesweeper;

public class Main {

    public static void main(String[] args) {
        IOHandler ioHandler = new ConsoleIOHandler(System.in, System.out);
        Game game = new Game(System.out, ioHandler);

        game.run();
    }
}
