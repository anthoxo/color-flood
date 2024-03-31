package com.anthoxo.hackhaton.services;

import com.anthoxo.hackhaton.dtos.UserDto;
import com.anthoxo.hackhaton.entities.Ladder;
import com.anthoxo.hackhaton.entities.User;
import com.anthoxo.hackhaton.repositories.LadderRepository;
import com.anthoxo.hackhaton.repositories.UserRepository;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {

  private final UserRepository userRepository;
  private final LadderRepository ladderRepository;

  public UserService(
      UserRepository userRepository,
      LadderRepository ladderRepository
  ) {
    this.userRepository = userRepository;
    this.ladderRepository = ladderRepository;
  }

  public List<UserDto> getUsers() {
    return userRepository.findAll()
        .stream()
        .map(UserDto::toDto)
        .collect(Collectors.toList());
  }

  @Transactional(rollbackFor = Exception.class)
  public void createUser(String teamName) {
    if (userRepository.existsByTeamName(teamName)) {
      throw new IllegalArgumentException("Team name exists already.");
    }

    User user = userRepository.save(new User(teamName));
    Ladder ladder = new Ladder(user);
    user.setLadder(ladder);
    ladderRepository.save(ladder);
    userRepository.save(user);
  }
}
