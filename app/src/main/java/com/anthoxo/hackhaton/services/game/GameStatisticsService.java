package com.anthoxo.hackhaton.services.game;

import com.anthoxo.hackhaton.dtos.GridResultDto;
import com.anthoxo.hackhaton.models.Game;
import com.anthoxo.hackhaton.models.Grid;
import com.anthoxo.hackhaton.models.StartingTile;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.IntStream;

@Service
public class GameStatisticsService {

    public List<GridResultDto.Statistic> getStatistics(Game game) {
        Grid grid = game.getGrid();
        List<ShortStatistic> shortStatistics = game.getPlayers()
                .stream()
                .map(player -> {
                    int tileNumber = (int) countTiles(grid,
                            player.currentColor(grid));
                    return new ShortStatistic(
                            player.name(),
                            player.currentColor(grid),
                            tileNumber,
                            player.startingTile()
                    );
                })
                .sorted(Comparator.comparing(ShortStatistic::tileNumber)
                        .reversed())
                .toList();
        return IntStream.range(0, game.getPlayers().size())
                .mapToObj(i -> {
                    ShortStatistic shortStatistic = shortStatistics.get(i);
                    return new GridResultDto.Statistic(
                            shortStatistic.name(),
                            i + 1,
                            shortStatistic.finalColor(),
                            shortStatistic.tileNumber(),
                            shortStatistic.startingTile()
                    );
                })
                .toList();
    }

    private long countTiles(Grid grid, int color) {
        return grid.colors().stream()
                .flatMap(List::stream)
                .filter(i -> color == i)
                .count();
    }

    private record ShortStatistic(String name, int finalColor, int tileNumber,
                                  StartingTile startingTile) {

    }
}
