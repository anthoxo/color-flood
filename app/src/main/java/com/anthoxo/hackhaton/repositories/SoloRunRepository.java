package com.anthoxo.hackhaton.repositories;

import com.anthoxo.hackhaton.entities.GridEntity;
import com.anthoxo.hackhaton.entities.SoloRun;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SoloRunRepository extends JpaRepository<SoloRun, Long> {
    List<SoloRun> findAllByGrid(GridEntity gridEntity);
}
