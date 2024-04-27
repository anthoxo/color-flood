package com.anthoxo.hackhaton.services;

import com.anthoxo.hackhaton.entities.Ladder;
import com.anthoxo.hackhaton.entities.User;
import com.anthoxo.hackhaton.repositories.LadderRepository;
import com.anthoxo.hackhaton.repositories.UserRepository;

import java.util.List;
import java.util.Optional;
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

  public boolean exists(String name, String password) {
    return userRepository.existsByTeamNameAndPassword(name, password);
  }

  public Optional<User> getUser(String name, String password) {
    return userRepository.findByTeamNameAndPassword(name, password);
  }

  public List<User> getAll() {
    return userRepository.findAll();
  }

  public void saveUser(User user) {
    userRepository.save(user);
  }

  @Transactional(rollbackFor = Exception.class)
  public void createUser(String teamName, String code) {
    if (userRepository.existsByTeamName(teamName)) {
      throw new IllegalArgumentException("Team name exists already.");
    }

    User user = userRepository.save(new User(teamName, code));
    Ladder ladder = new Ladder(user);
    user.setLadder(ladder);
    ladderRepository.save(ladder);
    userRepository.save(user);
  }
}
