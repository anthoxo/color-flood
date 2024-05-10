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

import java.util.Collections;
import java.util.List;
import java.util.function.BiConsumer;

@Service
public class ContestService {

    private static final Logger LOGGER = LoggerFactory.getLogger(
        ContestService.class);

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

    public void runSoloContest() {
        LOGGER.info("Start solo contest!!");
        runContest(this::runForSolo);
        LOGGER.info("End solo contest!!");
    }

    public void runVersusContest() {
        LOGGER.info("Start versus contest!!");
        runContest(this::runForVersus);
        LOGGER.info("End versus contest!!");
    }

    public void runBattleContest() {
        LOGGER.info("Start battle contest!!");
        runContest(this::runForBattle);
        LOGGER.info("End battle contest!!");
    }

    private void runContest(
        BiConsumer<List<User>, List<GridEntity>> biConsumer) {
        initGrids();

        List<GridEntity> gridEntities = gridRepository.findAll();
        List<User> users = userService.getAll();

        biConsumer.accept(users, gridEntities);
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
            .limit(12)
            .toList());
    }

    private void runForVersus(List<User> users, List<GridEntity> gridEntities) {
        Collections.shuffle(gridEntities);
        versusContestService.run(users, gridEntities
            .stream()
            .limit(8)
            .toList());
    }

    private void runForBattle(List<User> users, List<GridEntity> gridEntities) {
        Collections.shuffle(gridEntities);
        battleContestService.run(users, gridEntities
            .stream()
            .limit(8)
            .toList());
    }
}
