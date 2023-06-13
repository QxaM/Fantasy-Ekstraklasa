package com.kodilla.fantasy.livescore.mapper;

import com.kodilla.fantasy.domain.Player;
import com.kodilla.fantasy.domain.Position;
import com.kodilla.fantasy.domain.Team;
import com.kodilla.fantasy.domain.exception.ElementNotFoundException;
import com.kodilla.fantasy.livescore.domain.EventType;
import com.kodilla.fantasy.livescore.domain.Match;
import com.kodilla.fantasy.livescore.domain.dto.*;
import com.kodilla.fantasy.livescore.domain.exception.CouldNotMapElement;
import com.kodilla.fantasy.service.PlayerDbService;
import com.kodilla.fantasy.service.TeamDbService;
import com.kodilla.fantasy.validator.EventTypeValidator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
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
    @Mock
    private EventTypeValidator eventValidator;

    @Test
    void shouldMapToMatch() {
        //Given
        LiveScoreTeamDto teamDto1 = new LiveScoreTeamDto("Test team 1");
        LiveScoreTeamDto teamDto2 = new LiveScoreTeamDto("Team 2");
        MatchDto matchDto = new MatchDto("1", teamDto1, teamDto2);

        Team team1 = new Team(1L, "Test", "TET", new ArrayList<>());
        Team team2 = new Team(2L, "Team 2", "TE2", new ArrayList<>());
        List<Team> teams = List.of(team1, team2);

        //When
        Match mappedMatch = new Match();
        try {
            mappedMatch = liveScoreMapper.mapToMatch(matchDto, teams);
        } catch (CouldNotMapElement e) {}

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
        List<Team> teams = List.of(team1, team2);

        //When + Then
        assertThrows(CouldNotMapElement.class, () -> liveScoreMapper.mapToMatch(matchDto, teams));
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
        } catch (CouldNotMapElement e) {}

        //Then
        assertEquals(2, mappedMatches.size());
    }

    @Test
    void shouldMapLineup() {
        //Given
        Team team1 = new Team(1L, 1L, "Test", "TET", new ArrayList<>());
        Team team2 = new Team(2L,2L, "Team 2", "TE2", new ArrayList<>());
        Match match = new Match("1", team1, team2, new HashMap<>());

        LiveScorePlayerDto playerDto1 = new LiveScorePlayerDto("Firstname", "Lastname");
        LiveScorePlayerDto playerDto2 = new LiveScorePlayerDto("Firstname 1", "Lastname 1");
        LineupDto lineupDto1 = new LineupDto(List.of(playerDto1));
        LineupDto lineupDto2 = new LineupDto(List.of(playerDto2));
        LineupsDataDto lineupsDataDto = new LineupsDataDto(lineupDto1, lineupDto2);
        GetLineupsDto getLineupsDto = new GetLineupsDto(lineupsDataDto);

        Player player1 = new Player(3L, 3L, "name", "Firstname", "Lastname", 21, BigDecimal.ZERO, Position.GK, team1, new ArrayList<>(), 0);
        Player player2 = new Player(3L, 3L, "name 1", "Firstname 1", "Lastname 1", 21, BigDecimal.ZERO, Position.GK, team1, new ArrayList<>(), 0);

        when(playerDbService.getPlayerByTeamId(1L)).thenReturn(List.of(player1, player2));
        when(playerDbService.getPlayerByTeamId(2L)).thenReturn(List.of(player2, player1));

        //When
        liveScoreMapper.mapLineup(match, getLineupsDto);

        //Then
        assertEquals(2, match.getEvents().size());
    }

    @Test
    void shouldMapEvent() {
        //Given
        Team team1 = new Team(1L, 1L, "Test", "TET", new ArrayList<>());
        Team team2 = new Team(2L,2L, "Team 2", "TE2", new ArrayList<>());
        Player player1 = new Player(3L, 3L, "name", "Firstname", "Lastname", 21, BigDecimal.ZERO, Position.GK, team1, new ArrayList<>(), 0);
        Player player2 = new Player(3L, 3L, "name1", "First", "Last", 21, BigDecimal.ZERO, Position.GK, team1, new ArrayList<>(), 0);
        Player player3 = new Player(4L, 4L, "name2", "Name", "Eman", 21, BigDecimal.ZERO, Position.GK, team1, new ArrayList<>(), 0);
        Player player4 = new Player(3L, 3L, "name3", "Test", "Test", 21, BigDecimal.ZERO, Position.GK, team1, new ArrayList<>(), 0);
        team1.getPlayers().addAll(List.of(player1, player3));
        team2.getPlayers().addAll(List.of(player2, player4));

        Match match = new Match("1", team1, team2, new HashMap<>());
        match.addEvent(player1, EventType.LINEUP);
        match.addEvent(player2, EventType.LINEUP);

        GetEventsDto getEventsDto = new GetEventsDto(new ArrayList<>());
        EventDataDto eventDto1 = new EventDataDto("YELLOW_CARD", "Firstname Lastname", 1, null);
        EventDto eventDto2 = new EventDto("GOAL", "Firstname Lastname", 1);
        EventDto eventDto3 = new EventDto("GOAL_ASSIST", "name1", 2);
        EventDataDto eventDto4 = new EventDataDto(null, null, 0, List.of(eventDto2, eventDto3));

        getEventsDto.getEvents().addAll(List.of(eventDto1, eventDto4));
        when(eventValidator.validateEvent("YELLOW_CARD")).thenReturn(EventType.YELLOW_CARD);
        when(eventValidator.validateEvent("GOAL")).thenReturn(EventType.GOAL);
        when(eventValidator.validateEvent("GOAL_ASSIST")).thenReturn(EventType.GOAL_ASSIST);

        //When
        liveScoreMapper.mapEvents(match, getEventsDto);

        //Then
        assertAll(() -> assertEquals(2, match.getEvents().size()),
                () -> assertEquals(3, match.getEvents().get(player1).size()),
                () -> assertEquals(2, match.getEvents().get(player2).size()));
    }
}
