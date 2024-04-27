package com.anthoxo.hackhaton.repositories;

import com.anthoxo.hackhaton.entities.Ladder;
import com.anthoxo.hackhaton.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LadderRepository extends JpaRepository<Ladder, Long> {
    Optional<Ladder> findByUser(User user);
}
