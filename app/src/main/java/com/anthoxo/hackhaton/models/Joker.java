package com.anthoxo.hackhaton.models;

import java.util.stream.Stream;

/**
 * ZAP: you prevent someone from playing one turn
 * SHADOW: you blind someone one turn
 * SHIELD: you can protect you from enemy curses,
 * ARCANE_THIEF: you can steal joker randomly from one person, however if it is
 * impossible (no joker or activated shield), then you can't play the next turn
 *
 */
public enum Joker {
    ZAP, SHADOW, SHIELD, ARCANE_THIEF, NONE;

    public static Joker getJoker(String value) {
        return Stream.of(Joker.values())
            .filter(joker -> joker.name().equals(value))
            .findAny()
            .orElse(NONE);
    }
}
