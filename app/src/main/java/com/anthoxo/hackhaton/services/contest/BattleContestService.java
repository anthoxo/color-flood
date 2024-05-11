package com.anthoxo.hackhaton.services.contest;

import com.anthoxo.hackhaton.dtos.GridResultDto;
import com.anthoxo.hackhaton.entities.BattleRun;
import com.anthoxo.hackhaton.entities.GridEntity;
import com.anthoxo.hackhaton.entities.User;
import com.anthoxo.hackhaton.exceptions.GameCancelledException;
import com.anthoxo.hackhaton.models.Grid;
import com.anthoxo.hackhaton.repositories.BattleRunRepository;
import com.anthoxo.hackhaton.services.game.BattleRunService;
import com.anthoxo.hackhaton.services.game.GameResolverService;
import com.anthoxo.hackhaton.services.ladder.EloService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class BattleContestService {

    private final BattleRunService battleRunService;
    private final BattleRunRepository battleRunRepository;
    private final EloService eloService;
    private final GameResolverService gameResolverService;

    public BattleContestService(
        BattleRunService battleRunService,
        BattleRunRepository battleRunRepository, EloService eloService,
        GameResolverService gameResolverService) {
        this.battleRunService = battleRunService;
        this.battleRunRepository = battleRunRepository;
        this.eloService = eloService;
        this.gameResolverService = gameResolverService;
    }

    public void run(List<User> users, List<GridEntity> gridEntities) {
        List<User> shuffledUsers = new ArrayList<>(users);

        for (GridEntity gridEntity : gridEntities) {
            Collections.shuffle(shuffledUsers);
            for (int i1 = 0; i1 < shuffledUsers.size(); i1++) {
                User user1 = shuffledUsers.get(i1);

                for (int i2 = i1 + 1; i2 < shuffledUsers.size(); i2++) {
                    User user2 = shuffledUsers.get(i2);

                    for (int i3 = i2 + 1; i3 < shuffledUsers.size(); i3++) {
                        User user3 = shuffledUsers.get(i3);

                        for (int i4 = i3 + 1; i4 < shuffledUsers.size(); i4++) {
                            User user4 = shuffledUsers.get(i4);

                            run(user1, user2, user3, user4, gridEntity);
                        }
                    }
                }
            }
        }
    }

    public void run(User user1, User user2, User user3, User user4, GridEntity gridEntity) {
        /**
         * Fights
         * 1234
         * 2413
         * 3142
         * 4321
         */
        runBattle(user1, user2, user3, user4, gridEntity);
        runBattle(user2, user4, user1, user3, gridEntity);
        runBattle(user3, user1, user4, user2, gridEntity);
        runBattle(user4, user3, user2, user1, gridEntity);
    }

    private void runBattle(User user1, User user2, User user3, User user4, GridEntity gridEntity) {
        Grid grid = new Grid(gridEntity);
        try {
            GridResultDto gridResultDto = battleRunService.run(user1, user2, user3, user4, grid);
            List<String> moves = gameResolverService.computeMoves(gridResultDto);
            BattleRun battleRun = new BattleRun(
                gridEntity,
                user1, user2, user3, user4,
                moves
            );
            battleRunRepository.save(battleRun);
            eloService.computeElo(battleRun);

        } catch (GameCancelledException gameCancelledException) {
            BattleRun battleRun = new BattleRun(
                gridEntity,
                user1, user2, user3, user4,
                List.of()
            );
            battleRunRepository.save(battleRun);
            eloService.computeLossElo(battleRun);
        }
    }
}
