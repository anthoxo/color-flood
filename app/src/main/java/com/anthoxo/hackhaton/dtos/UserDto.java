package com.anthoxo.hackhaton.dtos;

import com.anthoxo.hackhaton.models.User;

public record UserDto(Long id, String teamName, float elo,
                      int fightCount) {

  public static UserDto toDto(User user) {
    return new UserDto(user.getId(), user.getTeamName(),
        user.getLadder().getElo(), user.getLadder().getFightCount());
  }
}
