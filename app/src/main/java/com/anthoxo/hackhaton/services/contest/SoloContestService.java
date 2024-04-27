package com.anthoxo.hackhaton.services.contest;

import com.anthoxo.hackhaton.dtos.GridResultDto;
import com.anthoxo.hackhaton.entities.GridEntity;
import com.anthoxo.hackhaton.entities.SoloRun;
import com.anthoxo.hackhaton.entities.User;
import com.anthoxo.hackhaton.models.Grid;
import com.anthoxo.hackhaton.models.StartingTile;
import com.anthoxo.hackhaton.services.game.SoloRunService;
import com.anthoxo.hackhaton.services.ladder.EloService;
import com.anthoxo.hackhaton.utils.ListUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SoloContestService {

    private static final Logger LOGGER = LoggerFactory.getLogger(SoloContestService.class);

    private final SoloRunService soloRunService;
    private final EloService eloService;

    public SoloContestService(SoloRunService soloRunService,
            EloService eloService) {
        this.soloRunService = soloRunService;
        this.eloService = eloService;
    }

    public void run(List<User> users, List<GridEntity> gridEntities) {
        gridEntities.forEach(gridEntity -> {
            users.forEach(user -> {
                Grid grid = new Grid(ListUtils.copy(gridEntity.getGrid()));
                GridResultDto gridResultDto;
                try {
                    gridResultDto = soloRunService.run(user, grid);
                } catch (Exception ex) {
                    LOGGER.error(
                            "Something happen during the running, move set to 300.");
                    gridResultDto = new GridResultDto(
                            List.of(),
                            List.of(new GridResultDto.Statistic(
                                    user.getId().toString(),
                                    1,
                                    -1,
                                    0,
                                    StartingTile.TOP_LEFT
                            ))
                    );
                }

                List<String> moves = gridResultDto.history().stream()
                        .skip(1)
                        .map(hist -> hist.getCurrentColor(StartingTile.TOP_LEFT))
                        .map(String::valueOf)
                        .toList();

                soloRunService.save(user, gridEntity, moves);
            });
            List<SoloRun> soloRuns = soloRunService.findAllByGrid(gridEntity);
            eloService.computeElo(soloRuns);
        });
    }
}
