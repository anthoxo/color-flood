package com.anthoxo.hackhaton.dtos;

import com.anthoxo.hackhaton.entities.Ladder;

public record UserDto(Long id, String teamName, double soloElo,
                      double versusElo, double battleElo,
                      double elo) {

    public UserDto(Ladder ladder) {
        this(
            ladder.getUserId(),
            ladder.getUser().getTeamName(),
            ladder.getSoloElo(),
            ladder.getVersusElo(),
            ladder.getBattleElo(),
            (ladder.getSoloElo() + ladder.getVersusElo() + ladder.getBattleElo()) / 3.
        );
    }
}
