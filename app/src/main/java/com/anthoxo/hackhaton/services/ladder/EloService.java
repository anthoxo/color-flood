package com.anthoxo.hackhaton.services.ladder;

import com.anthoxo.hackhaton.dtos.GridResultDto;
import com.anthoxo.hackhaton.entities.*;
import com.anthoxo.hackhaton.models.Grid;
import com.anthoxo.hackhaton.models.StartingTile;
import com.anthoxo.hackhaton.repositories.LadderRepository;
import com.anthoxo.hackhaton.services.game.GameResolverService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;

@Service
public class EloService {

    private static final Logger LOGGER = LoggerFactory.getLogger(
        EloService.class);
    private static final BiFunction<Run, Ladder, Double> GET_ELO_FUNCTION = (run, ladder) -> switch (run) {
        case SoloRun ignored -> ladder.getSoloElo();
        case VersusRun ignored -> ladder.getVersusElo();
        case BattleRun ignored -> ladder.getBattleElo();
    };


    private final LadderRepository ladderRepository;
    private final GameResolverService gameResolverService;

    public EloService(
        LadderRepository ladderRepository,
        GameResolverService gameResolverService
    ) {
        this.ladderRepository = ladderRepository;
        this.gameResolverService = gameResolverService;
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
                boolean playerTwoHasFinished = hasFinished(soloRun2);

                double elo1 = computeElo(
                    soloRun1.getMoves().size(),
                    ladder1.getSoloElo(),
                    playerOneHasFinished,
                    soloRun2.getMoves().size(),
                    ladder2.getSoloElo(),
                    playerTwoHasFinished
                );
                double elo2 = computeElo(
                    soloRun2.getMoves().size(),
                    ladder2.getSoloElo(),
                    playerTwoHasFinished,
                    soloRun1.getMoves().size(),
                    ladder1.getSoloElo(),
                    playerOneHasFinished
                );
                LOGGER.info(
                    "(solo) ELO => player1={}, nbMoves={}, eloChange={}, elo={}",
                    soloRun1.getUser().getTeamName(),
                    soloRun1.getMoves().size(), elo1, ladder1.getSoloElo());
                LOGGER.info(
                    "(solo) ELO => player2={}, nbMoves={}, eloChange={}, elo={}",
                    soloRun2.getUser().getTeamName(),
                    soloRun2.getMoves().size(), elo2, ladder2.getSoloElo());
                ladder1.addSoloChange(elo1);
                ladder2.addSoloChange(elo2);
                ladderRepository.save(ladder1);
                ladderRepository.save(ladder2);
            }
        }
    }

    public void computeElo(VersusRun versusRun) {
        computeElo(versusRun, tile -> switch (tile) {
            case TOP_LEFT -> versusRun.getTopLeftUser();
            case BOTTOM_RIGHT -> versusRun.getBottomRightUser();
            case TOP_RIGHT, BOTTOM_LEFT -> throw new IllegalStateException(
                "VersusRun should not start TOP_RIGHT | BOTTOM_LEFT");
        });
    }

    public void computeElo(BattleRun battleRun) {
        computeElo(battleRun, tile -> switch (tile) {
            case TOP_LEFT -> battleRun.getTopLeftUser();
            case BOTTOM_RIGHT -> battleRun.getBottomRightUser();
            case TOP_RIGHT -> battleRun.getTopRightUser();
            case BOTTOM_LEFT -> battleRun.getBottomLeftUser();
        });
    }

    public void computeLossElo(VersusRun versusRun) {
        computeLossElo(versusRun, tile -> switch (tile) {
            case TOP_LEFT -> versusRun.getTopLeftUser();
            case BOTTOM_RIGHT -> versusRun.getBottomRightUser();
            case TOP_RIGHT, BOTTOM_LEFT -> throw new IllegalStateException(
                "VersusRun should not start TOP_RIGHT | BOTTOM_LEFT");
        });
    }

    public void computeLossElo(BattleRun battleRun) {
        computeLossElo(battleRun, tile -> switch (tile) {
            case TOP_LEFT -> battleRun.getTopLeftUser();
            case BOTTOM_RIGHT -> battleRun.getBottomRightUser();
            case TOP_RIGHT -> battleRun.getTopRightUser();
            case BOTTOM_LEFT -> battleRun.getBottomLeftUser();
        });
    }

    private void computeElo(
        Run run,
        Function<StartingTile, User> getUserByStartingTile
    ) {
        List<GridResultDto.Statistic> statistics = gameResolverService.resolve(
                run
            )
            .statistics();

        for (int i = 0; i < statistics.size(); i++) {
            GridResultDto.Statistic stat1 = statistics.get(i);
            User user1 = getUserByStartingTile.apply(stat1.startingTile());
            Ladder ladder1 = ladderRepository.findByUser(user1)
                .orElseGet(() -> new Ladder(user1));

            for (int j = i + 1; j < statistics.size(); j++) {
                GridResultDto.Statistic stat2 = statistics.get(j);
                User user2 = getUserByStartingTile.apply(stat2.startingTile());
                Ladder ladder2 = ladderRepository.findByUser(user2)
                    .orElseGet(() -> new Ladder(user2));

                double elo1 = computeElo(
                    stat1.rank(),
                    GET_ELO_FUNCTION.apply(run, ladder1),
                    stat2.rank(),
                    GET_ELO_FUNCTION.apply(run, ladder2)
                );
                double elo2 = computeElo(
                    stat2.rank(),
                    GET_ELO_FUNCTION.apply(run, ladder2),
                    stat1.rank(),
                    GET_ELO_FUNCTION.apply(run, ladder1)
                );

                switch (run) {
                    case SoloRun ignored -> {
                        ladder1.addSoloChange(elo1);
                        ladder2.addSoloChange(elo2);
                    }
                    case VersusRun ignored -> {
                        ladder1.addVersusChange(elo1);
                        ladder2.addVersusChange(elo2);
                    }
                    case BattleRun ignored -> {
                        ladder1.addBattleChange(elo1);
                        ladder2.addBattleChange(elo2);
                    }
                }
                LOGGER.info("ELO => player1={}, rank={}, eloChange={}, elo={}",
                    user1.getTeamName(), stat1.rank(), elo1,
                    GET_ELO_FUNCTION.apply(run, ladder1));
                LOGGER.info("ELO => player2={}, rank={}, eloChange={}, elo={}",
                    user2.getTeamName(), stat2.rank(), elo2,
                    GET_ELO_FUNCTION.apply(run, ladder2));
                ladderRepository.save(ladder1);
                ladderRepository.save(ladder2);
            }
        }
    }

    private void computeLossElo(
        Run run,
        Function<StartingTile, User> getUserByStartingTile
    ) {
        List<GridResultDto.Statistic> statistics = gameResolverService.resolve(
                run
            )
            .statistics();

        for (int i = 0; i < statistics.size(); i++) {
            GridResultDto.Statistic stat1 = statistics.get(i);
            User user1 = getUserByStartingTile.apply(stat1.startingTile());
            Ladder ladder1 = ladderRepository.findByUser(user1)
                .orElseGet(() -> new Ladder(user1));

            for (int j = i + 1; j < statistics.size(); j++) {
                GridResultDto.Statistic stat2 = statistics.get(j);
                User user2 = getUserByStartingTile.apply(stat2.startingTile());
                Ladder ladder2 = ladderRepository.findByUser(user2)
                    .orElseGet(() -> new Ladder(user2));

                double elo1 = computeElo(
                    stat1.rank(),
                    GET_ELO_FUNCTION.apply(run, ladder1),
                    false,
                    stat2.rank(),
                    GET_ELO_FUNCTION.apply(run, ladder2),
                    false
                );
                double elo2 = computeElo(
                    stat2.rank(),
                    GET_ELO_FUNCTION.apply(run, ladder2),
                    false,
                    stat1.rank(),
                    GET_ELO_FUNCTION.apply(run, ladder1),
                    false
                );
                switch (run) {
                    case SoloRun ignored -> {
                        ladder1.addSoloChange(elo1);
                        ladder2.addSoloChange(elo2);
                    }
                    case VersusRun ignored -> {
                        ladder1.addVersusChange(elo1);
                        ladder2.addVersusChange(elo2);
                    }
                    case BattleRun ignored -> {
                        ladder1.addBattleChange(elo1);
                        ladder2.addBattleChange(elo2);
                    }
                }
                ladderRepository.save(ladder1);
                ladderRepository.save(ladder2);
            }
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
        double K = 32;
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
        List<Grid> history = gameResolverService.resolve(soloRun).history();
        return !history.isEmpty() &&
            history.get(history.size() - 1).getNumberOfColors() == 1;
    }
}
