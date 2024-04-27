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

  @Column(name = "elo")
  private double elo;

  public Ladder() {
  }

  public Ladder(User user) {
    this.user = user;
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

  public double getElo() {
    return elo;
  }

  public void setElo(double elo) {
    this.elo = elo;
  }

  public void addChange(double eloChange) {
    this.elo += eloChange;
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
    return Double.compare(elo, ladder.elo) == 0
            && Objects.equals(userId, ladder.userId)
            && Objects.equals(user, ladder.user);
  }

  @Override
  public int hashCode() {
    return Objects.hash(userId, user, elo);
  }

  @Override
  public String toString() {
    return "Ladder{" +
            "userId=" + userId +
            ", elo=" + elo +
            '}';
  }
}
