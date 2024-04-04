package com.anthoxo.hackhaton.controllers;

import com.anthoxo.hackhaton.dtos.GridResultDto;
import com.anthoxo.hackhaton.services.FileCheckerService;
import com.anthoxo.hackhaton.services.game.BattleRoyaleRunService;
import com.anthoxo.hackhaton.services.game.DuelRunService;
import com.anthoxo.hackhaton.services.game.SoloRunService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/codes")
public class CodeController {

    private final FileCheckerService fileCheckerService;
    private final SoloRunService soloRunService;
    private final DuelRunService duelRunService;
    private final BattleRoyaleRunService battleRoyaleRunService;

    public CodeController(
            FileCheckerService fileCheckerService,
            SoloRunService soloRunService,
            DuelRunService duelRunService,
            BattleRoyaleRunService battleRoyaleRunService
    ) {
        this.fileCheckerService = fileCheckerService;
        this.soloRunService = soloRunService;
        this.duelRunService = duelRunService;
        this.battleRoyaleRunService = battleRoyaleRunService;
    }

    @PostMapping
    public void deployCode(DeployCodeRequestDto dto) {

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
        return duelRunService.runAlone(file);
    }

    @PostMapping("/battle")
    public GridResultDto runBattleRoyale(@RequestParam("file") MultipartFile file)
            throws IOException, InterruptedException {
        fileCheckerService.checkFileExtension(file);
        return battleRoyaleRunService.runAlone(file);
    }

    private record DeployCodeRequestDto(String teamName) {

    }
}
