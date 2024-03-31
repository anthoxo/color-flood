package com.anthoxo.hackhaton.services;

import com.anthoxo.hackhaton.models.Game;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Scanner;
import java.util.stream.Collectors;

@Service
public class CodeRunnerService {

    public void run(String pathFile, Game game)
            throws IOException, InterruptedException {
        int size = game.getSize();
        ProcessBuilder processBuilder = new ProcessBuilder("java", pathFile);
        processBuilder.redirectErrorStream(true);
        Process process = processBuilder.start();

        process.outputWriter().write(size + "\n");
        process.outputWriter().flush();

        Scanner scanner = new Scanner(process.getInputStream());
        int turn = 0;
        do {
            for (int i = 0; i < size; ++i) {
                String line = game.getGrid().colors().get(i).stream().map(String::valueOf).collect(Collectors.joining(","));
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
