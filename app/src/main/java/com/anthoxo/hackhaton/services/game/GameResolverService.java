package com.anthoxo.hackhaton.services.game;

import com.anthoxo.hackhaton.dtos.GridResultDto;
import com.anthoxo.hackhaton.entities.BattleRun;
import com.anthoxo.hackhaton.entities.Run;
import com.anthoxo.hackhaton.entities.SoloRun;
import com.anthoxo.hackhaton.entities.VersusRun;
import com.anthoxo.hackhaton.models.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class GameResolverService {

    private final GameStatisticsService gameStatisticsService;

    public GameResolverService(GameStatisticsService gameStatisticsService) {
        this.gameStatisticsService = gameStatisticsService;
    }

    public GridResultDto resolve(Run run) {
        return switch (run) {
            case SoloRun soloRun -> resolve(soloRun);
            case VersusRun versusRun -> resolve(versusRun);
            case BattleRun battleRun -> resolve(battleRun);
        };
    }

    public GridResultDto resolve(SoloRun soloRun) {
        Player player = new Player(
            soloRun.getUser().getTeamName(),
            StartingTile.TOP_LEFT,
            soloRun.getUser().getCodeFilename()
        );
        Grid grid = new Grid(soloRun.getGrid());
        Game game = new Game(List.of(player), grid);
        resolve(game, soloRun.getMoves(), soloRun.getJokers());
        return new GridResultDto(game.getHistory(), gameStatisticsService.getStatistics(game));
    }

    public GridResultDto resolve(VersusRun versusRun) {
        Player player1 = new Player(
            versusRun.getTopLeftUser().getTeamName(),
            StartingTile.TOP_LEFT,
            versusRun.getTopLeftUser().getCodeFilename()
        );
        Player player2 = new Player(
            versusRun.getBottomRightUser().getTeamName(),
            StartingTile.BOTTOM_RIGHT,
            versusRun.getBottomRightUser().getCodeFilename()
        );
        Grid grid = new Grid(versusRun.getGrid());
        Game game = new Game(List.of(player1, player2), grid);
        resolve(game, versusRun.getMoves(), versusRun.getJokers());
        return new GridResultDto(game.getHistory(), gameStatisticsService.getStatistics(game));
    }

    public GridResultDto resolve(BattleRun battleRun) {
        Player player1 = new Player(
            battleRun.getTopLeftUser().getTeamName(),
            StartingTile.TOP_LEFT,
            battleRun.getTopLeftUser().getCodeFilename()
        );
        Player player2 = new Player(
            battleRun.getBottomRightUser().getTeamName(),
            StartingTile.BOTTOM_RIGHT,
            battleRun.getBottomRightUser().getCodeFilename()
        );
        Player player3 = new Player(
            battleRun.getTopRightUser().getTeamName(),
            StartingTile.TOP_RIGHT,
            battleRun.getTopRightUser().getCodeFilename()
        );
        Player player4 = new Player(
            battleRun.getBottomLeftUser().getTeamName(),
            StartingTile.BOTTOM_LEFT,
            battleRun.getBottomLeftUser().getCodeFilename()
        );
        Grid grid = new Grid(battleRun.getGrid());
        Game game = new Game(List.of(player1, player2, player3, player4), grid);
        resolve(game, battleRun.getMoves(), battleRun.getJokers());
        return new GridResultDto(game.getHistory(), gameStatisticsService.getStatistics(game));
    }

    public List<String> computeMoves(GridResultDto gridResultDto) {
        int numberOfPlayers = gridResultDto.statistics().size();
        List<EnrichedGrid> history = gridResultDto.history();
        List<String> moves = new ArrayList<>();
        for (int i = 0; i < history.size() - 1; i++) {
            Grid grid = history.get(i+1).grid();
            StartingTile startingTile = switch (i % numberOfPlayers) {
                case 0 -> StartingTile.TOP_LEFT;
                case 1 -> StartingTile.BOTTOM_RIGHT;
                case 2 -> StartingTile.TOP_RIGHT;
                case 3 -> StartingTile.BOTTOM_LEFT;
                default -> throw new IllegalStateException();
            };
            moves.add(grid.getCurrentColor(startingTile).toString());
        }
        return moves;
    }

    public List<Joker> computeJokers(GridResultDto gridResultDto) {
        return gridResultDto
            .history()
            .stream()
            .skip(1)
            .map(EnrichedGrid::usedJoker)
            .toList();
    }

    private void resolve(Game game, List<String> moves, List<Joker> jokers) {
        for (int i = 0; i < moves.size(); i++) {
            game.run(i, Integer.valueOf(moves.get(i)), jokers.get(i));
        }
    }
}
