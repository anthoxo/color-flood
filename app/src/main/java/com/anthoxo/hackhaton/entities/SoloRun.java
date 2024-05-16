package com.anthoxo.hackhaton.entities;

import com.anthoxo.hackhaton.entities.converters.JokerConverter;
import com.anthoxo.hackhaton.entities.converters.MoveConverter;
import com.anthoxo.hackhaton.models.Joker;
import jakarta.persistence.*;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import java.util.List;

@Table
@Entity(name = "solo_runs")
public final class SoloRun implements Run {
    @Id
    @GeneratedValue(generator = "solo-run-generator")
    @GenericGenerator(
            name = "solo-run-generator",
            strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
            parameters = {
                    @Parameter(name = "sequence_name", value = "solo_runs_sequence"),
                    @Parameter(name = "initial_value", value = "1"),
                    @Parameter(name = "increment_size", value = "1")
            }
    )
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "grid_id", referencedColumnName = "id")
    private GridEntity grid;

    @Convert(converter = MoveConverter.class)
    @Column(name = "moves")
    private List<String> moves;

    @Convert(converter = JokerConverter.class)
    @Column(name = "jokers")
    private List<Joker> jokers;

    public SoloRun() {
    }

    public SoloRun(User user, GridEntity grid, List<String> moves, List<Joker> jokers) {
        this.user = user;
        this.grid = grid;
        this.moves = moves;
        this.jokers = jokers;
    }

    public Long getId() {
        return id;
    }

    @Override
    public List<User> getUsers() {
        return List.of(user);
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public GridEntity getGrid() {
        return grid;
    }

    public void setGrid(GridEntity grid) {
        this.grid = grid;
    }

    public List<String> getMoves() {
        return moves;
    }

    public void setMoves(List<String> moves) {
        this.moves = moves;
    }

    public List<Joker> getJokers() {
        return jokers;
    }

    public void setJokers(List<Joker> jokers) {
        this.jokers = jokers;
    }
}
