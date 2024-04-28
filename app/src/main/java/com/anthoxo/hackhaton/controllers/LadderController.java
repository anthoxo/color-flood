package com.anthoxo.hackhaton.controllers;

import com.anthoxo.hackhaton.dtos.UserDto;
import com.anthoxo.hackhaton.services.ladder.LadderService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/ladder")
public class LadderController {

    private final LadderService ladderService;

    public LadderController(LadderService ladderService) {
        this.ladderService = ladderService;
    }

    @GetMapping
    public List<UserDto> getLadder() {
        return ladderService.getLadder();
    }
}
