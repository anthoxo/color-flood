package com.anthoxo.hackhaton.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

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
  private float elo;

  @Column(name = "fight_count")
  private int fightCount;

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

  public float getElo() {
    return elo;
  }

  public void setElo(float elo) {
    this.elo = elo;
  }

  public int getFightCount() {
    return fightCount;
  }

  public void setFightCount(int fightCount) {
    this.fightCount = fightCount;
  }
}
