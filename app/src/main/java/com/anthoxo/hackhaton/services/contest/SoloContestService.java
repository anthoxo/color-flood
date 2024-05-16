package com.anthoxo.hackhaton.services.contest;


import com.anthoxo.hackhaton.dtos.GridResultDto;
import com.anthoxo.hackhaton.entities.GridEntity;
import com.anthoxo.hackhaton.entities.SoloRun;
import com.anthoxo.hackhaton.entities.User;
import com.anthoxo.hackhaton.exceptions.GameCancelledException;
import com.anthoxo.hackhaton.models.Grid;
import com.anthoxo.hackhaton.models.Joker;
import com.anthoxo.hackhaton.services.game.GameResolverService;
import com.anthoxo.hackhaton.services.game.SoloRunService;
import com.anthoxo.hackhaton.services.ladder.EloService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.IntStream;

@Service
public class SoloContestService {

    private static final Logger LOGGER = LoggerFactory.getLogger(SoloContestService.class);

    private final SoloRunService soloRunService;
    private final EloService eloService;
    private final GameResolverService gameResolverService;

    public SoloContestService(
        SoloRunService soloRunService,
        EloService eloService,
        GameResolverService gameResolverService
    ) {
        this.soloRunService = soloRunService;
        this.eloService = eloService;
        this.gameResolverService = gameResolverService;
    }

    public void run(List<User> users, List<GridEntity> gridEntities) {
        gridEntities.forEach(gridEntity -> {
            users.forEach(user -> {
                Grid grid = new Grid(gridEntity);
                List<String> moves;
                List<Joker> jokers;
                try {
                    GridResultDto gridResultDto = soloRunService.run(user, grid);
                    moves = gameResolverService.computeMoves(gridResultDto);
                    jokers = gameResolverService.computeJokers(gridResultDto);
                } catch (GameCancelledException ex) {
                    LOGGER.error(
                            "Something happen during the running, move set to 300.", ex);
                    moves = IntStream.range(0, 300)
                            .mapToObj(i -> 1)
                            .map(String::valueOf)
                            .toList();
                    jokers = IntStream.range(0, 300)
                        .mapToObj(i -> Joker.NONE)
                        .toList();
                }

                soloRunService.save(user, gridEntity, moves, jokers);
            });
            List<SoloRun> soloRuns = soloRunService.findAllByGrid(gridEntity);
            eloService.computeElo(soloRuns);
        });
    }
}
