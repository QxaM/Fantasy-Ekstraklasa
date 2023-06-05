package com.kodilla.fantasy.service;

import com.kodilla.fantasy.domain.Player;
import com.kodilla.fantasy.repository.PlayerRepository;
import lombok.RequiredArgsConstructor;
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
}
