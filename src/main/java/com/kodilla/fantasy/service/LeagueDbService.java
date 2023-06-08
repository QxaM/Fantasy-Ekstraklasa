package com.kodilla.fantasy.service;

import com.kodilla.fantasy.domain.League;
import com.kodilla.fantasy.domain.exception.ElementNotFoundException;
import com.kodilla.fantasy.repository.LeagueRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LeagueDbService {

    private final LeagueRepository repository;

    public List<League> getLeagues() {
        return repository.findAll();
    }

    public League getLeague(Long id) throws ElementNotFoundException {
        return repository.findById(id)
                .orElseThrow(() -> new ElementNotFoundException("League with given id: " + id + "does not exist"));
    }

    public League saveLeague(League league) {
        return repository.save(league);
    }

    public void deleteLeague(Long id) {
        repository.deleteById(id);
    }
}
