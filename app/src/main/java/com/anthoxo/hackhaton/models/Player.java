package com.anthoxo.hackhaton.models;

import java.util.*;
import java.util.stream.Collectors;

public final class Player {

    private final String name;
    private final StartingTile startingTile;
    private final String pathFile;
    private boolean isGameOver = false;
    private Map<Joker, Integer> jokers = new HashMap<>();
    private Joker cursedJoker = Joker.NONE;

    public Player(String name, StartingTile startingTile, String pathFile) {
        this.name = name;
        this.startingTile = startingTile;
        this.pathFile = pathFile;
    }

    public String name() {
        return name;
    }

    public StartingTile startingTile() {
        return startingTile;
    }

    public void initJokers(int size) {
        jokers.clear();
        jokers.put(Joker.ZAP, 0);
        jokers.put(Joker.SHADOW, 0);
        jokers.put(Joker.SHIELD, 0);
        jokers.put(Joker.ARCANE_THIEF, size / 5);
        cursedJoker = Joker.NONE;
    }

    public String getJokersForProgram() {
        return jokers.entrySet()
            .stream()
            .filter(entry -> entry.getValue() > 0)
            .map(Map.Entry::getKey)
            .map(Joker::toString)
            .collect(Collectors.joining(","));
    }

    public int getJokerCounter(Joker joker) {
        return jokers.getOrDefault(joker, 0);
    }

    public void useJoker(Joker joker) {
        int count = getJokerCounter(joker);
        jokers.put(joker, count - 1);
    }

    public void winJoker(Joker joker) {
        int count = getJokerCounter(joker);
        jokers.put(joker, count + 1);

    }

    public Joker getCursedJoker() {
        return cursedJoker;
    }

    public void setCursedJoker(Joker cursedJoker) {
        this.cursedJoker = cursedJoker;
    }

    public boolean isProtected() {
        return cursedJoker == Joker.SHIELD;
    }

    public int currentColor(Grid grid) {
        return grid.getCurrentColor(startingTile);
    }

    public String pathFile() {
        return pathFile;
    }

    public boolean isGameOver() {
        return isGameOver;
    }

    public boolean isZapped() {
        return cursedJoker == Joker.ZAP;
    }

    public void setGameOver(boolean gameOver) {
        isGameOver = gameOver;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj == null || obj.getClass() != this.getClass()) {
            return false;
        }
        var that = (Player) obj;
        return Objects.equals(this.name, that.name) &&
                Objects.equals(this.startingTile, that.startingTile);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, startingTile);
    }

    @Override
    public String toString() {
        return "Player[" +
                "name=" + name + ", " +
                "startingTile=" + startingTile + ']';
    }

}
