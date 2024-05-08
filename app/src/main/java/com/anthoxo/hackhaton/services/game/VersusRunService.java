package com.anthoxo.hackhaton.services.game;

import com.anthoxo.hackhaton.dtos.GridResultDto;
import com.anthoxo.hackhaton.entities.User;
import com.anthoxo.hackhaton.exceptions.GameCancelledException;
import com.anthoxo.hackhaton.models.Game;
import com.anthoxo.hackhaton.models.Grid;
import com.anthoxo.hackhaton.models.Player;
import com.anthoxo.hackhaton.models.StartingTile;
import com.anthoxo.hackhaton.services.file.FileUtilsService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.List;

@Service
public class VersusRunService {

    private final GridService gridService;
    private final FileUtilsService fileUtilsService;
    private final GameRunnerService gameRunnerService;
    private final GameStatisticsService gameStatisticsService;

    public VersusRunService(GridService gridService,
            FileUtilsService fileUtilsService,
            GameRunnerService gameRunnerService,
            GameStatisticsService gameStatisticsService) {
        this.gridService = gridService;
        this.fileUtilsService = fileUtilsService;
        this.gameRunnerService = gameRunnerService;
        this.gameStatisticsService = gameStatisticsService;
    }

    public GridResultDto runWithRandom(MultipartFile multipartFile)
            throws GameCancelledException {
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

    public GridResultDto run(User user1, User user2, Grid grid)
            throws GameCancelledException {
        Player playerOne = new Player(
                user1.getId().toString(),
                StartingTile.TOP_LEFT,
                user1.getCodeFilename()
        );
        Player playerTwo = new Player(
                user2.getId().toString(),
                StartingTile.BOTTOM_RIGHT,
                user2.getCodeFilename()
        );
        Game game = new Game(List.of(playerOne, playerTwo), grid);
        gameRunnerService.run(game);
        return new GridResultDto(
                game.getHistory(),
                gameStatisticsService.getStatistics(game)
        );
    }
}
