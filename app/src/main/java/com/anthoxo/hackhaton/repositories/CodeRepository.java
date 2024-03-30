package com.anthoxo.hackhaton.repositories;

import com.anthoxo.hackhaton.models.Code;
import com.anthoxo.hackhaton.models.Ladder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CodeRepository extends JpaRepository<Code, Long> { }
