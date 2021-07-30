package me.bilyan.minesweeper;

import java.io.InputStream;
import java.io.PrintStream;
import java.util.List;
import java.util.Scanner;

public class IOHandler {
    private static final String PROMPT_DEFAULT = "-> ";
    private static final String PROMPT_MOVE = "Enter your move (row, column)";
    private static final String PROMPT_DIFFICULTY = "Enter the difficulty level";
    private static final String PROMPT_DIFFICULTY_OPTION = "Press %d for %s %s";
    private static final String MESSAGE_INVALID_TILE = "Invalid tile position";
    private static final String MESSAGE_GAME_WON = "You won!";
    private static final String MESSAGE_GAME_LOST = "You lost!";

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
        output.println(MESSAGE_INVALID_TILE);
    }

    public void printEndgameMessage(boolean hasWon) {
        if (hasWon) {
            output.println(MESSAGE_GAME_WON);
        } else {
            output.println(MESSAGE_GAME_LOST);
        }
    }

    private void printDefaultPrompt() {
        output.print(PROMPT_DEFAULT);
    }

    private void printMovePrompt() {
        output.println(PROMPT_MOVE);
        printDefaultPrompt();
    }

    private void printDifficultyPrompt() {
        output.println(PROMPT_DIFFICULTY);
        List<Difficulty> difficulties =
                List.of(Difficulty.BEGINNER, Difficulty.INTERMEDIATE, Difficulty.ADVANCED);

        for (Difficulty difficulty : difficulties) {
            output.printf((PROMPT_DIFFICULTY_OPTION) + "%n",
                    difficulty.ordinal(), difficulty, difficulty.getDescription());
        }

        printDefaultPrompt();
    }
}
