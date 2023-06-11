package com.kodilla.fantasy.livescore.mapper;

import com.kodilla.fantasy.domain.Player;
import com.kodilla.fantasy.domain.Position;
import com.kodilla.fantasy.domain.Team;
import com.kodilla.fantasy.domain.exception.ElementNotFoundException;
import com.kodilla.fantasy.livescore.domain.Match;
import com.kodilla.fantasy.livescore.domain.dto.*;
import com.kodilla.fantasy.livescore.domain.exception.CouldNotMapTeam;
import com.kodilla.fantasy.service.PlayerDbService;
import com.kodilla.fantasy.service.TeamDbService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class LiveScoreMapperTests {

    @InjectMocks
    private LiveScoreMapper liveScoreMapper;
    @Mock
    private TeamDbService teamDbService;
    @Mock
    private PlayerDbService playerDbService;

    @Test
    void shouldMapToMatch() {
        //Given
        LiveScoreTeamDto teamDto1 = new LiveScoreTeamDto("Test team 1");
        LiveScoreTeamDto teamDto2 = new LiveScoreTeamDto("Team 2");
        MatchDto matchDto = new MatchDto("1", teamDto1, teamDto2);

        Team team1 = new Team(1L, "Test", "TET", new ArrayList<>());
        Team team2 = new Team(2L, "Team 2", "TE2", new ArrayList<>());

        when(teamDbService.getTeams()).thenReturn(List.of(team1, team2));

        //When
        Match mappedMatch = new Match();
        try {
            mappedMatch = liveScoreMapper.mapToMatch(matchDto);
        } catch (CouldNotMapTeam e) {}

        //Then
        assertEquals("1", mappedMatch.getMatchId());
        assertEquals("Test", mappedMatch.getTeam1().getName());
        assertEquals("Team 2", mappedMatch.getTeam2().getName());
    }

    @Test
    void shouldNotMapToMatch() {
        //Given
        LiveScoreTeamDto teamDto1 = new LiveScoreTeamDto("Test team 1");
        LiveScoreTeamDto teamDto2 = new LiveScoreTeamDto("Team 2");
        MatchDto matchDto = new MatchDto("1", teamDto1, teamDto2);

        Team team1 = new Team(1L, "Different", "TET", new ArrayList<>());
        Team team2 = new Team(2L, "Team 2", "TE2", new ArrayList<>());

        when(teamDbService.getTeams()).thenReturn(List.of(team1, team2));

        //When + Then
        assertThrows(CouldNotMapTeam.class, () -> liveScoreMapper.mapToMatch(matchDto));
    }

    @Test
    void shouldMapToList() {
        //Given
        LiveScoreTeamDto teamDto1 = new LiveScoreTeamDto("Test team 1");
        LiveScoreTeamDto teamDto2 = new LiveScoreTeamDto("Team 2");
        MatchDto matchDto = new MatchDto("1", teamDto1, teamDto2);
        GetMatchesDto getMatchesDto = new GetMatchesDto(List.of(matchDto, matchDto));

        Team team1 = new Team(1L, "Test", "TET", new ArrayList<>());
        Team team2 = new Team(2L, "Team 2", "TE2", new ArrayList<>());

        when(teamDbService.getTeams()).thenReturn(List.of(team1, team2));

        //When
        List<Match> mappedMatches = new ArrayList<>();
        try {
            mappedMatches = liveScoreMapper.mapToMatchList(getMatchesDto);
        } catch (CouldNotMapTeam e) {}

        //Then
        assertEquals(2, mappedMatches.size());
    }

    @Test
    void shouldMapLineup() throws ElementNotFoundException {
        //Given
        Team team1 = new Team(1L, 1L, "Test", "TET", new ArrayList<>());
        Team team2 = new Team(2L,2L, "Team 2", "TE2", new ArrayList<>());
        Match match = new Match("1", team1, team2, new ArrayList<>(), new ArrayList<>());

        LiveScorePlayerDto playerDto1 = new LiveScorePlayerDto("Firstname", "Lastname");
        LiveScorePlayerDto playerDto2 = new LiveScorePlayerDto("Firstname 1", "Lastname 1");
        LineupDto lineupDto1 = new LineupDto(List.of(playerDto1));
        LineupDto lineupDto2 = new LineupDto(List.of(playerDto2));
        LineupsDataDto lineupsDataDto = new LineupsDataDto(lineupDto1, lineupDto2);
        GetLineupsDto getLineupsDto = new GetLineupsDto(lineupsDataDto);

        Player player1 = new Player(3L, 3L, "Firstname", "Lastname", 21, BigDecimal.ZERO, Position.GK, team1, new ArrayList<>());
        Player player2 = new Player(3L, 3L, "Firstname 1", "Lastname 1", 21, BigDecimal.ZERO, Position.GK, team1, new ArrayList<>());

        when(playerDbService.getPlayerByFirstnameAndLastname(
                "Firstname",
                "Lastname",
                1L)).thenReturn(player1);
        when(playerDbService.getPlayerByFirstnameAndLastname(
                "Firstname 1",
                "Lastname 1",
                2L)).thenReturn(player2);

        //When
        Match mappedMatch = liveScoreMapper.mapLineup(match, getLineupsDto);

        //Then
        assertAll(() -> assertEquals(1, mappedMatch.getLineup1().size()),
                () -> assertEquals(1, mappedMatch.getLineup2().size()));

    }
}
