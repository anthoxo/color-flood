package com.anthoxo.hackhaton.dtos;

import com.anthoxo.hackhaton.models.EnrichedGrid;
import com.anthoxo.hackhaton.models.StartingTile;

import java.util.List;

public record GridResultDto(List<EnrichedGrid> history, List<Statistic> statistics) {

    public record Statistic(String name, int rank, int finalColor, int tileNumber, StartingTile startingTile) {

    }
}
