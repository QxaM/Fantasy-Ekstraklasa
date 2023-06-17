package com.kodilla.fantasy.service;

import com.kodilla.fantasy.domain.Player;
import com.kodilla.fantasy.domain.SortType;
import com.kodilla.fantasy.domain.exception.ElementNotFoundException;
import com.kodilla.fantasy.factory.PlayerSorterFactory;
import com.kodilla.fantasy.repository.PlayerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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

    public Page<Player> getPlayers(Integer page, SortType sortType) {
        Sort sort = PlayerSorterFactory.makeSorter(sortType);
        Pageable pageWith20Elements = PageRequest.of(page, 20, sort);
        return repository.findAll(pageWith20Elements);
    }

    public Player getPlayer(Long id) throws ElementNotFoundException {
        return repository.findById(id)
                .orElseThrow(() -> new ElementNotFoundException("Player with given id: " + id + " does not exist"));
    }

    public List<Player> getPlayersByTeamId(Long id) {
        return repository.findPlayersByTeamId(id);
    }

    @Transactional
    public Player updatePlayer(Player player) throws ElementNotFoundException {
        Player foundPlayer = repository.findById(player.getId())
                .orElseThrow(() -> new ElementNotFoundException("Player with given id: " + player.getId() + " does not exist"));

        Player changedPlayer = Player.builder()
                .apiFootballId(foundPlayer.getApiFootballId())
                .name(player.getName())
                .firstname(player.getFirstname())
                .lastname(player.getLastname())
                .age(player.getAge())
                .value(player.getValue())
                .position(player.getPosition())
                .team(player.getTeam())
                .squads(player.getSquads())
                .points(player.getPoints())
                .build();
        changedPlayer.setId(foundPlayer.getId());

        return repository.save(changedPlayer);
    }

    public void saveAllPlayers(List<Player> players) {
        repository.saveAll(players);
    }

    public void deletePlayer(Long id) {
        repository.deleteById(id);
    }
}
