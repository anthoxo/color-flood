package com.anthoxo.hackhaton.services.game;

import com.anthoxo.hackhaton.dtos.GridResultDto;
import com.anthoxo.hackhaton.entities.BattleRun;
import com.anthoxo.hackhaton.entities.SoloRun;
import com.anthoxo.hackhaton.entities.VersusRun;
import com.anthoxo.hackhaton.models.Game;
import com.anthoxo.hackhaton.models.Grid;
import com.anthoxo.hackhaton.models.Player;
import com.anthoxo.hackhaton.models.StartingTile;
import com.anthoxo.hackhaton.utils.ListUtils;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GameResolverService {

    private final GameStatisticsService gameStatisticsService;

    public GameResolverService(GameStatisticsService gameStatisticsService) {
        this.gameStatisticsService = gameStatisticsService;
    }

    public GridResultDto resolve(SoloRun soloRun) {
        Player player = new Player(
            soloRun.getUser().getTeamName(),
            StartingTile.TOP_LEFT,
            soloRun.getUser().getCodeFilename()
        );
        Grid grid = new Grid(ListUtils.copy(soloRun.getGrid().getGrid()));
        Game game = new Game(List.of(player), grid);
        resolve(game, soloRun.getMoves());
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
        Grid grid = new Grid(ListUtils.copy(versusRun.getGrid().getGrid()));
        Game game = new Game(List.of(player1, player2), grid);
        resolve(game, versusRun.getMoves());
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
        Grid grid = new Grid(ListUtils.copy(battleRun.getGrid().getGrid()));
        Game game = new Game(List.of(player1, player2, player3, player4), grid);
        resolve(game, battleRun.getMoves());
        return new GridResultDto(game.getHistory(), gameStatisticsService.getStatistics(game));
    }

    public void resolve(Game game, List<String> moves) {
        for (int i = 0; i < moves.size(); i++) {
            game.run(i, Integer.valueOf(moves.get(i)));
        }
    }
}
