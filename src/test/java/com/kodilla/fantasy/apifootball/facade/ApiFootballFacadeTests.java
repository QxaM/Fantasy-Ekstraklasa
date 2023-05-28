package com.kodilla.fantasy.apifootball.facade;

import com.kodilla.fantasy.apifootball.client.ApiFootballClient;
import com.kodilla.fantasy.apifootball.dto.ApiFootballTeamDto;
import com.kodilla.fantasy.apifootball.dto.GetTeamsDto;
import com.kodilla.fantasy.apifootball.dto.TeamResponseDto;
import com.kodilla.fantasy.domain.Team;
import com.kodilla.fantasy.mapper.ApiFootballMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
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
    private GetTeamsDto teamsResponse;

    @BeforeEach
    void buildTeamResponse() {
        apiFootballTeam1 = new ApiFootballTeamDto(357L, "Test team 1", "TT1");
        apiFootballTeam2 = new ApiFootballTeamDto(358L, "Test team 2", "TT2");
        TeamResponseDto teamResponse1 = new TeamResponseDto(apiFootballTeam1);
        TeamResponseDto teamResponse2 = new TeamResponseDto(apiFootballTeam2);
        teamsResponse = new GetTeamsDto(new TeamResponseDto[]{teamResponse1, teamResponse2});
    }

    @Test
    void testShouldGetTeams() {
        //Given
        Team team1 = new Team(357L, "Test team 1", "TT1");
        Team team2 = new Team(358L, "Test team 2", "TT2");

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
        when(apiFootballClient.fetchTeams()).thenReturn(new GetTeamsDto(new TeamResponseDto[0]));

        //When
        List<Team> fetchedTeams = apiFootballFacade.getAllTeams();

        //Then
        assertEquals(0, fetchedTeams.size());
    }
}
