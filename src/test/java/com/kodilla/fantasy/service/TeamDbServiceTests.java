package com.kodilla.fantasy.service;

import com.kodilla.fantasy.domain.Team;
import com.kodilla.fantasy.repository.TeamRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class TeamDbServiceTests {

    @InjectMocks
    private TeamDbService teamDbService;
    @Mock
    private TeamRepository teamRepository;

    @Test
    void shouldInitTeams() {
        //Given
        Team team1 = new Team(1L, 2L, "Test", "TET", new ArrayList<>());
        Team team2 = new Team(2L, 3L, "Test2", "TE2", new ArrayList<>());

        //When
        teamDbService.initTeams(List.of(team1, team2));

        //Then
        verify(teamRepository, times(1)).deleteAll();
        verify(teamRepository, times(1)).saveAll(anyList());
    }
}
