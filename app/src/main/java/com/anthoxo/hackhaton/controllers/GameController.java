package com.anthoxo.hackhaton.controllers;

import com.anthoxo.hackhaton.dtos.GameOverviewDto;
import com.anthoxo.hackhaton.dtos.GridResultDto;
import com.anthoxo.hackhaton.services.game.GameGetService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/game")
public class GameController {

    private final GameGetService gameGetService;

    public GameController(GameGetService gameGetService) {
        this.gameGetService = gameGetService;
    }

    @GetMapping
    public GameOverviewDto getAll() {
        return gameGetService.getAll();
    }

    @GetMapping("/solo/{gameId}")
    public GridResultDto getSoloDetail(@PathVariable Long gameId) {
        return gameGetService.getSoloDetail(gameId);
    }

    @GetMapping("/versus/{gameId}")
    public GridResultDto getVersusDetail(@PathVariable Long gameId) {
        return gameGetService.getVersusDetail(gameId);
    }

    @GetMapping("/battle/{gameId}")
    public GridResultDto getBattleDetail(@PathVariable Long gameId) {
        return gameGetService.getBattleDetail(gameId);
    }
}
