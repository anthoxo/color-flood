package com.anthoxo.hackhaton.services.contest;

import com.anthoxo.hackhaton.dtos.GridResultDto;
import com.anthoxo.hackhaton.entities.BattleRun;
import com.anthoxo.hackhaton.entities.GridEntity;
import com.anthoxo.hackhaton.entities.User;
import com.anthoxo.hackhaton.exceptions.GameCancelledException;
import com.anthoxo.hackhaton.models.Grid;
import com.anthoxo.hackhaton.models.StartingTile;
import com.anthoxo.hackhaton.repositories.BattleRunRepository;
import com.anthoxo.hackhaton.services.game.BattleRoyaleRunService;
import com.anthoxo.hackhaton.services.ladder.EloService;
import com.anthoxo.hackhaton.utils.ListUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class BattleRoyaleContestService {

    private final BattleRoyaleRunService battleRoyaleRunService;
    private final BattleRunRepository battleRunRepository;
    private final EloService eloService;

    public BattleRoyaleContestService(
        BattleRoyaleRunService battleRoyaleRunService,
        BattleRunRepository battleRunRepository, EloService eloService) {
        this.battleRoyaleRunService = battleRoyaleRunService;
        this.battleRunRepository = battleRunRepository;
        this.eloService = eloService;
    }

    public void run(List<User> users, List<GridEntity> gridEntities) {
        List<User> shuffledUsers = new ArrayList<>(users);
        Collections.shuffle(shuffledUsers);

        int gridCounter = 0;
        for (int i1 = 0; i1 < shuffledUsers.size(); i1++) {
            User user1 = shuffledUsers.get(i1);

            for (int i2 = i1 + 1; i2 < shuffledUsers.size(); i2++) {
                User user2 = shuffledUsers.get(i2);

                for (int i3 = i2 + 1; i3 < shuffledUsers.size(); i3++) {
                    User user3 = shuffledUsers.get(i3);

                    for (int i4 = i3 + 1; i4 < shuffledUsers.size(); i4++) {
                        User user4 = shuffledUsers.get(i4);

                        GridEntity gridEntity = gridEntities.get(gridCounter);
                        runBattleRoyale(user1, user2, user3, user4, gridEntity);
                    }
                }
            }
        }
    }

    public void runBattleRoyale(User user1, User user2, User user3, User user4, GridEntity gridEntity) {
        Grid grid1 = new Grid(ListUtils.copy(gridEntity.getGrid()));
        Grid grid2 = new Grid(ListUtils.copy(gridEntity.getGrid()));
        Grid grid3 = new Grid(ListUtils.copy(gridEntity.getGrid()));
        Grid grid4 = new Grid(ListUtils.copy(gridEntity.getGrid()));
        /**
         * Fights
         * 1234
         * 2413
         * 3142
         * 4321
         */
        try {
            GridResultDto dto1 = battleRoyaleRunService.run(user1, user2, user3, user4, grid1);
            GridResultDto dto2 = battleRoyaleRunService.run(user2, user4, user1, user3, grid2);
            GridResultDto dto3 = battleRoyaleRunService.run(user3, user1, user4, user2, grid3);
            GridResultDto dto4 = battleRoyaleRunService.run(user4, user3, user2, user1, grid4);
            List<String> moves1 = computeMoves(dto1.history());
            List<String> moves2 = computeMoves(dto2.history());
            List<String> moves3 = computeMoves(dto3.history());
            List<String> moves4 = computeMoves(dto4.history());
            BattleRun battleRun1 = new BattleRun(
                gridEntity,
                user1, user2, user3, user4,
                moves1
            );
            battleRunRepository.save(battleRun1);
            eloService.computeElo(battleRun1);
            BattleRun battleRun2 = new BattleRun(
                gridEntity,
                user2, user4, user1, user3,
                moves2
            );
            battleRunRepository.save(battleRun2);
            eloService.computeElo(battleRun2);
            BattleRun battleRun3 = new BattleRun(
                gridEntity,
                user3, user1, user4, user2,
                moves3
            );
            battleRunRepository.save(battleRun3);
            eloService.computeElo(battleRun3);
            BattleRun battleRun4 = new BattleRun(
                gridEntity,
                user4, user3, user2, user1,
                moves4
            );
            battleRunRepository.save(battleRun4);
            eloService.computeElo(battleRun4);
        } catch (GameCancelledException gameCancelledException) {
            // handle loss for both players
        }

    }

    public List<String> computeMoves(List<Grid> history) {
        List<String> moves = new ArrayList<>();
        for (int i = 0; i < history.size() - 1; i++) {
            Grid grid = history.get(i+1);
            StartingTile startingTile = switch (i % 4) {
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
}
