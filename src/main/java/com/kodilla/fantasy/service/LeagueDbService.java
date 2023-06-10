package com.kodilla.fantasy.service;

import com.kodilla.fantasy.domain.League;
import com.kodilla.fantasy.domain.User;
import com.kodilla.fantasy.domain.exception.ElementNotFoundException;
import com.kodilla.fantasy.repository.LeagueRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LeagueDbService {

    private final LeagueRepository repository;
    private final UserDbService userDbService;

    public List<League> getLeagues() {
        return repository.findAll();
    }

    @Transactional
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

    @Transactional
    public void addUser(Long leagueId, Long userId) throws ElementNotFoundException {
        League foundLeague = getLeague(leagueId);
        User foundUser = userDbService.getUser(userId);

        foundLeague.getUsers().add(foundUser);
        repository.save(foundLeague);
    }

    @Transactional
    public void removeUser(Long leagueId, Long userId) throws ElementNotFoundException {
        League foundLeague = getLeague(leagueId);
        User foundUser = userDbService.getUser(userId);

        foundLeague.getUsers().remove(foundUser);
        repository.save(foundLeague);
    }
}
