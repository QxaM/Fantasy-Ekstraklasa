package com.kodilla.fantasy.service;

import com.kodilla.fantasy.domain.Player;
import com.kodilla.fantasy.domain.Squad;
import com.kodilla.fantasy.domain.exception.ElementNotFoundException;
import com.kodilla.fantasy.domain.exception.NotEnoughFundsException;
import com.kodilla.fantasy.domain.exception.PlayerAlreadyExistInSquadException;
import com.kodilla.fantasy.domain.exception.SquadAlreadyFullException;
import com.kodilla.fantasy.repository.SquadRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SquadDbService {

    private final SquadRepository repository;
    private final PlayerDbService playerDbService;

    public List<Squad> getSquads() {
        return repository.findAll();
    }

    public Squad getSquad(Long id) throws ElementNotFoundException {
        return repository.findById(id)
                .orElseThrow(() -> new ElementNotFoundException("Squad with given id: " + id + " does not exist"));
    }

    public Squad saveSquad(Squad squad) {
        return repository.save(squad);
    }

    public void deleteSquad(Long id) {
        repository.deleteById(id);
    }

    public Squad addPlayer(Long squadId, Long playerId) throws PlayerAlreadyExistInSquadException,
                                                                ElementNotFoundException,
                                                                SquadAlreadyFullException,
                                                                NotEnoughFundsException {
        Squad foundSquad = getSquad(squadId);
        Player playerToAdd = playerDbService.getPlayer(playerId);

        foundSquad.addPlayer(playerToAdd);

        return repository.save(foundSquad);
    }

    public Squad removePlayer(Long squadId, Long playerId) throws ElementNotFoundException {
        Squad foundSquad = getSquad(squadId);
        Player playerToRemove = playerDbService.getPlayer(playerId);

        foundSquad.removePlayer(playerToRemove);

        return repository.save(foundSquad);
    }
}
