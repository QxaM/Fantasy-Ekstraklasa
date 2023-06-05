package com.kodilla.fantasy.service;

import com.kodilla.fantasy.apifootball.facade.ApiFootballFacade;
import com.kodilla.fantasy.domain.Player;
import com.kodilla.fantasy.domain.Position;
import com.kodilla.fantasy.domain.Team;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DataInitializerTests {

    @InjectMocks
    private DataInitializer dataInitializer;
    @Mock
    private ApiFootballFacade apiFootballFacade;
    @Mock
    private PlayerDbService playerDbService;
    @Mock
    private TeamDbService teamDbService;

    @Test
    void testShouldInitTeams() {
        //Given
        Team team1 = new Team(1L, 2L, "Test", "TET", new ArrayList<>());
        Team team2 = new Team(2L, 3L, "Test2", "TE2", new ArrayList<>());
        List<Team> teams = List.of(team1, team2);
        when(apiFootballFacade.getAllTeams()).thenReturn(teams);

        //When
        dataInitializer.fetchTeams();

        //Then
        verify(teamDbService, times(1)).initTeams(teams);
    }

    @Test
    void testShouldInitPlayers() {
        //Given
        Team team1 = new Team(1L, 2L, "Test", "TET", new ArrayList<>());
        Player player1 = new Player(1L, 2L, "Test", "Test", 21, BigDecimal.ONE, Position.ST, team1);
        Player player2 = new Player(2L, 3L, "Test2", "Test2", 22, BigDecimal.TEN, Position.GK, team1);
        team1.getPlayers().add(player1);
        team1.getPlayers().add(player2);
        List<Player> players = List.of(player1, player2);
        when(apiFootballFacade.getAllPlayers()).thenReturn(players);

        //When
        dataInitializer.fetchPlayers();

        //Then
        verify(playerDbService, times(1)).initPlayers(players);
    }
}
