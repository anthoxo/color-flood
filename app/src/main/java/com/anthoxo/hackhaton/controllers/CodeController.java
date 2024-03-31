package com.anthoxo.hackhaton.controllers;

import com.anthoxo.hackhaton.services.FileCheckerService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/codes")
public class CodeController {

    private final FileCheckerService fileCheckerService;

    public CodeController(FileCheckerService fileCheckerService) {
        this.fileCheckerService = fileCheckerService;
    }

    @PostMapping
    public void deployCode(DeployCodeRequestDto dto) {

    }

    @PostMapping("/solo")
    public void runSolo(@RequestParam("file") MultipartFile file) {
        fileCheckerService.checkFileExtension(file);
    }

    private record DeployCodeRequestDto(String teamName) {

    }
}
