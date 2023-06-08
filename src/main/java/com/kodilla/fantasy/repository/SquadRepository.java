package com.kodilla.fantasy.repository;

import com.kodilla.fantasy.domain.Squad;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SquadRepository extends CrudRepository<Squad, Long> {

    @Override
    List<Squad> findAll();
}
