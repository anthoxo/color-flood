package com.anthoxo.hackhaton.repositories;

import com.anthoxo.hackhaton.models.Ladder;
import com.anthoxo.hackhaton.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LadderRepository extends JpaRepository<Ladder, Long> { }
