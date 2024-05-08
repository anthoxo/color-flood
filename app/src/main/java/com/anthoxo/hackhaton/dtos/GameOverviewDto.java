package com.anthoxo.hackhaton.dtos;

import java.util.List;

public record GameOverviewDto(List<GameDto> soloGames, List<GameDto> versusGames, List<GameDto> battleGames) {
    public record GameDto(Long id, List<String> players) {}
}
