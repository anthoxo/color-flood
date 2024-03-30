package com.anthoxo.hackhaton.repositories;

import com.anthoxo.hackhaton.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

  @Query
  boolean existsByTeamName(@Param("teamName") String teamName);
}
