package com.anthoxo.hackhaton.controllers;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/codes")
public class CodeController {
  @PostMapping
  public void deployCode(DeployCodeRequestDto dto) {

  }

  private record DeployCodeRequestDto(String teamName) { }
}
