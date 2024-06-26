package com.anthoxo.hackhaton.controllers;

import com.anthoxo.hackhaton.dtos.GridResultDto;
import com.anthoxo.hackhaton.exceptions.GameCancelledException;
import com.anthoxo.hackhaton.services.contest.ContestEventService;
import com.anthoxo.hackhaton.services.file.FileCheckerService;
import com.anthoxo.hackhaton.services.game.BattleRunService;
import com.anthoxo.hackhaton.services.game.VersusRunService;
import com.anthoxo.hackhaton.services.game.SoloRunService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/game/run")
public class GameRunController {

    private final FileCheckerService fileCheckerService;
    private final SoloRunService soloRunService;
    private final VersusRunService versusRunService;
    private final BattleRunService battleRunService;
    private final ContestEventService contestEventService;

    public GameRunController(
        FileCheckerService fileCheckerService,
        SoloRunService soloRunService,
        VersusRunService versusRunService,
        BattleRunService battleRunService,
        ContestEventService contestEventService) {
        this.fileCheckerService = fileCheckerService;
        this.soloRunService = soloRunService;
        this.versusRunService = versusRunService;
        this.battleRunService = battleRunService;
        this.contestEventService = contestEventService;
    }

    @PostMapping("/solo")
    public GridResultDto runSolo(@RequestParam("file") MultipartFile file)
        throws GameCancelledException {
        fileCheckerService.checkFileExtension(file);
        return soloRunService.run(file);
    }

    @PostMapping("/versus")
    public GridResultDto runVersus(@RequestParam("file") MultipartFile file)
        throws GameCancelledException {
        fileCheckerService.checkFileExtension(file);
        return versusRunService.runWithRandom(file);
    }

    @PostMapping("/battle")
    public GridResultDto runBattleRoyale(
        @RequestParam("file") MultipartFile file)
        throws GameCancelledException {
        fileCheckerService.checkFileExtension(file);
        return battleRunService.runAlone(file);
    }

    @PostMapping("/solo/contest")
    public void runSoloContest() {
        contestEventService.publishSoloEvent();
    }

    @PostMapping("/versus/contest")
    public void runVersusContest() {
        contestEventService.publishVersusEvent();
    }

    @PostMapping("/battle/contest")
    public void runBattleContest() {
        contestEventService.publishBattleEvent();
    }
}
