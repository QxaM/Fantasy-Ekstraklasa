package com.kodilla.fantasy.service;

import com.kodilla.fantasy.domain.Player;
import com.kodilla.fantasy.domain.exception.ElementNotFoundException;
import com.kodilla.fantasy.repository.PlayerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PlayerDbService {

    private final PlayerRepository repository;

    @Transactional
    public void initPlayers(List<Player> players) {
        repository.deleteAll();
        repository.saveAll(players);
    }

    public Page<Player> getPlayers(Integer page) {
        Pageable pageWith20Elements = PageRequest.of(page, 20);
        return repository.findAll(pageWith20Elements);
    }

    public Player getPlayer(Long id) throws ElementNotFoundException {
        return repository.findById(id)
                .orElseThrow(() -> new ElementNotFoundException("Player with given id: " + id + " does not exist"));
    }

    public Player getPlayerByFirstnameAndLastnameAndTeamId(String firstname, String lastname, Long id) throws ElementNotFoundException {
        return repository.findPlayerByFirstnameAndLastnameAndTeamId(firstname, lastname, id)
                .orElseThrow(() -> new ElementNotFoundException("Player for given firstname: " + firstname
                                                                + " and lastname: " + lastname + " does not exist"));
    }

    @Transactional
    public Player updatePlayer(Player player) throws ElementNotFoundException {
        Player foundPlayer = repository.findById(player.getId())
                .orElseThrow(() -> new ElementNotFoundException("Player with given id: " + player.getId() + " does not exist"));
        Player changedPlayer = new Player(
                player.getId(),
                foundPlayer.getApiFootballId(),
                player.getFirstname(),
                player.getLastname(),
                player.getAge(),
                player.getValue(),
                player.getPosition(),
                player.getTeam(),
                player.getSquads(),
                player.getPoints()
        );
        return repository.save(changedPlayer);
    }

    public void saveAllPlayers(List<Player> players) {
        repository.saveAll(players);
    }

    public void deletePlayer(Long id) {
        repository.deleteById(id);
    }
}
