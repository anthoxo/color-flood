package com.anthoxo.hackhaton.services;

import com.anthoxo.hackhaton.models.Grid;
import com.anthoxo.hackhaton.models.Player;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
public class ColorFloodService {

    private final static int MAX_TURN = 200;

    private final Random random;

    public ColorFloodService() {
        this.random = new Random();
    }

    public Grid initGrid(int size, int numberOfColors) {
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

    public Grid run(List<Player> players) {
        Grid grid = players.get(0).grid();
        for (int i = 0; i < MAX_TURN; ++i) {
            Player player = players.get(i % players.size());
            int newColor = random.nextInt(3);
            if (players.stream()
                    .map(Player::currentColor)
                    .noneMatch(color -> color == newColor)
            ) {
                player.color(newColor);
            }
            
            if (grid.containsOnlyTwoColors()) {
                break;
            }
        }
        return grid;
    }
}
