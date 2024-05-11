package com.anthoxo.hackhaton.services.game;

import com.anthoxo.hackhaton.exceptions.GameCancelledException;
import com.anthoxo.hackhaton.models.Game;
import com.anthoxo.hackhaton.models.Grid;
import com.anthoxo.hackhaton.models.Player;
import com.anthoxo.hackhaton.services.file.FileUtilsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;

@Service
public class GameRunnerService {

    public static final String RANDOM_LOCATION = "src/main/resources/examples/RandomExample.java";

    private static final Logger LOGGER = LoggerFactory.getLogger(
        GameRunnerService.class);

    private final GridService gridService;
    private final FileUtilsService fileUtilsService;

    public GameRunnerService(GridService gridService,
        FileUtilsService fileUtilsService) {
        this.gridService = gridService;
        this.fileUtilsService = fileUtilsService;
    }

    public void run(Game game) throws GameCancelledException {
        run(game, false);
    }

    public void run(Game game, boolean shouldSaveErrorFile) throws GameCancelledException {
        List<Process> processes = game.getPlayers().stream()
            .map(player -> {
                ProcessBuilder processBuilder = new ProcessBuilder(
                    fileUtilsService.getExtensionOrThrow(player.pathFile())
                        .getCommandRunner(),
                    player.pathFile());
                if (shouldSaveErrorFile) {
                    processBuilder.redirectError(new File("error.txt"));
                } else {
                    processBuilder.redirectErrorStream(true);
                }
                try {
                    return processBuilder.start();
                } catch (IOException e) {
                    LOGGER.error(e.getMessage(), e);
                    throw new IllegalArgumentException(
                        "Error occurs when open the file.", e);
                }
            }).toList();

        int size = game.getSize();
        int numberOfPlayers = game.getPlayers().size();
        try {
            for (Process process : processes) {
                write(process, String.valueOf(size));
                write(process, String.valueOf(numberOfPlayers));
            }
        } catch (IOException ioException) {
            throw new GameCancelledException(ioException);
        }

        int turn = 0;
        do {
            Player player = game.getCurrentPlayer(turn);
            if (player.isGameOver()) {
                turn++;
                game.saveHistory();
                continue;
            }
            Process process = processes.get(turn % game.getPlayers().size());
            Scanner scanner = new Scanner(process.getInputStream());
            Grid rotatedGrid = gridService.rotateGridIfNeeded(game.getGrid(),
                player.startingTile());
            List<String> lines = gridService.getFormatGridForProgram(
                rotatedGrid);
            try {
                for (String line : lines) {
                    write(process, line);
                }
            } catch (IOException ioException) {
                LOGGER.error(
                    "Error occurs when writing the grid. player={}, ex={}",
                    player, ioException.getMessage());
                game.gameOver(turn);
                turn++;
                continue;
            }
            try {
                String answer = scanner.nextLine();
                Integer res = Integer.valueOf(answer);
                game.run(turn, res);
            } catch (Exception ex) {
                LOGGER.error("Something happens for the player={}, ex={}", game.getCurrentPlayer(turn), ex.getMessage());
                game.gameOver(turn);
            }
            if (game.isFinished()) {
                break;
            }
            ++turn;
        } while (turn < 200);

        for (Process process : processes) {
            try {
                process.outputWriter().close();
                process.destroy();
                process.waitFor();
            } catch (IOException ioException) {
                LOGGER.warn("error when closing the process");
            } catch (InterruptedException interruptedException) {
                throw new IllegalStateException(
                    "should be exit before interrupted");
            }
        }
    }

    private void write(Process process, String text) throws IOException {
        process.outputWriter().write(text + "\n");
        process.outputWriter().flush();
    }
}
