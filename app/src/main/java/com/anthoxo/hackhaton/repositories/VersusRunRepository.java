package com.anthoxo.hackhaton.repositories;

import com.anthoxo.hackhaton.entities.VersusRun;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VersusRunRepository extends JpaRepository<VersusRun, Long> {
}
