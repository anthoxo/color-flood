package com.anthoxo.hackhaton.models;

public enum StartingTile {
    TOP_LEFT(0, 0),
    BOTTOM_RIGHT(1, 1),
    BOTTOM_LEFT(1, 0),
    TOP_RIGHT(0, 1);

    private int rowBit;
    private int colBit;

    StartingTile(int rowBit, int colBit) {
        this.rowBit = rowBit;
        this.colBit = colBit;
    }

    public int getRow(int size) {
        return (size - rowBit) % size;
    }

    public int getCol(int size) {
        return (size - colBit) % size;
    }
}
