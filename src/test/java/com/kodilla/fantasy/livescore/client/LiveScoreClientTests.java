package com.kodilla.fantasy.livescore.client;

import com.kodilla.fantasy.livescore.config.LiveScoreConfig;
import com.kodilla.fantasy.livescore.domain.dto.*;
import com.kodilla.fantasy.livescore.domain.exception.NoResponseException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class LiveScoreClientTests {

    private final static String LIVE_SCORE_URL = "https://livescore-football.p.rapidapi.com/soccer";
    private final static String MATCHES_URL = "https://livescore-football.p.rapidapi.com/soccer/matches-by-league?country_code=poland&league_code=ekstraklasa&round=1";
    private final static String LINEUPS_URL = "https://livescore-football.p.rapidapi.com/soccer/match-lineups?match_id=1";
    private final static String EVENTS_URL = "https://livescore-football.p.rapidapi.com/soccer/match-events?match_id=1";

    @InjectMocks
    private LiveScoreClient liveScoreClient;
    @Mock
    private RestTemplate restTemplate;
    @Mock
    private LiveScoreConfig liveScoreConfig;

    private HttpEntity<Void> requestEntity;

    @BeforeEach
    void buildHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Test_key", "1234");
        headers.set("Test_host", "host");
        requestEntity = new HttpEntity<>(headers);
    }

    @BeforeEach
    void mockInit() {
        when(liveScoreConfig.getUrl()).thenReturn(LIVE_SCORE_URL);
        when(liveScoreConfig.getKeyHeader()).thenReturn("Test_key");
        when(liveScoreConfig.getKey()).thenReturn("1234");
        when(liveScoreConfig.getHostHeader()).thenReturn("Test_host");
        when(liveScoreConfig.getHost()).thenReturn("host");
    }

    @Test
    void shouldFetchMatches() throws URISyntaxException {
        //Given
        URI url = new URI(MATCHES_URL);

        LiveScoreTeamDto team1 = new LiveScoreTeamDto("Test team 1");
        LiveScoreTeamDto team2 = new LiveScoreTeamDto("Team 2");
        MatchDto matchDto = new MatchDto("1", team1, team2);
        GetMatchesDto getMatchesDto = new GetMatchesDto(List.of(matchDto));

        when(liveScoreConfig.getCountryCode()).thenReturn("poland");
        when(liveScoreConfig.getLeagueCode()).thenReturn("ekstraklasa");
        when(restTemplate.exchange(url, HttpMethod.GET, requestEntity, GetMatchesDto.class))
                .thenReturn(new ResponseEntity<>(getMatchesDto, HttpStatus.OK));

        //Then
        GetMatchesDto fetchedMatches = liveScoreClient.fetchMatches(1);

        //when
        assertAll(() -> assertEquals(1, fetchedMatches.getMatches().size()),
                () -> assertEquals("1", fetchedMatches.getMatches().get(0).getMatchId()),
                () -> assertEquals("Test team 1", fetchedMatches.getMatches().get(0).getTeam1().getName()),
                () -> assertEquals("Team 2", fetchedMatches.getMatches().get(0).getTeam2().getName()));
    }

    @Test
    void shouldFetchLineups() throws URISyntaxException {
        //Given
        URI url = new URI(LINEUPS_URL);

        LiveScorePlayerDto player1 = new LiveScorePlayerDto("firstname", "lastname");
        LiveScorePlayerDto player2 = new LiveScorePlayerDto("firstname 1", "lastname 2");
        LineupDto lineup1 = new LineupDto(List.of(player1));
        LineupDto lineup2 = new LineupDto(List.of(player2));
        LineupsDataDto lineupsDataDto = new LineupsDataDto(lineup1, lineup2);
        GetLineupsDto getLineupsDto = new GetLineupsDto(lineupsDataDto);

        when(restTemplate.exchange(url, HttpMethod.GET, requestEntity, GetLineupsDto.class))
                .thenReturn(new ResponseEntity<>(getLineupsDto, HttpStatus.OK));

        //When
        GetLineupsDto fetchedLineups = new GetLineupsDto();
        try {
            fetchedLineups = liveScoreClient.fetchLineups("1");
        } catch (NoResponseException e) {}

        //Then
        List<LiveScorePlayerDto> fetchedPlayers1 = fetchedLineups.getData().getTeam1().getPlayers();
        List<LiveScorePlayerDto> fetchedPlayers2 = fetchedLineups.getData().getTeam2().getPlayers();
        assertAll(() -> assertEquals(1, fetchedPlayers1.size()),
                () -> assertEquals(1, fetchedPlayers2.size()),
                () -> assertEquals("firstname", fetchedPlayers1.get(0).getFirstName()),
                () -> assertEquals("firstname 1", fetchedPlayers2.get(0).getFirstName()));
    }

    @Test
    void shouldNotFetchLineups() throws URISyntaxException {
        //Given
        URI url = new URI(LINEUPS_URL);

        when(restTemplate.exchange(url, HttpMethod.GET, requestEntity, GetLineupsDto.class))
                .thenReturn(new ResponseEntity<>(null, HttpStatus.OK));

        //When + Then
        assertThrows(NoResponseException.class, () -> liveScoreClient.fetchLineups("1"));
    }

    @Test
    void shouldFetchEvents() throws URISyntaxException {
        //Given
        URI url = new URI(EVENTS_URL);

        GetEventsDto eventsDto = new GetEventsDto(new ArrayList<>());
        EventDataDto eventDataDto1 = new EventDataDto("EVENT1", "Player1", 1, null);
        EventDataDto eventDataDto2 = new EventDataDto(null, null, 0, new ArrayList<>());
        EventDataDto eventDataDto3 = new EventDataDto("EVENT3", "Player3", 1, null);
        EventDataDto eventDataDto4 = new EventDataDto("EVENT4", "Player4", 1, null);
        eventDataDto2.getEvents().add(eventDataDto3);
        eventDataDto2.getEvents().add(eventDataDto4);
        eventsDto.getEvents().add(eventDataDto1);
        eventsDto.getEvents().add(eventDataDto2);

        when(restTemplate.exchange(url, HttpMethod.GET, requestEntity, GetEventsDto.class))
                .thenReturn(new ResponseEntity<>(eventsDto, HttpStatus.OK));

        //When
        GetEventsDto fetchedEvents = liveScoreClient.fetchEvents("1");

        //Then
        assertAll(() -> assertEquals("EVENT1", fetchedEvents.getEvents().get(0).getEvent()),
                () -> assertEquals("EVENT3", fetchedEvents.getEvents().get(1).getEvents().get(0).getEvent()),
                () -> assertEquals("EVENT4", fetchedEvents.getEvents().get(1).getEvents().get(1).getEvent()));
    }

    @Test
    void shouldNotFetchEvents() throws URISyntaxException {
        //Given
        URI url = new URI(EVENTS_URL);

        when(restTemplate.exchange(url, HttpMethod.GET, requestEntity, GetEventsDto.class))
                .thenReturn(new ResponseEntity<>(new GetEventsDto(Collections.emptyList()), HttpStatus.OK));

        //When
        GetEventsDto fetchedEvents = liveScoreClient.fetchEvents("1");

        //Then
        assertTrue(fetchedEvents.getEvents().isEmpty());
    }
}
