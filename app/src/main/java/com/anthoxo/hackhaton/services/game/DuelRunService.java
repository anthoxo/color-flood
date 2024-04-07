package com.anthoxo.hackhaton.services.game;

import com.anthoxo.hackhaton.dtos.GridResultDto;
import com.anthoxo.hackhaton.models.Game;
import com.anthoxo.hackhaton.models.Grid;
import com.anthoxo.hackhaton.models.Player;
import com.anthoxo.hackhaton.models.StartingTile;
import com.anthoxo.hackhaton.services.file.FileUtilsService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;

@Service
public class DuelRunService {

    private final GridService gridService;
    private final FileUtilsService fileUtilsService;
    private final GameRunnerService gameRunnerService;
    private final GameStatisticsService gameStatisticsService;

    public DuelRunService(GridService gridService,
            FileUtilsService fileUtilsService,
            GameRunnerService gameRunnerService,
            GameStatisticsService gameStatisticsService) {
        this.gridService = gridService;
        this.fileUtilsService = fileUtilsService;
        this.gameRunnerService = gameRunnerService;
        this.gameStatisticsService = gameStatisticsService;
    }

    public GridResultDto runWithRandom(MultipartFile multipartFile)
            throws IOException, InterruptedException {
        File file = fileUtilsService.generateTmpFile(multipartFile);

        Grid initialGrid = gridService.init(4);
        Player playerOne = new Player(
                "local-1",
                StartingTile.TOP_LEFT,
                file.getPath()
        );
        Player playerTwo = new Player(
                "random-example",
                StartingTile.BOTTOM_RIGHT,
                GameRunnerService.RANDOM_LOCATION
        );
        Game game = new Game(List.of(playerOne, playerTwo), initialGrid);

        try {
            gameRunnerService.run(game);
        } finally {
            fileUtilsService.deleteFile(file);
        }

        return new GridResultDto(
                game.getHistory(),
                gameStatisticsService.getStatistics(game)
        );
    }
}
