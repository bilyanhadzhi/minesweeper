package me.bilyan.minesweeper;

import me.bilyan.minesweeper.exceptions.InvalidBoardPositionException;
import me.bilyan.minesweeper.exceptions.UninitializedBoardException;

/**
 * The board for the minesweeper game.
 * The methods {@code render} and {@code revealTile}
 * are the backbone of the board.
 * There are additional methods, such as {@code hasRevealedMine}
 * that hold other important information about the board's state.
 */
public interface Board {

    /**
     * @throws UninitializedBoardException if this method is called before {@code revealTile} has been.
     *
     * Prints the board's state.
     */
    void render() throws UninitializedBoardException;


    /**
     * @param coordinates The place of the tile to be revealed, as per (row, column) pair.
     * @throws InvalidBoardPositionException if an invalid tile position has been supplied.
     *
     * Upon first call, the board's state is unknown, the board's state is
     * then created and the supplied tile is used to ensure that itself
     * cannot contain a mine.
     */
    void revealTile(IntPair coordinates) throws InvalidBoardPositionException;


    /**
     * @return The number of safe (i.e. not containing a mine) tiles
     * that have been revealed.
     */
    int getRevealedSafeTilesCount();


    /**
     * @return Whether a mine has been revealed.
     *
     * This is an easy way to check if the board is still in a playable state.
     */
    boolean hasRevealedMine();


    /**
     * @return The number of all tiles on the board.
     */
    int getAllTilesCount();
}
