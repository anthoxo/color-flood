package com.anthoxo.hackhaton.services.contest;

import com.anthoxo.hackhaton.dtos.GridResultDto;
import com.anthoxo.hackhaton.entities.GridEntity;
import com.anthoxo.hackhaton.entities.User;
import com.anthoxo.hackhaton.entities.VersusRun;
import com.anthoxo.hackhaton.exceptions.GameCancelledException;
import com.anthoxo.hackhaton.models.Grid;
import com.anthoxo.hackhaton.repositories.VersusRunRepository;
import com.anthoxo.hackhaton.services.game.VersusRunService;
import com.anthoxo.hackhaton.services.game.GameResolverService;
import com.anthoxo.hackhaton.services.ladder.EloService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class VersusContestService {

    private final VersusRunService versusRunService;
    private final VersusRunRepository versusRunRepository;
    private final EloService eloService;
    private final GameResolverService gameResolverService;

    public VersusContestService(
        VersusRunService versusRunService,
        VersusRunRepository versusRunRepository,
        EloService eloService,
        GameResolverService gameResolverService
    ) {
        this.versusRunService = versusRunService;
        this.versusRunRepository = versusRunRepository;
        this.eloService = eloService;
        this.gameResolverService = gameResolverService;
    }

    public void run(List<User> users, List<GridEntity> gridEntities) {
        List<User> shuffledUsers = new ArrayList<>(users);
        Collections.shuffle(shuffledUsers);
        for (GridEntity gridEntity : gridEntities) {
            for (int i = 0; i < shuffledUsers.size(); i++) {
                User user1 = shuffledUsers.get(i);
                for (int j = i + 1; j < shuffledUsers.size(); j++) {
                    User user2 = shuffledUsers.get(j);
                    run(user1, user2, gridEntity);
                }
            }
        }
    }

    public void run(User user1, User user2, GridEntity gridEntity) {
        runVersus(user1, user2, gridEntity);
        runVersus(user2, user1, gridEntity);
    }

    private void runVersus(User user1, User user2, GridEntity gridEntity) {
        Grid grid = new Grid(gridEntity);
        try {
            GridResultDto gridResultDto = versusRunService.run(user1, user2, grid);
            List<String> moves = gameResolverService.computeMoves(gridResultDto);
            VersusRun versusRun = new VersusRun(
                gridEntity,
                user1,
                user2,
                moves
            );
            versusRunRepository.save(versusRun);
            eloService.computeElo(versusRun);
        } catch (GameCancelledException gameCancelledException) {
            VersusRun versusRun = new VersusRun(
                gridEntity,
                user1,
                user2,
                List.of()
            );
            versusRunRepository.save(versusRun);
            eloService.computeLossElo(versusRun);
        }
    }
}
