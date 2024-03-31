package com.anthoxo.hackhaton.models;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public final class Game {

    private final List<Player> players;
    private final List<Grid> history = new ArrayList<>();
    private final Grid grid;

    public Game(List<Player> players, Grid grid) {
        this.players = players;
        this.grid = grid;
        this.history.add(new Grid(grid));
    }

    public List<Grid> getHistory() {
        return history;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public void run(int turn, int newColor) {
        Player currentPlayer = players.get(turn % players.size());
        if (players.stream().map(player -> player.currentColor(grid))
                .noneMatch(color -> color == newColor)) {
            grid.color(currentPlayer.startingTile(),
                    currentPlayer.currentColor(grid), newColor);
        }
        history.add(new Grid(grid));
    }

    public boolean isFinished() {
        return grid.containsOnlyOneColors();
    }

    public Grid getGrid() {
        return grid;
    }

    public int getSize() {
        return grid.colors().size();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj == null || obj.getClass() != this.getClass()) {
            return false;
        }
        var that = (Game) obj;
        return Objects.equals(this.players, that.players);
    }

    @Override
    public int hashCode() {
        return Objects.hash(players);
    }

    @Override
    public String toString() {
        return "Game[" +
                "players=" + players + ']';
    }


}
