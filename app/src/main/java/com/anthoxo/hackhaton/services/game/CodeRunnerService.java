package com.anthoxo.hackhaton.services.game;

import com.anthoxo.hackhaton.models.Game;
import com.anthoxo.hackhaton.models.Grid;
import com.anthoxo.hackhaton.models.Player;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Scanner;

@Service
public class CodeRunnerService {

    private static final Logger LOGGER = LoggerFactory.getLogger(
            CodeRunnerService.class);

    private final GridService gridService;

    public CodeRunnerService(GridService gridService) {
        this.gridService = gridService;
    }

    public void run(Game game)
            throws IOException, InterruptedException {
        ProcessBuilder processBuilder1 = new ProcessBuilder("java",
                game.getPlayers().get(0).pathFile());
        ProcessBuilder processBuilder2 = new ProcessBuilder("java",
                game.getPlayers().get(1).pathFile());
        processBuilder1.redirectErrorStream(true);
        processBuilder2.redirectErrorStream(true);
        List<Process> processes = game.getPlayers().stream()
                .map(player -> {
                    ProcessBuilder processBuilder = new ProcessBuilder("java",
                            player.pathFile());
                    processBuilder.redirectErrorStream(true);
                    try {
                        return processBuilder.start();
                    } catch (IOException e) {
                        LOGGER.error(e.getMessage(), e);
                        throw new IllegalArgumentException(
                                "Error occurs when open the file.", e);
                    }
                }).toList();

        int size = game.getSize();
        for (Process process : processes) {
            write(process, size + "\n");
        }

        int turn = 0;
        do {
            Process process = processes.get(turn % 2);
            Player player = game.getPlayers().get(turn % 2);
            Scanner scanner = new Scanner(process.getInputStream());
            Grid rotatedGrid = gridService.rotateGridIfNeeded(game.getGrid(),
                    player.startingTile());
            List<String> lines = gridService.getFormatGridForProgram(
                    rotatedGrid);
            for (String line : lines) {
                write(process, line + "\n");
            }
            try {
                String answer = scanner.nextLine();
                Integer res = Integer.valueOf(answer);
                game.run(turn, res);
            } catch (Exception ex) {
                // handle exception here?
                break;
            }
            if (game.isFinished()) {
                break;
            }
            ++turn;
        } while (turn < 200);

        for (Process process : processes) {
            process.outputWriter().close();
            process.destroy();
            int exitCode = process.waitFor();
        }
    }

    private void write(Process process, String text) throws IOException {
        process.outputWriter().write(text + "\n");
        process.outputWriter().flush();
    }
}
