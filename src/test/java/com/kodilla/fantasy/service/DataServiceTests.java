package com.kodilla.fantasy.service;

import com.kodilla.fantasy.apifootball.facade.ApiFootballFacade;
import com.kodilla.fantasy.domain.Player;
import com.kodilla.fantasy.domain.Position;
import com.kodilla.fantasy.domain.Team;
import com.kodilla.fantasy.livescore.domain.EventType;
import com.kodilla.fantasy.livescore.domain.Match;
import com.kodilla.fantasy.livescore.facade.LiveScoreFacade;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DataServiceTests {

    @InjectMocks
    private DataService dataInitializer;
    @Mock
    private ApiFootballFacade apiFootballFacade;
    @Mock
    private PlayerDbService playerDbService;
    @Mock
    private TeamDbService teamDbService;
    @Mock
    private LiveScoreFacade liveScoreFacade;

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

    @Test
    void testShouldAddScore() {
        //Given
        Team team1 = new Team(1L, 2L, "Test", "TET", new ArrayList<>());
        Player emptyPlayer1 = new Player(1L, 2L, "Test", "Test", 21, BigDecimal.ONE, Position.ST, team1, new ArrayList<>(), 0);
        Player emptyPlayer2 = new Player(2L, 3L, "Test2", "Test2", 22, BigDecimal.TEN, Position.GK, team1, new ArrayList<>(), 0);

        Match match1 = new Match("1", team1, team1, new HashMap<>());
        match1.addEvent(emptyPlayer1, EventType.LINEUP);
        match1.addEvent(emptyPlayer1, EventType.RED_CARD);
        Match match2 = new Match("2", team1, team1, new HashMap<>());
        match2.addEvent(emptyPlayer2, EventType.LINEUP);
        match2.addEvent(emptyPlayer2, EventType.GOAL);

        when(liveScoreFacade.fetchMatches(1)).thenReturn(List.of(match1, match2));

        //When
        dataInitializer.addScores(1);

        //Then
        verify(playerDbService, times(1)).saveAllPlayers(anyList());
    }

    @Test
    void shouldFetchScores() {
        //Given
        Team team1 = new Team(1L, 2L, "Test", "TET", new ArrayList<>());
        Player emptyPlayer1 = new Player(1L, 2L, "Test", "Test", 21, BigDecimal.ONE, Position.ST, team1, new ArrayList<>(), 0);
        Player emptyPlayer2 = new Player(2L, 3L, "Test2", "Test2", 22, BigDecimal.TEN, Position.GK, team1, new ArrayList<>(), 0);

        Match match1 = new Match("1", team1, team1, new HashMap<>());
        match1.addEvent(emptyPlayer1, EventType.LINEUP);
        match1.addEvent(emptyPlayer1, EventType.RED_CARD);
        Match match2 = new Match("2", team1, team1, new HashMap<>());
        match2.addEvent(emptyPlayer2, EventType.LINEUP);
        match2.addEvent(emptyPlayer2, EventType.GOAL);

        //When
        List<Player> fetchedPlayers = dataInitializer.fetchPlayerScores(List.of(match1, match2));

        //Then
        assertAll(() -> assertEquals(2, fetchedPlayers.size()),
                () -> assertEquals(-2, fetchedPlayers.get(0).getPoints()),
                () -> assertEquals(4, fetchedPlayers.get(1).getPoints()));

    }
}
