package me.bilyan.minesweeper;

public class Tile {
    public static char TILE_SYMBOL_UNREVEALED = '–';
    public static char TILE_SYMBOL_EMPTY = '.';
    public static char TILE_SYMBOL_MINE = '*';

    private boolean isMine;
    private boolean isRevealed;

    public Tile() {
        this.isMine = false;
        this.isRevealed = false;
    }

    public Tile(boolean isMine, boolean isRevealed) {
        this.isMine = isMine;
        this.isRevealed = isRevealed;
    }

    public boolean isMine() {
        return isMine;
    }

    public void setMine(boolean mine) {
        isMine = mine;
    }

    public boolean isRevealed() {
        return isRevealed;
    }

    public void setRevealed(boolean revealed) {
        isRevealed = revealed;
    }
}
