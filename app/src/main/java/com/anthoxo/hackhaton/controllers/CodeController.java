package com.anthoxo.hackhaton.controllers;

import com.anthoxo.hackhaton.dtos.GridResultDto;
import com.anthoxo.hackhaton.services.CodeService;
import com.anthoxo.hackhaton.services.UserService;
import com.anthoxo.hackhaton.services.file.FileCheckerService;
import com.anthoxo.hackhaton.services.game.BattleRoyaleRunService;
import com.anthoxo.hackhaton.services.game.DuelRunService;
import com.anthoxo.hackhaton.services.game.SoloRunService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/codes")
public class CodeController {

    private final FileCheckerService fileCheckerService;
    private final SoloRunService soloRunService;
    private final DuelRunService duelRunService;
    private final CodeService codeService;
    private final BattleRoyaleRunService battleRoyaleRunService;
    private final UserService userService;

    public CodeController(
            FileCheckerService fileCheckerService,
            SoloRunService soloRunService,
            DuelRunService duelRunService, CodeService codeService,
            BattleRoyaleRunService battleRoyaleRunService,
            UserService userService) {
        this.fileCheckerService = fileCheckerService;
        this.soloRunService = soloRunService;
        this.duelRunService = duelRunService;
        this.codeService = codeService;
        this.battleRoyaleRunService = battleRoyaleRunService;
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<PushCodeResponseDto> pushCode(
            @RequestParam("file") MultipartFile file,
            @RequestParam("name") String name,
            @RequestParam("code") String code
    ) {
        codeService.validateCode(file, name, code);
        boolean alreadyExists = userService.exists(name, code);
        codeService.pushCode(file, name, code);
        if (alreadyExists) {
            return ResponseEntity.ok(
                    new PushCodeResponseDto("Your code has been updated."));
        }
        return ResponseEntity.ok(
                new PushCodeResponseDto("Your code has been pushed."));
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

    public record PushCodeResponseDto(String message) {

    }
}
