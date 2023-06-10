package com.kodilla.fantasy.service;

import com.kodilla.fantasy.domain.Player;
import com.kodilla.fantasy.domain.Squad;
import com.kodilla.fantasy.domain.User;
import com.kodilla.fantasy.domain.exception.ElementNotFoundException;
import com.kodilla.fantasy.domain.exception.SquadAlreadyFullException;
import com.kodilla.fantasy.repository.SquadRepository;
import lombok.RequiredArgsConstructor;
import org.hibernate.hql.internal.ast.SqlASTFactory;
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
                .orElseThrow(() -> new ElementNotFoundException("Squad with given id: " + id + "does not exist"));
    }

    public Squad saveSquad(Squad squad) {
        return repository.save(squad);
    }

    public void deleteSquad(Long id) {
        repository.deleteById(id);
    }

    public Squad addPlayer(Long squadId, Long playerId) throws ElementNotFoundException, SquadAlreadyFullException {
        Squad foundSquad = getSquad(squadId);
        if(foundSquad.getPlayers().size() >= 11) {
            throw new SquadAlreadyFullException();
        }

        Player playerToAdd = playerDbService.getPlayer(playerId);
        foundSquad.getPlayers().add(playerToAdd);

        return repository.save(foundSquad);
    }

    public Squad removePlayer(Long squadId, Long playerId) throws ElementNotFoundException {
        Squad foundSquad = getSquad(squadId);

        Player playerToAdd = playerDbService.getPlayer(playerId);
        foundSquad.getPlayers().remove(playerToAdd);

        return repository.save(foundSquad);
    }
}
