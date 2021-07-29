package me.bilyan.minesweeper;

public class Tile {
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
