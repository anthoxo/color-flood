package com.anthoxo.hackhaton.repositories;

import com.anthoxo.hackhaton.entities.GridEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GridRepository extends JpaRepository<GridEntity, Long> {

}
