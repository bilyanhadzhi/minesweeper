package me.bilyan.minesweeper;

/**
 * The input-output handler of the minesweeper game.
 *
 * Implementations of this interface need to ensure
 * the user can interact with the game via input
 * about functions such as making a move, or choosing
 * the game's difficulty.
 */
public interface IOHandler {
    Difficulty getDifficultyFromInput();

    IntPair getCoordinatesFromInput();

    void printInvalidInputMessage();

    void printEndgameMessage(boolean hasWon);

    void printMovePrompt();

    void printDifficultyPrompt();

    void printConsolePrompt();
}
