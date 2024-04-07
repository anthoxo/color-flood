package com.anthoxo.hackhaton.models;

import java.util.ArrayList;
import java.util.List;

public record Grid(List<List<Integer>> colors) {

    public Grid(Grid grid) {
        this(new ArrayList<>());
        for (int i = 0; i < grid.colors.size(); i++) {
            List<Integer> copy = new ArrayList<>();
            for (int j = 0; j < grid.colors.get(i).size(); j++) {
                copy.add(grid.colors.get(i).get(j));
            }
            this.colors.add(copy);
        }
    }

    public long getNumberOfColors() {
        return colors.stream()
                .flatMap(List::stream)
                .distinct()
                .count();
    }

    public void color(StartingTile startingTile, int previousColor, int newColor) {
        List<List<Boolean>> passed = new ArrayList<>();
        for (int row = 0; row < colors.size(); row++) {
            List<Boolean> passedRow = new ArrayList<>();
            for (int col = 0; col < colors.get(row).size(); col++) {
                passedRow.add(Boolean.FALSE);
            }
            passed.add(passedRow);
        }
        switch (startingTile) {
            case TOP_LEFT -> color(0, 0, previousColor, newColor, passed);
            case BOTTOM_RIGHT ->
                    color(colors.size() - 1, colors.size() - 1, previousColor,
                            newColor, passed);
            case BOTTOM_LEFT -> color(colors.size() - 1, 0, previousColor, newColor, passed);
            case TOP_RIGHT ->
                    color(0, colors.size() - 1, previousColor,
                            newColor, passed);
        }
    }

    private void color(int row, int col, int previousColor, int newColor,
            List<List<Boolean>> passed) {
        if (passed.get(row).get(col).booleanValue()) {
            return;
        }
        passed.get(row).set(col, Boolean.TRUE);

        if (!colors.get(row).get(col).equals(previousColor)) {
            return;
        }
        colors.get(row).set(col, newColor);

        if (row + 1 < colors.size()) {
            color(row + 1, col, previousColor, newColor, passed);
        }
        if (col + 1 < colors.size()) {
            color(row, col + 1, previousColor, newColor, passed);
        }
        if (row > 0) {
            color(row - 1, col, previousColor, newColor, passed);
        }
        if (col > 0) {
            color(row, col - 1, previousColor, newColor, passed);
        }
    }
}
