package me.bilyan.minesweeper;

import java.util.Objects;

public class IntPair {
    private int firstElement;
    private int secondElement;

    public IntPair() {
        this.firstElement = 0;
        this.secondElement = 0;
    }

    public IntPair(int firstElement, int secondElement) {
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

        IntPair intPair = (IntPair) other;

        return firstElement == intPair.firstElement && secondElement == intPair.secondElement;
    }

    @Override
    public int hashCode() {
        return Objects.hash(firstElement, secondElement);
    }
}
