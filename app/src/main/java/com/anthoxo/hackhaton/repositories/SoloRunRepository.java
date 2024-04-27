package com.anthoxo.hackhaton.repositories;

import com.anthoxo.hackhaton.entities.SoloRun;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SoloRunRepository extends JpaRepository<SoloRun, Long> {

}
