package com.anthoxo.hackhaton.models;

import java.util.Objects;

public final class Player {

    private final String name;
    private final StartingTile startingTile;
    private final String pathFile;
    private int currentColor;

    public Player(String name, StartingTile startingTile, String pathFile) {
        this.name = name;
        this.startingTile = startingTile;
        this.pathFile = pathFile;
        this.currentColor = -1;
    }

    public String name() {
        return name;
    }

    public StartingTile startingTile() {
        return startingTile;
    }

    public int currentColor(Grid grid) {
        if (currentColor == -1) {
            int size = grid.colors().size();
            return switch (startingTile) {
                case TOP_LEFT -> grid.colors().get(0).get(0);
                case BOTTOM_RIGHT -> grid.colors().get(size - 1).get(size - 1);
            };
        }
        return currentColor;
    }

    public String pathFile() {
        return pathFile;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj == null || obj.getClass() != this.getClass()) {
            return false;
        }
        var that = (Player) obj;
        return Objects.equals(this.name, that.name) &&
                Objects.equals(this.startingTile, that.startingTile) &&
                this.currentColor == that.currentColor;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, startingTile, currentColor);
    }

    @Override
    public String toString() {
        return "Player[" +
                "name=" + name + ", " +
                "startingTile=" + startingTile + ", " +
                "color=" + currentColor + ']';
    }

}
