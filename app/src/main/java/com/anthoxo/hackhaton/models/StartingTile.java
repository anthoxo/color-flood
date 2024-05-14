package com.anthoxo.hackhaton.models;

import java.util.Optional;
import java.util.stream.Stream;

public enum StartingTile {
    TOP_LEFT(0, 0),
    BOTTOM_RIGHT(1, 1),
    TOP_RIGHT(0, 1),
    BOTTOM_LEFT(1, 0);

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

    public static Optional<StartingTile> getStartingTile(String value) {
        return Stream.of(StartingTile.values())
            .filter(startingTile -> startingTile.name().equals(value))
            .findAny();
    }

    public StartingTile rotate(StartingTile startingTile) {
        int rotation = switch (this) {
            case TOP_LEFT -> 0;
            case TOP_RIGHT -> 1;
            case BOTTOM_RIGHT -> 2;
            case BOTTOM_LEFT -> 3;
        };
        int index = rotation + switch (startingTile) {
            case TOP_LEFT -> 0;
            case TOP_RIGHT -> 1;
            case BOTTOM_RIGHT -> 2;
            case BOTTOM_LEFT -> 3;
        };

        return switch (index%4) {
            case 0 -> TOP_LEFT;
            case 1 -> TOP_RIGHT;
            case 2 -> BOTTOM_RIGHT;
            case 3 -> BOTTOM_LEFT;
            default -> throw new IllegalStateException("Unexpected value: " + index);
        };
    }
}
