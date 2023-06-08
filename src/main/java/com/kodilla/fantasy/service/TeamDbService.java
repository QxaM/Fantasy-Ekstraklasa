package com.kodilla.fantasy.service;

import com.kodilla.fantasy.domain.Team;
import com.kodilla.fantasy.domain.exception.ElementNotFoundException;
import com.kodilla.fantasy.repository.TeamRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TeamDbService {

    private final TeamRepository repository;

    @Transactional
    public void initTeams(List<Team> teams) {
        repository.deleteAll();
        repository.saveAll(teams);
    }

    public List<Team> getTeams() {
        return repository.findAll();
    }

    public Team getTeam(Long id) throws ElementNotFoundException {
        return repository.findById(id)
                .orElseThrow(() -> new ElementNotFoundException("Team with given id: " + id + " does not exist"));
    }

    public Team getTeamByApiFootballId(Long id) {
        return repository.findTeamByApiFootballId(id)
                .orElse(new Team());
    }

    @Transactional
    public Team updateTeam(Team team) throws ElementNotFoundException {
        Team foundTeam = repository.findById(team.getId())
                .orElseThrow(() -> new ElementNotFoundException("Team with given id: " + team.getId() + " does not exist"));
        Team updatedTeam = new Team(
                team.getId(),
                foundTeam.getApiFootballId(),
                team.getName(),
                team.getCode(),
                foundTeam.getPlayers()
        );
        return repository.save(updatedTeam);
    }

    public void deleteTeam(Long id) {
        repository.deleteById(id);
    }
}
