package com.anthoxo.hackhaton.services.ladder;

import com.anthoxo.hackhaton.dtos.GridResultDto;
import com.anthoxo.hackhaton.entities.*;
import com.anthoxo.hackhaton.models.Game;
import com.anthoxo.hackhaton.models.Grid;
import com.anthoxo.hackhaton.models.Player;
import com.anthoxo.hackhaton.models.StartingTile;
import com.anthoxo.hackhaton.repositories.LadderRepository;
import com.anthoxo.hackhaton.services.game.GameStatisticsService;
import com.anthoxo.hackhaton.utils.ListUtils;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EloService {

    private final LadderRepository ladderRepository;
    private final GameStatisticsService gameStatisticsService;

    public EloService(LadderRepository ladderRepository,
        GameStatisticsService gameStatisticsService) {
        this.ladderRepository = ladderRepository;
        this.gameStatisticsService = gameStatisticsService;
    }

    public void computeElo(List<SoloRun> soloRuns) {
        for (int i = 0; i < soloRuns.size(); i++) {
            SoloRun soloRun1 = soloRuns.get(i);
            Ladder ladder1 = ladderRepository.findByUser(soloRun1.getUser())
                .orElseGet(() -> new Ladder(soloRun1.getUser()));
            boolean playerOneHasFinished = hasFinished(soloRun1);
            for (int j = i + 1; j < soloRuns.size(); j++) {
                SoloRun soloRun2 = soloRuns.get(j);
                Ladder ladder2 = ladderRepository.findByUser(soloRun2.getUser())
                    .orElseGet(() -> new Ladder(soloRun2.getUser()));
                boolean playerTwoHasFinished = hasFinished(soloRun1);

                double elo1 = computeElo(
                    soloRun1.getMoves().size(),
                    ladder1.getElo(),
                    playerOneHasFinished,
                    soloRun2.getMoves().size(),
                    ladder2.getElo(),
                    playerTwoHasFinished
                );
                double elo2 = computeElo(
                    soloRun2.getMoves().size(),
                    ladder2.getElo(),
                    playerTwoHasFinished,
                    soloRun1.getMoves().size(),
                    ladder1.getElo(),
                    playerOneHasFinished
                );
                ladder1.addChange(elo1);
                ladder2.addChange(elo2);
                ladderRepository.save(ladder1);
                ladderRepository.save(ladder2);
            }
        }
    }

    public void computeElo(VersusRun versusRun) {
        GridEntity gridEntity = versusRun.getGrid();
        Game game = new Game(
            List.of(
                new Player(
                    versusRun.getTopLeftUser().getTeamName(),
                    StartingTile.TOP_LEFT,
                    null
                ),
                new Player(
                    versusRun.getBottomRightUser().getTeamName(),
                    StartingTile.BOTTOM_RIGHT,
                    null
                )
            ),
            new Grid(ListUtils.copy(gridEntity.getGrid()))
        );
        resolveMoves(game, versusRun.getMoves());

        List<GridResultDto.Statistic> statistics = gameStatisticsService.getStatistics(
            game);
        for (int i = 0; i < statistics.size(); i++) {
            GridResultDto.Statistic stat1 = statistics.get(i);
            User user1 = switch (stat1.startingTile()) {
                case TOP_LEFT -> versusRun.getTopLeftUser();
                case BOTTOM_RIGHT -> versusRun.getBottomRightUser();
                case TOP_RIGHT, BOTTOM_LEFT -> throw new IllegalStateException("VersusRun should not start TOP_RIGHT | BOTTOM_LEFT");
            };
            Ladder ladder1 = ladderRepository.findByUser(user1)
                .orElseGet(() -> new Ladder(user1));

            for (int j = i + 1; j < statistics.size(); j++) {
                GridResultDto.Statistic stat2 = statistics.get(j);
                User user2 = switch (stat2.startingTile()) {
                    case TOP_LEFT -> versusRun.getTopLeftUser();
                    case BOTTOM_RIGHT -> versusRun.getBottomRightUser();
                    case TOP_RIGHT, BOTTOM_LEFT -> throw new IllegalStateException("VersusRun should not start TOP_RIGHT | BOTTOM_LEFT");
                };
                Ladder ladder2 = ladderRepository.findByUser(user2)
                    .orElseGet(() -> new Ladder(user2));

                double elo1 = computeElo(
                    stat1.rank(),
                    ladder1.getElo(),
                    stat2.rank(),
                    ladder2.getElo()
                );
                double elo2 = computeElo(
                    stat2.rank(),
                    ladder2.getElo(),
                    stat1.rank(),
                    ladder1.getElo()
                );
                ladder1.addChange(elo1);
                ladder2.addChange(elo2);
                ladderRepository.save(ladder1);
                ladderRepository.save(ladder2);
            }
        }
    }

    public void computeElo(BattleRun battleRun) {
        GridEntity gridEntity = battleRun.getGrid();
        Game game = new Game(
            List.of(
                new Player(
                    battleRun.getTopLeftUser().getTeamName(),
                    StartingTile.TOP_LEFT,
                    null
                ),
                new Player(
                    battleRun.getBottomRightUser().getTeamName(),
                    StartingTile.BOTTOM_RIGHT,
                    null
                ),
                new Player(
                    battleRun.getTopRightUser().getTeamName(),
                    StartingTile.TOP_RIGHT,
                    null
                ),
                new Player(
                    battleRun.getBottomLeftUser().getTeamName(),
                    StartingTile.BOTTOM_LEFT,
                    null
                )
            ),
            new Grid(ListUtils.copy(gridEntity.getGrid()))
        );
        resolveMoves(game, battleRun.getMoves());

        List<GridResultDto.Statistic> statistics = gameStatisticsService.getStatistics(
            game);
        for (int i = 0; i < statistics.size(); i++) {
            GridResultDto.Statistic stat1 = statistics.get(i);
            User user1 = switch (stat1.startingTile()) {
                case TOP_LEFT -> battleRun.getTopLeftUser();
                case BOTTOM_RIGHT -> battleRun.getBottomRightUser();
                case TOP_RIGHT -> battleRun.getTopRightUser();
                case BOTTOM_LEFT -> battleRun.getBottomLeftUser();
            };
            Ladder ladder1 = ladderRepository.findByUser(user1)
                .orElseGet(() -> new Ladder(user1));

            for (int j = i + 1; j < statistics.size(); j++) {
                GridResultDto.Statistic stat2 = statistics.get(j);
                User user2 = switch (stat2.startingTile()) {
                    case TOP_LEFT -> battleRun.getTopLeftUser();
                    case BOTTOM_RIGHT -> battleRun.getBottomRightUser();
                    case TOP_RIGHT -> battleRun.getTopRightUser();
                    case BOTTOM_LEFT -> battleRun.getBottomLeftUser();
                };
                Ladder ladder2 = ladderRepository.findByUser(user2)
                    .orElseGet(() -> new Ladder(user2));

                double elo1 = computeElo(
                    stat1.rank(),
                    ladder1.getElo(),
                    stat2.rank(),
                    ladder2.getElo()
                );
                double elo2 = computeElo(
                    stat2.rank(),
                    ladder2.getElo(),
                    stat1.rank(),
                    ladder1.getElo()
                );
                ladder1.addChange(elo1);
                ladder2.addChange(elo2);
                ladderRepository.save(ladder1);
                ladderRepository.save(ladder2);
            }
        }
    }

    private void resolveMoves(Game game, List<String> moves) {
        for (int i = 0; i < moves.size(); i++) {
            game.run(i, Integer.valueOf(moves.get(i)));
        }
    }

    private double computeElo(
        int moves1,
        double myElo,
        boolean oneHasFinished,
        int moves2,
        double opponentElo,
        boolean twoHasFinished
    ) {
        double difference = (opponentElo - myElo) / 400.;
        double tenRaisedPower = Math.pow(10, difference) + 1;
        double expected = 1 / tenRaisedPower;
        double score = 0;
        if (!oneHasFinished) {
            score = 0;
        } else if (moves1 < moves2 || !twoHasFinished) {
            score = 1;
        } else if (moves1 == moves2) {
            score = 0.5;
        }
        double K = 20;
        return K * (score - expected);
    }

    private double computeElo(
        int rank1,
        double myElo,
        int rank2,
        double opponentElo
    ) {
        return computeElo(rank1, myElo, true, rank2, opponentElo, true);
    }

    private boolean hasFinished(SoloRun soloRun) {
        Grid grid = new Grid(ListUtils.copy(soloRun.getGrid().getGrid()));
        for (int i = 0; i < soloRun.getMoves().size(); i++) {
            grid.color(StartingTile.TOP_LEFT,
                grid.getCurrentColor(StartingTile.TOP_LEFT),
                Integer.valueOf(soloRun.getMoves().get(i)));
        }
        return grid.getNumberOfColors() == 1;
    }
}
