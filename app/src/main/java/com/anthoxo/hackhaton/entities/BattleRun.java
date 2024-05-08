package com.anthoxo.hackhaton.entities;

import com.anthoxo.hackhaton.entities.converters.MoveConverter;
import jakarta.persistence.*;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import java.util.List;

@Entity
@Table(name = "battle_runs")
public class BattleRun implements Run {

    @Id
    @GeneratedValue(generator = "battle-run-generator")
    @GenericGenerator(
        name = "battle-run-generator",
        strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
        parameters = {
            @Parameter(name = "sequence_name", value = "battle_runs_sequence"),
            @Parameter(name = "initial_value", value = "1"),
            @Parameter(name = "increment_size", value = "1")
        }
    )
    private Long id;

    @ManyToOne
    @JoinColumn(name = "grid_id", referencedColumnName = "id")
    private GridEntity grid;

    @ManyToOne
    @JoinColumn(name = "top_left_user_id", referencedColumnName = "id")
    private User topLeftUser;

    @ManyToOne
    @JoinColumn(name = "bottom_right_user_id", referencedColumnName = "id")
    private User bottomRightUser;

    @ManyToOne
    @JoinColumn(name = "top_right_user_id", referencedColumnName = "id")
    private User topRightUser;

    @ManyToOne
    @JoinColumn(name = "bottom_left_user_id", referencedColumnName = "id")
    private User bottomLeftUser;

    @Convert(converter = MoveConverter.class)
    @Column(name = "moves")
    private List<String> moves;

    public BattleRun() {
    }

    public BattleRun(
        GridEntity grid,
        User topLeftUser,
        User bottomRightUser,
        User topRightUser,
        User bottomLeftUser,
        List<String> moves
    ) {
        this.grid = grid;
        this.bottomRightUser = bottomRightUser;
        this.topLeftUser = topLeftUser;
        this.topRightUser = topRightUser;
        this.bottomLeftUser = bottomLeftUser;
        this.moves = moves;
    }

    public Long getId() {
        return id;
    }

    @Override
    public List<User> getUsers() {
        return List.of(topLeftUser, topRightUser, bottomLeftUser, bottomRightUser);
    }

    public void setId(Long id) {
        this.id = id;
    }

    public GridEntity getGrid() {
        return grid;
    }

    public void setGrid(GridEntity grid) {
        this.grid = grid;
    }

    public User getTopLeftUser() {
        return topLeftUser;
    }

    public void setTopLeftUser(User topLeftUser) {
        this.topLeftUser = topLeftUser;
    }

    public User getBottomRightUser() {
        return bottomRightUser;
    }

    public void setBottomRightUser(User bottomRightUser) {
        this.bottomRightUser = bottomRightUser;
    }

    public User getTopRightUser() {
        return topRightUser;
    }

    public void setTopRightUser(User topRightUser) {
        this.topRightUser = topRightUser;
    }

    public User getBottomLeftUser() {
        return bottomLeftUser;
    }

    public void setBottomLeftUser(User bottomLeftUser) {
        this.bottomLeftUser = bottomLeftUser;
    }

    public List<String> getMoves() {
        return moves;
    }

    public void setMoves(List<String> moves) {
        this.moves = moves;
    }
}
