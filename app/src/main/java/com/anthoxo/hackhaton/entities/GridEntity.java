package com.anthoxo.hackhaton.entities;

import com.anthoxo.hackhaton.entities.converters.GridConverter;
import jakarta.persistence.*;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "grids")
public class GridEntity {
    @Id
    @GeneratedValue(generator = "grid-generator")
    @GenericGenerator(
            name = "grid-generator",
            strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
            parameters = {
                    @Parameter(name = "sequence_name", value = "grids_sequence"),
                    @Parameter(name = "initial_value", value = "1"),
                    @Parameter(name = "increment_size", value = "1")
            }
    )
    private Long id;

    @Convert(converter = GridConverter.class)
    @Column(name = "grid")
    private List<List<Integer>> grid;

    public GridEntity() {
    }

    public GridEntity(List<List<Integer>> grid) {
        this.grid = grid;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<List<Integer>> getGrid() {
        return grid;
    }

    public void setGrid(List<List<Integer>> grid) {
        this.grid = grid;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        GridEntity that = (GridEntity) o;
        return Objects.equals(id, that.id) && Objects.equals(
                grid, that.grid);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, grid);
    }

    @Override
    public String toString() {
        return "GridEntity{" +
                "id=" + id +
                ", grid=" + grid +
                '}';
    }
}
