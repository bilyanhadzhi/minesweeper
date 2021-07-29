package me.bilyan.minesweeper;

import java.util.List;
import java.util.Scanner;

public class IOHandler {
    private final Scanner scanner = new Scanner(System.in);

    public Difficulty getDifficultyFromInput() {
        List<Integer> validInputs = List.of(0, 1, 2);

        int userInput;
        do {
            printDifficultyPrompt();
            userInput = scanner.nextInt();
        } while (!validInputs.contains(userInput));

        if (userInput == 0) {
            return Difficulty.BEGINNER;
        } else if (userInput == 1) {
            return Difficulty.INTERMEDIATE;
        } else {
            return Difficulty.ADVANCED;
        }
    }

    public Pair getCoordinatesFromInput() {
        printMovePrompt();

        int row, column;

        row = scanner.nextInt();
        column = scanner.nextInt();

        return new Pair(row, column);
    }

    public void printInvalidInputMessage() {
        System.out.println("Invalid tile position");
    }

    public void printEndgameMessage(boolean hasWon) {
        if (hasWon) {
            System.out.println("You won!");
        } else {
            System.out.println("You lost!");
        }
    }

    private void printPrompt() {
        System.out.print("-> ");
    }

    private void printMovePrompt() {
        System.out.println("Enter your move (row, column)");
        printPrompt();
    }

    private void printDifficultyPrompt() {
        System.out.println("Enter the difficulty level");
        System.out.println("Press 0 for " + Difficulty.BEGINNER);
        System.out.println("Press 1 for " + Difficulty.INTERMEDIATE);
        System.out.println("Press 2 for " + Difficulty.ADVANCED);

        printPrompt();
    }

}
