package com.anthoxo.hackhaton.services.game;

import com.anthoxo.hackhaton.models.Grid;
import com.anthoxo.hackhaton.models.StartingTile;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Service
public class GridService {

    private static final int MIN_GRID_SIZE = 5;
    private static final int MAX_GRID_SIZE = 25;

    private static final int MAX_COLOR_NUMBER = 18;

    private final Random random;

    public GridService() {
        this.random = new Random();
    }

    public Grid init(int minNumberOfColors) {
        int size = random.nextInt(MIN_GRID_SIZE, MAX_GRID_SIZE);
        int numberOfColors = random.nextInt(minNumberOfColors, MAX_COLOR_NUMBER);

        List<List<Integer>> colors = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            List<Integer> line = new ArrayList<>();
            for (int j = 0; j < size; j++) {
                line.add(random.nextInt(numberOfColors));
            }
            colors.add(line);
        }
        return new Grid(colors);
    }

    public Grid rotateGridIfNeeded(Grid grid, StartingTile startingTile) {
        if (startingTile == StartingTile.TOP_LEFT) {
            return new Grid(grid);
        }
        List<List<Integer>> colors = new ArrayList<>();
        if (startingTile == StartingTile.BOTTOM_RIGHT) {
            for (int i = grid.colors().size() - 1; i >= 0; --i) {
                List<Integer> line = new ArrayList<>();
                for (int j = grid.colors().size() - 1; j >= 0; --j) {
                    line.add(grid.colors().get(i).get(j));
                }
                colors.add(line);
            }
        }
        if (startingTile == StartingTile.BOTTOM_LEFT) {
            for (int j = 0; j < grid.colors().size(); ++j) {
                List<Integer> line = new ArrayList<>();
                for (int i = grid.colors().size() - 1; i >= 0; --i) {
                    line.add(grid.colors().get(i).get(j));
                }
                colors.add(line);
            }
        }
        if (startingTile == StartingTile.TOP_RIGHT) {
            for (int j = grid.colors().size() - 1; j >= 0; --j) {
                List<Integer> line = new ArrayList<>();
                for (int i = 0; i < grid.colors().size(); ++i) {
                    line.add(grid.colors().get(i).get(j));
                }
                colors.add(line);
            }
        }
        return new Grid(colors);
    }

    public List<String> getFormatGridForProgram(Grid grid) {
        return grid.colors().stream()
                .map(list -> list.stream()
                        .map(String::valueOf)
                        .collect(Collectors.joining(","))
                )
                .toList();
    }
}
