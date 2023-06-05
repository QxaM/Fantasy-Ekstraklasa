package com.kodilla.fantasy.service;

import com.kodilla.fantasy.domain.Team;
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
}
