package com.kodilla.fantasy.service;

import com.kodilla.fantasy.domain.Player;
import com.kodilla.fantasy.repository.PlayerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PlayerDbService {

    private final PlayerRepository repository;

    public List<Player> initPlayers(List<Player> players) {
        repository.deleteAll();
        return (List<Player>) repository.saveAll(players);
    }
}
