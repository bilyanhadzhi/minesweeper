package me.bilyan.minesweeper;

import me.bilyan.minesweeper.exceptions.InvalidBoardPositionException;
import me.bilyan.minesweeper.exceptions.UninitializedBoardException;

import java.io.PrintStream;
import java.io.Writer;

// TODO: comment

public interface Board {
    void render(PrintStream printStream) throws UninitializedBoardException;

    void revealTile(IntPair coordinates) throws InvalidBoardPositionException;

    int getRevealedSafeTilesCount();

    boolean hasRevealedMine();

    int getAllTilesCount();
}
