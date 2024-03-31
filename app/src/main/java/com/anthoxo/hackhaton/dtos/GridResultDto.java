package com.anthoxo.hackhaton.dtos;

import com.anthoxo.hackhaton.models.Grid;

import java.util.List;

public record GridResultDto(List<Grid> history, List<Statistic> statistics) {

    public record Statistic(String name, int finalColor) {

    }
}
