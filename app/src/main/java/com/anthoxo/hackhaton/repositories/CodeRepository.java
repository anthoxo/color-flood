package com.anthoxo.hackhaton.repositories;

import com.anthoxo.hackhaton.entities.Code;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CodeRepository extends JpaRepository<Code, Long> { }
