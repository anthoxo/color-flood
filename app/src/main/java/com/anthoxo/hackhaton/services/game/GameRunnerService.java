package com.anthoxo.hackhaton.services.game;

import com.anthoxo.hackhaton.exceptions.AnswerTooLongException;
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
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Service
public class GameRunnerService {

    private static final double MAXIMUM_TIME_FOR_ANSWER_IN_MS = 110;
    private static final double MAXIMUM_TIME_FOR_ANSWER_AT_BEGINNING_IN_MS = 1100;

    public static final String RANDOM_LOCATION = "src/main/resources/examples/RandomExample.java";

    private static final Logger LOGGER = LoggerFactory.getLogger(GameRunnerService.class);


    private final GridService gridService;
    private final FileUtilsService fileUtilsService;
    private final ExecutorService executorService = Executors.newVirtualThreadPerTaskExecutor();

    public GameRunnerService(GridService gridService,
        FileUtilsService fileUtilsService) {
        this.gridService = gridService;
        this.fileUtilsService = fileUtilsService;
    }

    public void run(Game game) throws GameCancelledException {
        run(game, false);
    }

    public void run(Game game, boolean shouldSaveErrorFile)
        throws GameCancelledException {
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
                String unknown = "NONE";
                write(process, unknown);
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
            long start = System.nanoTime();
            try {
                String answer = executorService.submit(
                    () -> scanner.nextLine()
                ).get(2, TimeUnit.SECONDS);
                double elapsedTimeInMs = (System.nanoTime() - start) / 1e6;
                throwExceptionIfTooLong(turn, elapsedTimeInMs);
                Integer res = Integer.valueOf(answer);
                game.run(turn, res);
            } catch (Exception ex) {
                LOGGER.error("Something happens for the player={}, turn={}, ex={}",
                    game.getCurrentPlayer(turn), turn, ex.getMessage());
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

    private void throwExceptionIfTooLong(int turn, double elapsedTimeInMs)
        throws AnswerTooLongException {
        if (
            (turn > 4 && elapsedTimeInMs > MAXIMUM_TIME_FOR_ANSWER_IN_MS) ||
                elapsedTimeInMs > MAXIMUM_TIME_FOR_ANSWER_AT_BEGINNING_IN_MS
        ) {
            throw new AnswerTooLongException(
                "Answer takes too long (" + elapsedTimeInMs + "ms).");
        }

    }
}
