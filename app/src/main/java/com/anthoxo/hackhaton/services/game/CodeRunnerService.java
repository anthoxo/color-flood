package com.anthoxo.hackhaton.services.game;

import com.anthoxo.hackhaton.models.Game;
import com.anthoxo.hackhaton.models.Grid;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

@Service
public class CodeRunnerService {

    private final GridService gridService;

    public CodeRunnerService(GridService gridService) {
        this.gridService = gridService;
    }

    public void run(Game game)
            throws IOException, InterruptedException {
        ProcessBuilder processBuilder = new ProcessBuilder("java", game.getPlayers().get(0).pathFile());
        processBuilder.redirectErrorStream(true);
        Process process = processBuilder.start();

        int size = game.getSize();
        process.outputWriter().write(size + "\n");
        process.outputWriter().flush();

        Scanner scanner = new Scanner(process.getInputStream());
        int turn = 0;
        do {
            Grid rotatedGrid = gridService.rotateGridIfNeeded(game.getGrid(), game.getPlayers().get(0).startingTile());
            List<String> lines = gridService.getFormatGridForProgram(rotatedGrid);
            for (String line : lines) {
                process.outputWriter().write(line + "\n");
                process.outputWriter().flush();
            }
            String answer = scanner.nextLine();
            Integer res = Integer.valueOf(answer);
            game.run(turn, res);
            if (game.isFinished()) {
                break;
            }
            ++turn;
        } while (turn < 200);

        process.outputWriter().close();
        process.destroy();
        int exitCode = process.waitFor();
    }
}
