package com.anthoxo.hackhaton.services;

import com.anthoxo.hackhaton.dtos.GridResultDto;
import com.anthoxo.hackhaton.models.Game;
import com.anthoxo.hackhaton.models.Grid;
import com.anthoxo.hackhaton.models.Player;
import com.anthoxo.hackhaton.models.StartingTile;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class SoloRunService {

    private final GridService gridService;
    private final CodeRunnerService codeRunnerService;

    public SoloRunService(GridService gridService, CodeRunnerService codeRunnerService) {
        this.gridService = gridService;
        this.codeRunnerService = codeRunnerService;
    }

    public GridResultDto run(MultipartFile multipartFile)
            throws IOException, InterruptedException {
        UUID uuid = UUID.randomUUID();
        String tmpFilename = String.format("src/main/resources/tmp/%s-%s", uuid, multipartFile.getOriginalFilename());
        File file = new File(tmpFilename);
        saveTemporaryFile(file, multipartFile);

        int size = 20;
        int numberOfColors = 3;

        Grid initialGrid = gridService.init(size, numberOfColors);
        Player playerOne = new Player("local", StartingTile.TOP_LEFT, initialGrid);
        Game game = new Game(List.of(playerOne), initialGrid);
        codeRunnerService.run(tmpFilename, game);
        cleanTemporaryFile(file);

        return new GridResultDto(game.getHistory(), game.getPlayers()
                .stream()
                .map(player -> new GridResultDto.Statistic(player.name(), player.currentColor()))
                .collect(Collectors.toList()));
    }

    private void saveTemporaryFile(File destinationFile, MultipartFile multipartFile) {
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
