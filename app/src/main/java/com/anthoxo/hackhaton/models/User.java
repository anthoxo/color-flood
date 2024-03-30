package com.anthoxo.hackhaton.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

@Entity
@Table(name = "users")
public class User {

  @Id
  @GeneratedValue(generator = "user-generator")
  @GenericGenerator(
      name = "user-generator",
      strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
      parameters = {
          @Parameter(name = "sequence_name", value = "users_sequence"),
          @Parameter(name = "initial_value", value = "1"),
          @Parameter(name = "increment_size", value = "1")
      }
  )
  private Long id;

  @Column(name = "team_name")
  private String teamName;

  @OneToOne(mappedBy = "user")
  @PrimaryKeyJoinColumn
  private Ladder ladder;

  @OneToOne(mappedBy = "user", fetch = FetchType.LAZY)
  @PrimaryKeyJoinColumn
  private Code code;

  public User() {
  }

  public User(String teamName) {
    this.teamName = teamName;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getTeamName() {
    return teamName;
  }

  public void setTeamName(String teamName) {
    this.teamName = teamName;
  }

  public Ladder getLadder() {
    return ladder;
  }

  public void setLadder(Ladder ladder) {
    this.ladder = ladder;
  }

  public Code getCode() {
    return code;
  }

  public void setCode(Code code) {
    this.code = code;
  }
}
