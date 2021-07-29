package me.bilyan.minesweeper;

import me.bilyan.minesweeper.exceptions.InvalidBoardPositionException;

// TODO: comment

public interface Board {
    void render();

    void revealTile(Pair coordinates) throws InvalidBoardPositionException;

    int getRevealedSafeTilesCount();

    boolean hasRevealedMine();

    int getAllTilesCount();
}
