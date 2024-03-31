package com.anthoxo.hackhaton.services;

import com.anthoxo.hackhaton.models.Grid;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
public class GridService {

    private final Random random;

    public GridService() {
        this.random = new Random();
    }

    public Grid init(int size, int numberOfColors) {
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
}
