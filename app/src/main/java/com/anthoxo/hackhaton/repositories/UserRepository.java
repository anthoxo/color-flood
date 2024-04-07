package com.anthoxo.hackhaton.repositories;

import com.anthoxo.hackhaton.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

  @Query
  Optional<User> findByTeamNameAndPassword(@Param("teamName") String teamName, @Param("password") String password);

  @Query
  boolean existsByTeamName(@Param("teamName") String teamName);

  @Query
  boolean existsByTeamNameAndPassword(@Param("teamName") String teamName, @Param("password") String password);
}
