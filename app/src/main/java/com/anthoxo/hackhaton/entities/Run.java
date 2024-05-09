package com.anthoxo.hackhaton.entities;

import java.util.List;

public sealed interface Run permits SoloRun, VersusRun, BattleRun {

    Long getId();

    List<User> getUsers();
}
