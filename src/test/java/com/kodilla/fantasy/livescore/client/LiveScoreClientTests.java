package com.kodilla.fantasy.livescore.client;

import com.kodilla.fantasy.livescore.config.LiveScoreConfig;
import com.kodilla.fantasy.livescore.domain.dto.GetMatchesDto;
import com.kodilla.fantasy.livescore.domain.dto.LiveScoreTeamDto;
import com.kodilla.fantasy.livescore.domain.dto.MatchDto;
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
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class LiveScoreClientTests {

    private final static String LIVE_SCORE_URL = "https://livescore-football.p.rapidapi.com/soccer";
    private final static String MATCHES_URL = "https://livescore-football.p.rapidapi.com/soccer/matches-by-league?country_code=poland&league_code=ekstraklasa&round=1";

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
        when(liveScoreConfig.getCountryCode()).thenReturn("poland");
        when(liveScoreConfig.getLeagueCode()).thenReturn("ekstraklasa");
    }

    @Test
    void shouldFetchMatches() throws URISyntaxException {
        //Given
        URI url = new URI(MATCHES_URL);

        LiveScoreTeamDto team1 = new LiveScoreTeamDto("Test team 1");
        LiveScoreTeamDto team2 = new LiveScoreTeamDto("Team 2");
        MatchDto matchDto = new MatchDto("1", team1, team2);
        GetMatchesDto getMatchesDto = new GetMatchesDto(List.of(matchDto));

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
}
