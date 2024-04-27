package com.anthoxo.hackhaton.controllers;

import com.anthoxo.hackhaton.dtos.GridResultDto;
import com.anthoxo.hackhaton.services.file.FileCheckerService;
import com.anthoxo.hackhaton.services.game.BattleRoyaleRunService;
import com.anthoxo.hackhaton.services.contest.ContestService;
import com.anthoxo.hackhaton.services.game.DuelRunService;
import com.anthoxo.hackhaton.services.game.SoloRunService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/game/run")
public class GameRunController {

    private final FileCheckerService fileCheckerService;
    private final SoloRunService soloRunService;
    private final DuelRunService duelRunService;
    private final BattleRoyaleRunService battleRoyaleRunService;
    private final ContestService contestService;

    public GameRunController(
            FileCheckerService fileCheckerService,
            SoloRunService soloRunService,
            DuelRunService duelRunService,
            BattleRoyaleRunService battleRoyaleRunService,
            ContestService contestService) {
        this.fileCheckerService = fileCheckerService;
        this.soloRunService = soloRunService;
        this.duelRunService = duelRunService;
        this.battleRoyaleRunService = battleRoyaleRunService;
        this.contestService = contestService;
    }

    @PostMapping("/solo")
    public GridResultDto runSolo(@RequestParam("file") MultipartFile file)
            throws IOException, InterruptedException {
        fileCheckerService.checkFileExtension(file);
        return soloRunService.run(file);
    }

    @PostMapping("/versus")
    public GridResultDto runVersus(@RequestParam("file") MultipartFile file)
            throws IOException, InterruptedException {
        fileCheckerService.checkFileExtension(file);
        return duelRunService.runWithRandom(file);
    }

    @PostMapping("/battle")
    public GridResultDto runBattleRoyale(
            @RequestParam("file") MultipartFile file)
            throws IOException, InterruptedException {
        fileCheckerService.checkFileExtension(file);
        return battleRoyaleRunService.runAlone(file);
    }

    @PostMapping("/contest")
    public void runContest() {
        contestService.runContest();
    }
}
