package com.anthoxo.hackhaton.dtos;

import com.anthoxo.hackhaton.entities.User;

public record UserDto(Long id, String teamName, double elo) {

    public static UserDto toDto(User user) {
        return new UserDto(user.getId(), user.getTeamName(),
                user.getLadder().getElo());
    }
}
