package com.kodilla.fantasy.livescore.facade;

import com.kodilla.fantasy.domain.Player;
import com.kodilla.fantasy.domain.Position;
import com.kodilla.fantasy.domain.Team;
import com.kodilla.fantasy.livescore.client.LiveScoreClient;
import com.kodilla.fantasy.livescore.domain.EventType;
import com.kodilla.fantasy.livescore.domain.Match;
import com.kodilla.fantasy.livescore.domain.dto.*;
import com.kodilla.fantasy.livescore.domain.exception.CouldNotMapElement;
import com.kodilla.fantasy.livescore.domain.exception.NoResponseException;
import com.kodilla.fantasy.livescore.mapper.LiveScoreMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class LiveScoreFacadeTests {

    @InjectMocks
    private LiveScoreFacade facade;
    @Mock
    private LiveScoreClient liveScoreClient;
    @Mock
    private LiveScoreMapper liveScoreMapper;

    @Test
    void testAddLineup() throws NoResponseException {
        //Given
        LiveScorePlayerDto playerDto1 = new LiveScorePlayerDto("Firstname", "Lastname");
        LiveScorePlayerDto playerDto2 = new LiveScorePlayerDto("Firstname 1", "Lastname 1");
        LineupDto lineupDto1 = new LineupDto(List.of(playerDto1));
        LineupDto lineupDto2 = new LineupDto(List.of(playerDto2));
        LineupsDataDto lineupsDataDto = new LineupsDataDto(lineupDto1, lineupDto2);
        GetLineupsDto getLineupsDto = new GetLineupsDto(lineupsDataDto);

        Team team1 = new Team(1L, 1L, "Test", "TET", new ArrayList<>());
        Team team2 = new Team(2L,2L, "Team 2", "TE2", new ArrayList<>());
        Match emptyMatch = new Match("1", team1, team2, new ArrayList<>(), new ArrayList<>(), new ArrayList<>());

        when(liveScoreClient.fetchLineups("1")).thenReturn(getLineupsDto);

        //When
        facade.addLineup(emptyMatch);

        //Then
        verify(liveScoreMapper, times(1)).mapLineup(emptyMatch, getLineupsDto);
    }

    @Test
    void testAddEvents() {
        //Given
        GetEventsDto getEventsDto = new GetEventsDto(new ArrayList<>());
        EventDataDto eventDto1 = new EventDataDto("YELLOW_CARD", "Firstname Lastname", 1, new ArrayList<>());
        EventDto eventDto2 = new EventDto("GOAL", "Firstname Lastname", 1);
        EventDto eventDto3 = new EventDto("GOAL_ASSIST", "Firstname 1 Lastname 1", 2);
        EventDataDto eventDto4 = new EventDataDto(null, null, 0, List.of(eventDto2, eventDto3));
        getEventsDto.getEvents().addAll(List.of(eventDto1, eventDto4));

        Team team1 = new Team(1L, 1L, "Test", "TET", new ArrayList<>());
        Team team2 = new Team(2L,2L, "Team 2", "TE2", new ArrayList<>());
        Match emptyMatch = new Match("1", team1, team2, new ArrayList<>(), new ArrayList<>(), new ArrayList<>());

        when(liveScoreClient.fetchEvents("1")).thenReturn(getEventsDto);

        //When
        facade.addEvents(emptyMatch);

        //Then
        verify(liveScoreMapper, times(1)).mapEvents(emptyMatch, getEventsDto);
    }

    @Test
    void testFindMatches() throws CouldNotMapElement {
        //Given
        LiveScoreTeamDto teamDto1 = new LiveScoreTeamDto("Test team 1");
        LiveScoreTeamDto teamDto2 = new LiveScoreTeamDto("Team 2");
        MatchDto matchDto = new MatchDto("1", teamDto1, teamDto2);
        GetMatchesDto getMatchesDto = new GetMatchesDto(List.of(matchDto, matchDto));

        Team team1 = new Team(1L, 1L, "Test", "TET", new ArrayList<>());
        Team team2 = new Team(2L, 2L, "Team 2", "TE2", new ArrayList<>());
        Match match = new Match("3", team1, team2, new ArrayList<>(), new ArrayList<>(), new ArrayList<>());

        when(liveScoreClient.fetchMatches(1)).thenReturn(getMatchesDto);
        when(liveScoreMapper.mapToMatchList(getMatchesDto)).thenReturn(List.of(match));

        //When
        List<Match> matches = facade.findMatches(1);

        //Then
        assertAll(() -> assertEquals(1, matches.size()),
                () -> assertEquals(1L, matches.get(0).getTeam1().getId()),
                () -> assertEquals(2L, matches.get(0).getTeam2().getId()));
    }

    @Test
    void testFetchMatches() throws CouldNotMapElement, NoResponseException {
        //Given
        LiveScorePlayerDto playerDto1 = new LiveScorePlayerDto("Firstname", "Lastname");
        LiveScorePlayerDto playerDto2 = new LiveScorePlayerDto("Firstname 1", "Lastname 1");
        LineupDto lineupDto1 = new LineupDto(List.of(playerDto1));
        LineupDto lineupDto2 = new LineupDto(List.of(playerDto2));
        LineupsDataDto lineupsDataDto = new LineupsDataDto(lineupDto1, lineupDto2);
        GetLineupsDto getLineupsDto = new GetLineupsDto(lineupsDataDto);

        Team team1 = new Team(1L, 1L, "Test", "TET", new ArrayList<>());
        Team team2 = new Team(2L,2L, "Team 2", "TE2", new ArrayList<>());
        Player player1 = new Player(3L, 3L, "Firstname", "Lastname", 21, BigDecimal.ZERO, Position.GK, team1, new ArrayList<>());
        Player player2 = new Player(3L, 3L, "Firstname 1", "Lastname 1", 21, BigDecimal.ZERO, Position.GK, team2, new ArrayList<>());
        Event event1 = new Event(EventType.YELLOW_CARD, player1);
        Event event2 = new Event(EventType.GOAL, player2);
        Event event3 = new Event(EventType.GOAL_ASSIST, player1);
        Match match = new Match("1", team1, team2, List.of(player1), List.of(player2), List.of(event1, event2, event3));

        LiveScoreTeamDto teamDto1 = new LiveScoreTeamDto("Test team 1");
        LiveScoreTeamDto teamDto2 = new LiveScoreTeamDto("Team 2");
        MatchDto matchDto = new MatchDto("1", teamDto1, teamDto2);
        GetMatchesDto getMatchesDto = new GetMatchesDto(List.of(matchDto, matchDto));

        GetEventsDto getEventsDto = new GetEventsDto(new ArrayList<>());
        EventDataDto eventDto1 = new EventDataDto("YELLOW_CARD", "Firstname Lastname", 1, new ArrayList<>());
        EventDto eventDto2 = new EventDto("GOAL", "Firstname Lastname", 1);
        EventDto eventDto3 = new EventDto("GOAL_ASSIST", "Firstname 1 Lastname 1", 2);
        EventDataDto eventDto4 = new EventDataDto(null, null, 0, List.of(eventDto2, eventDto3));
        getEventsDto.getEvents().addAll(List.of(eventDto1, eventDto4));

        when(liveScoreClient.fetchLineups("1")).thenReturn(getLineupsDto);
        when(liveScoreClient.fetchMatches(1)).thenReturn(getMatchesDto);
        when(liveScoreClient.fetchEvents("1")).thenReturn(getEventsDto);
        when(liveScoreMapper.mapToMatchList(getMatchesDto)).thenReturn(List.of(match));

        //When
        List<Match> fetchedMatches = facade.fetchMatches(1);

        //Then
        assertAll(() -> assertEquals(1, fetchedMatches.size()),
                () -> assertEquals("Firstname", fetchedMatches.get(0).getLineup1().get(0).getFirstname()),
                () -> assertEquals("Firstname 1", fetchedMatches.get(0).getLineup2().get(0).getFirstname()),
                () -> assertEquals(3, fetchedMatches.get(0).getEvents().size()));
    }
}
