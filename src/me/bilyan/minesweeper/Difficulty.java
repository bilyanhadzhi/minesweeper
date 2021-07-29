package me.bilyan.minesweeper;

public enum Difficulty {
    BEGINNER(9, 9, 10),
    INTERMEDIATE(16, 16, 40),
    ADVANCED(24, 24, 99);

    private final int rowsCount;
    private final int colsCount;
    private final int minesCount;

    Difficulty(int rowsCount, int colsCount, int minesCount) {
        this.rowsCount = rowsCount;
        this.colsCount = colsCount;
        this.minesCount = minesCount;
    }

    public String getDescription() {
        return "(" + getRowsCount() + " * " + getColsCount() + " Cells and " +
                getMinesCount() + " Mines)";
    }

    public int getRowsCount() {
        return rowsCount;
    }

    public int getColsCount() {
        return colsCount;
    }

    public int getMinesCount() {
        return minesCount;
    }
}
