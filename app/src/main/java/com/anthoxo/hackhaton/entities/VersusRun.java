package com.anthoxo.hackhaton.entities;

import com.anthoxo.hackhaton.entities.converters.MoveConverter;
import jakarta.persistence.*;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import java.util.List;

@Table
@Entity(name = "versus_runs")
public final class VersusRun implements Run {

    @Id
    @GeneratedValue(generator = "versus-run-generator")
    @GenericGenerator(
            name = "versus-run-generator",
            strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
            parameters = {
                    @Parameter(name = "sequence_name", value = "versus_runs_sequence"),
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

    @Convert(converter = MoveConverter.class)
    @Column(name = "moves")
    private List<String> moves;

    public VersusRun() {
    }

    public VersusRun(
            GridEntity grid,
            User topLeftUser,
            User bottomRightUser,
            List<String> moves) {
        this.grid = grid;
        this.topLeftUser = topLeftUser;
        this.bottomRightUser = bottomRightUser;
        this.moves = moves;
    }

    public Long getId() {
        return id;
    }

    @Override
    public List<User> getUsers() {
        return List.of(topLeftUser, bottomRightUser);
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

    public List<String> getMoves() {
        return moves;
    }

    public void setMoves(List<String> moves) {
        this.moves = moves;
    }
}

