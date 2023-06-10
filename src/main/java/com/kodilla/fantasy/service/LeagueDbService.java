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
                .orElseThrow(() -> new ElementNotFoundException("League with given id: " + id + " does not exist"));
    }

    public League saveLeague(League league) {
        return repository.save(league);
    }

    @Transactional
    public void deleteLeague(Long id) throws ElementNotFoundException {
        League foundLeague = getLeague(id);
        userDbService.detachUsers(foundLeague);
        repository.deleteById(id);
    }

    @Transactional
    public void addUser(Long leagueId, Long userId) throws ElementNotFoundException {
        League foundLeague = getLeague(leagueId);
        User foundUser = userDbService.getUser(userId);

        foundUser.getLeagues().add(foundLeague);
        foundLeague.getUsers().add(foundUser);

        userDbService.saveUser(foundUser);
    }

    @Transactional
    public void removeUser(Long leagueId, Long userId) throws ElementNotFoundException {
        League foundLeague = getLeague(leagueId);
        User foundUser = userDbService.getUser(userId);

        foundUser.getLeagues().remove(foundLeague);
        foundLeague.getUsers().remove(foundUser);

        userDbService.saveUser(foundUser);
    }
}
