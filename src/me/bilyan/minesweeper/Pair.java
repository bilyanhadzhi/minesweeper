package me.bilyan.minesweeper;

import java.util.Objects;

public class Pair {
    private int firstElement;
    private int secondElement;

    public Pair() {
        this.firstElement = 0;
        this.secondElement = 0;
    }

    public Pair(int firstElement, int secondElement) {
        this.firstElement = firstElement;
        this.secondElement = secondElement;
    }

    public int first() {
        return firstElement;
    }

    public void setFirst(int firstElement) {
        this.firstElement = firstElement;
    }

    public int second() {
        return secondElement;
    }

    public void setSecond(int secondElement) {
        this.secondElement = secondElement;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }

        if (other == null || getClass() != other.getClass()) {
            return false;
        }

        Pair pair = (Pair) other;

        return firstElement == pair.firstElement && secondElement == pair.secondElement;
    }

    @Override
    public int hashCode() {
        return Objects.hash(firstElement, secondElement);
    }
}
