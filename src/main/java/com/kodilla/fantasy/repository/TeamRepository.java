package com.kodilla.fantasy.repository;

import com.kodilla.fantasy.domain.Team;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TeamRepository extends CrudRepository<Team, Long> {
    @Override
    List<Team> findAll();
    Optional<Team> findTeamByApiFootballId(Long apiFootballId);
}
