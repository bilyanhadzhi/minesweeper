package me.bilyan.minesweeper;

import me.bilyan.minesweeper.exceptions.InvalidBoardPositionException;

// TODO: comment

public interface Board {
    void render();

    void revealTile(IntPair coordinates) throws InvalidBoardPositionException;

    int getRevealedSafeTilesCount();

    boolean hasRevealedMine();

    int getAllTilesCount();
}
