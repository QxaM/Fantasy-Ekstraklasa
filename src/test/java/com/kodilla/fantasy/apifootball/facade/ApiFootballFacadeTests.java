package com.kodilla.fantasy.apifootball.facade;

import com.kodilla.fantasy.apifootball.client.ApiFootballClient;
import com.kodilla.fantasy.apifootball.dto.*;
import com.kodilla.fantasy.domain.Player;
import com.kodilla.fantasy.domain.Position;
import com.kodilla.fantasy.domain.Team;
import com.kodilla.fantasy.domain.exception.ElementNotFoundException;
import com.kodilla.fantasy.mapper.ApiFootballMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ApiFootballFacadeTests {

    @InjectMocks
    private ApiFootballFacade apiFootballFacade;

    @Mock
    private ApiFootballClient apiFootballClient;
    @Mock
    private ApiFootballMapper apiFootballMapper;

    private ApiFootballTeamDto apiFootballTeam1;
    private ApiFootballTeamDto apiFootballTeam2;

    @BeforeEach
    void buildTeamResponse() {
        apiFootballTeam1 = new ApiFootballTeamDto(357L, "Test team 1", "TT1");
        apiFootballTeam2 = new ApiFootballTeamDto(358L, "Test team 2", "TT2");
    }

    @Test
    void testShouldGetTeams() {
        //Given
        Team team1 = new Team(357L, "Test team 1", "TT1");
        Team team2 = new Team(358L, "Test team 2", "TT2");

        TeamResponseDto teamResponse1 = new TeamResponseDto(apiFootballTeam1);
        TeamResponseDto teamResponse2 = new TeamResponseDto(apiFootballTeam2);
        GetTeamsDto teamsResponse = new GetTeamsDto(List.of(teamResponse1, teamResponse2));

        when(apiFootballClient.fetchTeams()).thenReturn(teamsResponse);
        when(apiFootballMapper.mapToTeam(apiFootballTeam1)).thenReturn(team1);
        when(apiFootballMapper.mapToTeam(apiFootballTeam2)).thenReturn(team2);

        //When
        List<Team> fetchedTeams = apiFootballFacade.getAllTeams();

        //Then
        assertEquals(2, fetchedTeams.size());
    }

    @Test
    void testShouldFetchEmptyTeams() {
        //Given
        when(apiFootballClient.fetchTeams()).thenReturn(new GetTeamsDto(Collections.emptyList()));

        //When
        List<Team> fetchedTeams = apiFootballFacade.getAllTeams();

        //Then
        assertEquals(0, fetchedTeams.size());
    }

    @Test
    void testShouldFetchPlayers() {
        //Given
        Player player = new Player.PlayerBuilder()
                .firstname("Test name")
                .lastname("Test name")
                .age(21)
                .value(BigDecimal.ONE)
                .position(Position.GK)
                .build();

        ApiFootballPlayerDto playerDto = new ApiFootballPlayerDto(357L, "name", "Test name", "Test name", 21);
        GamesDto gamesDto = new GamesDto("Goalkeeper", "6.0");
        StatisticsDto statisticsDto = new StatisticsDto(apiFootballTeam1, gamesDto);
        PlayerResponseDto playerResponseDto = new PlayerResponseDto(
                playerDto,
                List.of(statisticsDto)
        );

        GetPlayersDto getPlayersDto1 = new GetPlayersDto(
                new PagingDto(1, 2),
                List.of(playerResponseDto)
        );
        GetPlayersDto getPlayersDto2 = new GetPlayersDto(
                new PagingDto(2, 2),
                List.of(playerResponseDto)
        );

        when(apiFootballClient.fetchPlayers(1)).thenReturn(getPlayersDto1);
        when(apiFootballClient.fetchPlayers(2)).thenReturn(getPlayersDto2);
        when(apiFootballMapper.mapToPlayer(playerResponseDto)).thenReturn(player);

        //When
        List<Player> players = apiFootballFacade.getAllPlayers();

        //Then
        assertEquals(2, players.size());
    }

    @Test
    void testShouldFetchEmptyPlayers() throws ElementNotFoundException {
        //Given
        GetPlayersDto getPlayersDto = new GetPlayersDto(
                new PagingDto(3, 3),
                Collections.emptyList()
        );
        when(apiFootballClient.fetchPlayers(anyInt())).thenReturn(getPlayersDto);

        //When
        List<Player> players = apiFootballFacade.getAllPlayers();

        //Then
        assertEquals(0, players.size());
    }
}
