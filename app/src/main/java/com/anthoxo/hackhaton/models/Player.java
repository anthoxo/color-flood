package com.anthoxo.hackhaton.models;

import java.util.Objects;

public final class Player {

    private final String name;
    private final StartingTile startingTile;
    private final Grid grid;
    private int currentColor;

    public Player(String name, StartingTile startingTile, Grid grid) {
        this.name = name;
        this.startingTile = startingTile;
        this.grid = grid;
        this.currentColor = -1;
    }

    public void color(int newColor) {
        grid.color(startingTile, currentColor, newColor);
        this.currentColor = newColor;
    }

    public String name() {
        return name;
    }

    public StartingTile startingTile() {
        return startingTile;
    }

    public Grid grid() {
        return grid;
    }

    public int currentColor() {
        if (currentColor == -1) {
            int size = grid.colors().size();
            return switch (startingTile) {
                case TOP_LEFT -> grid.colors().get(0).get(0);
                case BOTTOM_RIGHT -> grid.colors().get(size - 1).get(size - 1);
            };
        }
        return currentColor;
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
                Objects.equals(this.grid, that.grid) &&
                this.currentColor == that.currentColor;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, startingTile, grid, currentColor);
    }

    @Override
    public String toString() {
        return "Player[" +
                "name=" + name + ", " +
                "startingTile=" + startingTile + ", " +
                "grid=" + grid + ", " +
                "color=" + currentColor + ']';
    }

}
