package com.anthoxo.hackhaton.services.game;

import com.anthoxo.hackhaton.dtos.GridResultDto;
import com.anthoxo.hackhaton.entities.GridEntity;
import com.anthoxo.hackhaton.entities.SoloRun;
import com.anthoxo.hackhaton.entities.User;
import com.anthoxo.hackhaton.exceptions.GameCancelledException;
import com.anthoxo.hackhaton.models.Game;
import com.anthoxo.hackhaton.models.Grid;
import com.anthoxo.hackhaton.models.Player;
import com.anthoxo.hackhaton.models.StartingTile;
import com.anthoxo.hackhaton.repositories.SoloRunRepository;
import com.anthoxo.hackhaton.services.file.FileUtilsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.List;

@Service
public class SoloRunService {

    private final GridService gridService;
    private final FileUtilsService fileUtilsService;
    private final GameRunnerService gameRunnerService;
    private final GameStatisticsService gameStatisticsService;
    private final SoloRunRepository soloRunRepository;

    public SoloRunService(
            GridService gridService,
            FileUtilsService fileUtilsService,
            GameRunnerService gameRunnerService,
            GameStatisticsService gameStatisticsService,
            SoloRunRepository soloRunRepository) {
        this.gridService = gridService;
        this.fileUtilsService = fileUtilsService;
        this.gameRunnerService = gameRunnerService;
        this.gameStatisticsService = gameStatisticsService;
        this.soloRunRepository = soloRunRepository;
    }

    public GridResultDto run(MultipartFile multipartFile)
            throws GameCancelledException {
        File file = fileUtilsService.generateTmpFile(multipartFile);

        try {
            return run(file, false);
        } finally {
            fileUtilsService.deleteFile(file);
        }
    }

    public GridResultDto run(File file, boolean shouldSaveErrorFile) throws GameCancelledException {
        Grid initialGrid = gridService.init(2);
        Player playerOne = new Player(
            "local",
            StartingTile.TOP_LEFT,
            file.getPath()
        );
        Game game = new Game(List.of(playerOne), initialGrid);

        gameRunnerService.run(game, shouldSaveErrorFile);

        return new GridResultDto(
            game.getHistory(),
            gameStatisticsService.getStatistics(game)
        );
    }

    public GridResultDto run(User user, Grid grid) throws GameCancelledException {
        Player playerOne = new Player(
                user.getId().toString(),
                StartingTile.TOP_LEFT,
                user.getCodeFilename()
        );
        Game game = new Game(List.of(playerOne), grid);
        gameRunnerService.run(game);
        return new GridResultDto(
                game.getHistory(),
                gameStatisticsService.getStatistics(game)
        );
    }

    @Transactional
    public void save(User user, GridEntity gridEntity, List<String> moves) {
        SoloRun soloRun = new SoloRun(user, gridEntity, moves);
        soloRunRepository.save(soloRun);
    }

    public List<SoloRun> findAllByGrid(GridEntity grid) {
        return soloRunRepository.findAllByGrid(grid);
    }
}
