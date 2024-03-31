package com.anthoxo.hackhaton.controllers;

import com.anthoxo.hackhaton.dtos.GridResultDto;
import com.anthoxo.hackhaton.services.FileCheckerService;
import com.anthoxo.hackhaton.services.game.SoloRunService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/codes")
public class CodeController {

    private final FileCheckerService fileCheckerService;
    private final SoloRunService soloRunService;

    public CodeController(
            FileCheckerService fileCheckerService,
            SoloRunService soloRunService
    ) {
        this.fileCheckerService = fileCheckerService;
        this.soloRunService = soloRunService;
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

    private record DeployCodeRequestDto(String teamName) {

    }
}
