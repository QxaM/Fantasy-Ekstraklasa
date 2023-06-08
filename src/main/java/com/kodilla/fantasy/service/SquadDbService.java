package com.kodilla.fantasy.service;

import com.kodilla.fantasy.domain.Squad;
import com.kodilla.fantasy.domain.User;
import com.kodilla.fantasy.domain.exception.ElementNotFoundException;
import com.kodilla.fantasy.repository.SquadRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SquadDbService {

    private final SquadRepository repository;

    public List<Squad> getSquads() {
        return repository.findAll();
    }

    public Squad getSquad(Long id) throws ElementNotFoundException {
        return repository.findById(id)
                .orElseThrow(() -> new ElementNotFoundException("Squad with given id: " + id + "does not exist"));
    }

    public Squad saveSquad(Squad squad) {
        return repository.save(squad);
    }

    public void deleteSquad(Long id) {
        repository.deleteById(id);
    }
}
