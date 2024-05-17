package com.anthoxo.hackhaton.models;

import java.util.*;

public final class Game {

    private final List<Player> players;
    private final List<EnrichedGrid> history = new ArrayList<>();
    private final Grid grid;
    private final Random random = new Random();

    public Game(List<Player> players, Grid grid) {
        this.players = players;
        this.grid = grid;
        this.history.add(new EnrichedGrid(new Grid(grid), Joker.NONE));
        initPlayers();
    }

    public List<EnrichedGrid> getHistory() {
        return history;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public Optional<Player> getPlayer(StartingTile tile) {
        return players.stream().filter(player -> tile.equals(player.startingTile())).findFirst();
    }

    public void saveHistory() {
        saveHistory(Joker.NONE);
    }

    public void saveHistory(Joker joker) {
        history.add(new EnrichedGrid(new Grid(grid), joker));
    }

    public void initPlayers() {
        players.forEach(player -> player.initJokers(grid.colors().size()));
    }

    public void run(int turn, String answer) {
        Player currentPlayer = getCurrentPlayer(turn);
        if (currentPlayer.isGameOver()) {
            saveHistory();
            return;
        }
        if (currentPlayer.isZapped()) {
            currentPlayer.setCursedJoker(Joker.NONE);
            saveHistory();
            return;
        }
        String[] lines = answer.split(" ");
        Integer newColor = Integer.valueOf(lines[0]);
        long previousTiles = grid.countZone(currentPlayer);

        Joker usedJoker = useJokers(currentPlayer, lines);
        run(turn, newColor, usedJoker);
        winJokers(currentPlayer, previousTiles);
    }

    public void run(int turn, int newColor) {
        run(turn, newColor, Joker.NONE);
    }

    public void run(int turn, int newColor, Joker usedJoker) {
        Player currentPlayer = getCurrentPlayer(turn);
        if (currentPlayer.isGameOver()) {
            return;
        }
        if (players.stream().map(player -> player.currentColor(grid))
                .noneMatch(color -> color == newColor)) {
            grid.color(currentPlayer.startingTile(),
                    currentPlayer.currentColor(grid), newColor);
        }
        saveHistory(usedJoker);
    }

    public Player getCurrentPlayer(int turn) {
        return players.get(turn % players.size());
    }

    public void gameOver(int turn) {
        Player currentPlayer = getCurrentPlayer(turn);
        currentPlayer.setGameOver(true);
        history.add(new EnrichedGrid(new Grid(grid), Joker.NONE));
    }

    public boolean isFinished() {
        return grid.getNumberOfColors() <= players.size();
    }

    public Grid getGrid() {
        return getGrid(Joker.NONE);
    }

    public Grid getGrid(Player player) {
        return getGrid(player.getCursedJoker());
    }

    public Grid getGrid(Joker joker) {
        if (joker == Joker.SHADOW) {
            return history.getFirst().grid();
        }
        return grid;
    }

    public int getSize() {
        return grid.colors().size();
    }

    private Joker useJokers(Player currentPlayer, String[] lines) {
        currentPlayer.setCursedJoker(Joker.NONE);
        if (lines.length == 1) {
            return Joker.NONE;
        }
        Joker joker = Joker.getJoker(lines[1]);
        Joker res = Joker.NONE;

        if (currentPlayer.getJokerCounter(joker) > 0) {
            res = joker;
        }

        switch (joker) {
            case Joker jok when currentPlayer.getJokerCounter(jok) == 0 -> {}
            case NONE -> {}
            case ZAP, SHADOW -> {
                currentPlayer.useJoker(joker);
                players
                    .stream()
                    .filter(enemyPlayer -> !enemyPlayer.equals(currentPlayer) && enemyPlayer.getCursedJoker() == Joker.NONE)
                    .forEach(enemyPlayer -> enemyPlayer.setCursedJoker(joker));
            }
            case ARCANE_THIEF -> {
                List<Player> enemies = players.stream()
                    .filter(enemy -> !enemy.equals(currentPlayer))
                    .toList();
                Player chosenPlayer = enemies.get(random.nextInt(enemies.size()));
                currentPlayer.useJoker(Joker.ARCANE_THIEF);
                if (chosenPlayer.getJokerCounter(Joker.ZAP) > 0 && !chosenPlayer.isProtected()) {
                    currentPlayer.winJoker(Joker.ZAP);
                    chosenPlayer.useJoker(Joker.ZAP);
                } else {
                    currentPlayer.setCursedJoker(Joker.ZAP);
                    chosenPlayer.winJoker(Joker.ZAP);
                }
            }
            case SHIELD -> {
                currentPlayer.useJoker(Joker.SHIELD);
                currentPlayer.setCursedJoker(Joker.SHIELD);
            }
        }

        return res;
    }

    public void winJokers(Player player, long previousTiles) {
        int step = getSize();
        long previous = previousTiles / step;
        long current = grid.countZone(player) / step;
        for (long i = previous; i < current; ++i) {
            if (i % 2 == 0) {
                player.winJoker(Joker.ZAP);
                player.winJoker(Joker.SHIELD);
            } else {
                player.winJoker(Joker.SHADOW);
                player.winJoker(Joker.SHIELD);
            }
        }
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
