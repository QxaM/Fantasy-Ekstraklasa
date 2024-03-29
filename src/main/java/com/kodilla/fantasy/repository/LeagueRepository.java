package com.kodilla.fantasy.repository;

import com.kodilla.fantasy.domain.League;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LeagueRepository extends CrudRepository<League, Long> {

    @Override
    List<League> findAll();
    List<League> findLeaguesByUsersId(Long userId);
}
