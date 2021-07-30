package me.bilyan.minesweeper;

import java.io.InputStream;
import java.io.PrintStream;
import java.util.List;
import java.util.Scanner;

public class IOHandler {
    private final Scanner input;
    private final PrintStream output;

    public IOHandler(InputStream input, PrintStream output) {
        this.input = new Scanner(input);
        this.output = output;
    }

    public Difficulty getDifficultyFromInput() {
        List<Integer> validInputs = List.of(0, 1, 2);

        int userInput;
        do {
            printDifficultyPrompt();
            userInput = input.nextInt();
        } while (!validInputs.contains(userInput));

        if (userInput == 0) {
            return Difficulty.BEGINNER;
        } else if (userInput == 1) {
            return Difficulty.INTERMEDIATE;
        } else {
            return Difficulty.ADVANCED;
        }
    }

    public IntPair getCoordinatesFromInput() {
        printMovePrompt();

        int row, column;

        row = input.nextInt();
        column = input.nextInt();

        return new IntPair(row, column);
    }

    public void printInvalidInputMessage() {
        output.println("Invalid tile position");
    }

    public void printEndgameMessage(boolean hasWon) {
        if (hasWon) {
            output.println("You won!");
        } else {
            output.println("You lost!");
        }
    }

    private void printPrompt() {
        output.print("-> ");
    }

    private void printMovePrompt() {
        output.println("Enter your move (row, column)");
        printPrompt();
    }

    private void printDifficultyPrompt() {
        output.println("Enter the difficulty level");
        output.println("Press 0 for " + Difficulty.BEGINNER + " " +
                Difficulty.BEGINNER.getDescription());

        output.println("Press 1 for " + Difficulty.INTERMEDIATE + " " +
                Difficulty.INTERMEDIATE.getDescription());

        output.println("Press 2 for " + Difficulty.ADVANCED + " " +
                Difficulty.ADVANCED.getDescription());

        printPrompt();
    }
}
