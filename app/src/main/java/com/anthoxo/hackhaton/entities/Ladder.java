package com.anthoxo.hackhaton.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

import java.util.Objects;

@Entity
@Table(name = "ladders")
public class Ladder {

    @Id
    @Column(name = "user_id")
    private Long userId;

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    @MapsId
    private User user;

    @Column(name = "solo_elo")
    private double soloElo;

    @Column(name = "versus_elo")
    private double versusElo;

    @Column(name = "battle_elo")
    private double battleElo;

    public Ladder() {
    }

    public Ladder(User user) {
        this.user = user;
        this.soloElo = 1_000;
        this.versusElo = 1_000;
        this.battleElo = 1_000;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public double getSoloElo() {
        return soloElo;
    }

    public void setSoloElo(double elo) {
        this.soloElo = elo;
    }

    public double getVersusElo() {
        return versusElo;
    }

    public void setVersusElo(double versusElo) {
        this.versusElo = versusElo;
    }

    public double getBattleElo() {
        return battleElo;
    }

    public void setBattleElo(double battleElo) {
        this.battleElo = battleElo;
    }

    public void addSoloChange(double eloChange) {
        this.soloElo += eloChange;
    }

    public void addVersusChange(double eloChange) {
        this.versusElo += eloChange;
    }

    public void addBattleChange(double eloChange) {
        this.battleElo += eloChange;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Ladder ladder = (Ladder) o;
        return Double.compare(soloElo, ladder.soloElo) == 0
            && Double.compare(versusElo, ladder.versusElo) == 0
            && Double.compare(battleElo, ladder.battleElo) == 0
            && Objects.equals(userId, ladder.userId)
            && Objects.equals(user, ladder.user);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, user, soloElo, versusElo, battleElo);
    }

    @Override
    public String toString() {
        return "Ladder{" +
            "userId=" + userId +
            ", elo=" + soloElo +
            '}';
    }
}
