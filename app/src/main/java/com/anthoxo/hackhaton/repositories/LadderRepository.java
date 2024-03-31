package com.anthoxo.hackhaton.repositories;

import com.anthoxo.hackhaton.entities.Ladder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LadderRepository extends JpaRepository<Ladder, Long> { }
