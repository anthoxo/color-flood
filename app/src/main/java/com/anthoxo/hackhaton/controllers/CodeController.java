package com.anthoxo.hackhaton.controllers;

import com.anthoxo.hackhaton.services.CodeService;
import com.anthoxo.hackhaton.services.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Locale;

@RestController
@RequestMapping("/api/codes")
public class CodeController {

    private final CodeService codeService;
    private final UserService userService;

    public CodeController(
            CodeService codeService,
            UserService userService) {
        this.codeService = codeService;
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<PushCodeResponseDto> pushCode(
            @RequestParam("file") MultipartFile file,
            @RequestParam("name") String name,
            @RequestParam("code") String code
    ) {
        String lowerName = name.toLowerCase(Locale.ROOT);
        codeService.validateCode(file, lowerName, code);
        boolean alreadyExists = userService.exists(lowerName, code);
        codeService.pushCode(file, lowerName, code);
        if (alreadyExists) {
            return ResponseEntity.ok(
                    new PushCodeResponseDto("Your code has been updated."));
        }
        return ResponseEntity.ok(
                new PushCodeResponseDto("Your code has been pushed."));
    }

    public record PushCodeResponseDto(String message) {

    }
}
