package com.anthoxo.hackhaton.controllers;

import com.anthoxo.hackhaton.dtos.UserDto;
import com.anthoxo.hackhaton.services.UserService;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
public class UserController {

  private final UserService userService;

  public UserController(UserService userService) {
    this.userService = userService;
  }

  @GetMapping
  public List<UserDto> getUsers() {
    return userService.getUsers();
  }

  @PostMapping
  public void createUser(@RequestBody CreateUserDto dto) {
    userService.createUser(dto.teamName, "1234");
  }

  private record CreateUserDto(String teamName) {}
}
