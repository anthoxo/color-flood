package com.anthoxo.hackhaton.services.contest;

import com.anthoxo.hackhaton.entities.GridEntity;
import com.anthoxo.hackhaton.entities.User;
import com.anthoxo.hackhaton.models.Grid;
import com.anthoxo.hackhaton.repositories.GridRepository;
import com.anthoxo.hackhaton.services.UserService;
import com.anthoxo.hackhaton.services.game.GridService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

@Service
public class ContestService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ContestService.class);

    private final GridService gridService;
    private final GridRepository gridRepository;
    private final UserService userService;
    private final SoloContestService soloContestService;
    private final VersusContestService versusContestService;
    private final BattleContestService battleContestService;

    public ContestService(GridService gridService,
            GridRepository gridRepository,
            UserService userService,
            SoloContestService soloContestService,
            VersusContestService versusContestService,
        BattleContestService battleContestService) {
        this.gridService = gridService;
        this.gridRepository = gridRepository;
        this.userService = userService;
        this.soloContestService = soloContestService;
        this.versusContestService = versusContestService;
        this.battleContestService = battleContestService;
    }

    @Transactional(rollbackFor = Exception.class)
    public void runContest() {
        LOGGER.info("Start computing contest!!");
        initGrids();

        List<GridEntity> gridEntities = gridRepository.findAll();
        List<User> users = userService.getAll();

        runForSolo(users, gridEntities);
        runForVersus(users, gridEntities);
        runForBattle(users, gridEntities);
        LOGGER.info("End computing contest!!");
    }

    private void initGrids() {
        if (gridRepository.count() > 0) {
            return;
        }
        for (int i = 0; i < 200; ++i) {
            Grid grid = gridService.init(6);
            GridEntity gridEntity = new GridEntity(grid.colors());
            gridRepository.save(gridEntity);
        }
    }

    private void runForSolo(List<User> users, List<GridEntity> gridEntities) {
        Collections.shuffle(gridEntities);
        soloContestService.run(users, gridEntities
                .stream()
                .limit(10)
                .toList());
    }

    private void runForVersus(List<User> users, List<GridEntity> gridEntities) {
        Collections.shuffle(gridEntities);
        versusContestService.run(users, gridEntities);
    }

    private void runForBattle(List<User> users, List<GridEntity> gridEntities) {
        Collections.shuffle(gridEntities);
        battleContestService.run(users, gridEntities);
    }
}
