package com.anthoxo.hackhaton.models;

import com.anthoxo.hackhaton.entities.GridEntity;
import com.anthoxo.hackhaton.utils.ListUtils;

import java.util.ArrayList;
import java.util.List;

public record Grid(List<List<Integer>> colors) {

    public Grid(Grid grid) {
        this(ListUtils.copy(grid.colors));
    }

    public Grid(GridEntity gridEntity) {
        this(ListUtils.copy(gridEntity.getGrid()));
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
        int startRow = startingTile.getRow(colors.size());
        int startCol = startingTile.getCol(colors.size());
        color(startRow, startCol, previousColor, newColor, passed);
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

    public Integer getCurrentColor(StartingTile startingTile) {
        int size = colors.size();
        int row = startingTile.getRow(size);
        int col = startingTile.getCol(size);
        return colors.get(row).get(col);
    }
}
