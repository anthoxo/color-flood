package com.anthoxo.hackhaton.services.game;

import com.anthoxo.hackhaton.dtos.GridResultDto;
import com.anthoxo.hackhaton.models.Game;
import com.anthoxo.hackhaton.models.Grid;
import com.anthoxo.hackhaton.models.Player;
import com.anthoxo.hackhaton.models.StartingTile;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.UUID;

@Service
public class BattleRoyaleRunService {

    private final GridService gridService;
    private final CodeRunnerService codeRunnerService;
    private final GameStatisticsService gameStatisticsService;

    public BattleRoyaleRunService(GridService gridService,
            CodeRunnerService codeRunnerService,
            GameStatisticsService gameStatisticsService) {
        this.gridService = gridService;
        this.codeRunnerService = codeRunnerService;
        this.gameStatisticsService = gameStatisticsService;
    }

    public GridResultDto runAlone(MultipartFile multipartFile)
            throws IOException, InterruptedException {
        UUID uuid = UUID.randomUUID();
        String tmpFilename = String.format("src/main/resources/tmp/%s-%s", uuid,
                multipartFile.getOriginalFilename());
        File file = new File(tmpFilename);
        saveTemporaryFile(file, multipartFile);

        int size = 10;
        int numberOfColors = 5;

        Grid initialGrid = gridService.init(size, numberOfColors);
        Player playerOne = new Player(
                "local-1",
                StartingTile.TOP_LEFT,
                tmpFilename
        );
        Player playerTwo = new Player(
                "local-2",
                StartingTile.BOTTOM_RIGHT,
                tmpFilename
        );
        Player playerThree = new Player(
                "local-3",
                StartingTile.TOP_RIGHT,
                tmpFilename
        );
        Player playerFour = new Player(
                "local-4",
                StartingTile.BOTTOM_LEFT,
                tmpFilename
        );
        Game game = new Game(List.of(playerOne, playerTwo, playerThree, playerFour), initialGrid);

        try {
            codeRunnerService.run(game);
        } finally {
            cleanTemporaryFile(file);
        }

        return new GridResultDto(
                game.getHistory(),
                gameStatisticsService.getStatistics(game)
        );
    }

    private void saveTemporaryFile(
            File destinationFile,
            MultipartFile multipartFile
    ) {
        try (OutputStream os = new FileOutputStream(destinationFile)) {
            os.write(multipartFile.getBytes());
        } catch (IOException e) {
            throw new IllegalArgumentException("File could not be written", e);
        }
    }

    private void cleanTemporaryFile(File file) {
        file.delete();
    }
}
