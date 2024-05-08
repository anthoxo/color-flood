package com.anthoxo.hackhaton.services.contest;

import com.anthoxo.hackhaton.dtos.GridResultDto;
import com.anthoxo.hackhaton.entities.GridEntity;
import com.anthoxo.hackhaton.entities.User;
import com.anthoxo.hackhaton.entities.VersusRun;
import com.anthoxo.hackhaton.exceptions.GameCancelledException;
import com.anthoxo.hackhaton.models.Grid;
import com.anthoxo.hackhaton.models.StartingTile;
import com.anthoxo.hackhaton.repositories.VersusRunRepository;
import com.anthoxo.hackhaton.services.game.DuelRunService;
import com.anthoxo.hackhaton.services.ladder.EloService;
import com.anthoxo.hackhaton.utils.ListUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class VersusContestService {

    private final DuelRunService duelRunService;
    private final VersusRunRepository versusRunRepository;
    private final EloService eloService;

    public VersusContestService(DuelRunService duelRunService,
            VersusRunRepository versusRunRepository, EloService eloService) {
        this.duelRunService = duelRunService;
        this.versusRunRepository = versusRunRepository;
        this.eloService = eloService;
    }

    public void run(List<User> users, List<GridEntity> gridEntities) {
        List<User> shuffledUsers = new ArrayList<>(users);
        Collections.shuffle(shuffledUsers);

        int gridCounter = 0;
        for (int i = 0; i < shuffledUsers.size(); i++) {
            User user1 = shuffledUsers.get(i);
            for (int j = i + 1; j < shuffledUsers.size(); j++) {
                User user2 = shuffledUsers.get(j);
                GridEntity gridEntity = gridEntities.get(gridCounter);
                runDuel(user1, user2, gridEntity);
            }
        }
    }

    public void runDuel(User user1, User user2, GridEntity gridEntity) {
        Grid grid1 = new Grid(ListUtils.copy(gridEntity.getGrid()));
        Grid grid2 = new Grid(ListUtils.copy(gridEntity.getGrid()));
        try {
            GridResultDto dto1 = duelRunService.run(user1, user2, grid1);
            GridResultDto dto2 = duelRunService.run(user2, user1, grid2);
            List<String> moves1 = computeMoves(dto1.history());
            List<String> moves2 = computeMoves(dto2.history());
            VersusRun versusRun1 = new VersusRun(
                    gridEntity,
                    user1,
                    user2,
                    moves1
            );
            versusRunRepository.save(versusRun1);
            eloService.computeElo(versusRun1);
            VersusRun versusRun2 = new VersusRun(
                    gridEntity,
                    user2,
                    user1,
                    moves2
            );
            versusRunRepository.save(versusRun2);
            eloService.computeElo(versusRun2);
        } catch (GameCancelledException gameCancelledException) {
            // handle loss for both players
        }

    }

    public List<String> computeMoves(List<Grid> history) {
        List<String> moves = new ArrayList<>();
        for (int i = 0; i < history.size(); i++) {
            StartingTile startingTile = i % 2 == 0 ? StartingTile.TOP_LEFT
                    : StartingTile.BOTTOM_RIGHT;
            moves.add(history.get(i).getCurrentColor(startingTile).toString());
        }
        return moves;
    }
}
