package com.kodilla.fantasy.apifootball.client;

import com.kodilla.fantasy.apifootball.config.ApiFootballConfig;
import com.kodilla.fantasy.apifootball.dto.*;
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
public class ApiFootballClientTests {

    private final static String API_FOOTBALL_URL = "https://api-football-v1.p.rapidapi.com/v3";
    private final static String TEAMS_URL = "https://api-football-v1.p.rapidapi.com/v3/teams?league=106&season=2022";
    private final static String PLAYERS_URL = "https://api-football-v1.p.rapidapi.com/v3/players?league=106&season=2022&page=2";

    @InjectMocks
    private ApiFootballClient apiFootballClient;

    @Mock
    private RestTemplate restTemplate;
    @Mock
    private ApiFootballConfig apiFootballConfig;

    HttpEntity<Void> requestEntity;

    @BeforeEach
    public void buildHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Test_key", "1234");
        headers.set("Test_host", "host");
        requestEntity = new HttpEntity<>(headers);
    }

    @BeforeEach
    public void mockInit() {
        when(apiFootballConfig.getUrl()).thenReturn(API_FOOTBALL_URL);
        when(apiFootballConfig.getKeyHeader()).thenReturn("Test_key");
        when(apiFootballConfig.getKey()).thenReturn("1234");
        when(apiFootballConfig.getHostHeader()).thenReturn("Test_host");
        when(apiFootballConfig.getHost()).thenReturn("host");
        when(apiFootballConfig.getLeague()).thenReturn("106");
        when(apiFootballConfig.getSeason()).thenReturn("2022");
    }

    @Test
    void shouldFetchTeams() throws URISyntaxException {
        //Given
        URI url = new URI(TEAMS_URL);

        ApiFootballTeamDto apiFootballTeam = new ApiFootballTeamDto(357L, "Test", "TET");
        TeamResponseDto teamResponse = new TeamResponseDto(apiFootballTeam);
        GetTeamsDto getTeamsDto = new GetTeamsDto(List.of(teamResponse));

        when(restTemplate.exchange(url, HttpMethod.GET, requestEntity, GetTeamsDto.class)).thenReturn(new ResponseEntity<>(getTeamsDto, HttpStatus.OK));

        //When
        GetTeamsDto fetchedResponse = apiFootballClient.fetchTeams();

        //Then
        assertAll(() -> assertEquals(1, fetchedResponse.getTeamResponse().size()),
                () -> assertEquals(357L, fetchedResponse.getTeamResponse().get(0).getTeam().getId()),
                () -> assertEquals("Test", fetchedResponse.getTeamResponse().get(0).getTeam().getName()),
                () -> assertEquals("TET", fetchedResponse.getTeamResponse().get(0).getTeam().getCode()));
    }

    @Test
    void shouldFetchEmptyTeams() throws URISyntaxException {
        //Given
        URI url = new URI(TEAMS_URL);
        when(restTemplate.exchange(url, HttpMethod.GET, requestEntity, GetTeamsDto.class)).thenReturn(new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR));

        //When
        GetTeamsDto fetchedResponse = apiFootballClient.fetchTeams();

        //Then
        assertEquals(0, fetchedResponse.getTeamResponse().size());
    }

    @Test
    void shouldFetchPlayers() throws URISyntaxException {
        //Given
        URI url = new URI(PLAYERS_URL);

        ApiFootballTeamDto team1 = new ApiFootballTeamDto(257L, "Test team 1", "TT1");
        ApiFootballTeamDto team2 = new ApiFootballTeamDto(258L, "Test team 2", "TT2");
        GamesDto games1 = new GamesDto("Goalkeeper", "6.5678");
        GamesDto games2 = new GamesDto("Defender", "2.34567");
        StatisticsDto statistics1 = new StatisticsDto(team1, games1);
        StatisticsDto statistics2 = new StatisticsDto(team2, games2);

        ApiFootballPlayerDto player1 = new ApiFootballPlayerDto(357L, "Test name 1", "Test name 1", 21);
        ApiFootballPlayerDto player2 = new ApiFootballPlayerDto(358L, "Test name 2", "Test name 2", 22);
        PlayerResponseDto response1 = new PlayerResponseDto(player1, List.of(statistics1));
        PlayerResponseDto response2 = new PlayerResponseDto(player2, List.of(statistics2));
        PagingDto pagingDto = new PagingDto(2, 37);
        GetPlayersDto playersDto = new GetPlayersDto(pagingDto, List.of(response1, response2));

        when(restTemplate.exchange(url, HttpMethod.GET, requestEntity, GetPlayersDto.class)).thenReturn(new ResponseEntity<>(playersDto, HttpStatus.OK));

        //When
        GetPlayersDto fetchedPlayers = apiFootballClient.fetchPlayers(2);

        //Then
        assertAll(() -> assertEquals(2, fetchedPlayers.getPaging().getCurrent()),
                () -> assertEquals(2, fetchedPlayers.getResponse().size()),
                () -> assertEquals(258L, fetchedPlayers.getResponse().get(1).getStatistics().get(0).getTeam().getId()),
                () -> assertEquals("Defender", fetchedPlayers.getResponse().get(1).getStatistics().get(0).getGames().getPosition()),
                () -> assertEquals(358L, fetchedPlayers.getResponse().get(1).getPlayer().getId()));
    }

    @Test
    void shouldFetchEmptyPlayers() throws URISyntaxException {
        //Given
        URI url = new URI(PLAYERS_URL);
        when(restTemplate.exchange(url, HttpMethod.GET, requestEntity, GetPlayersDto.class)).thenReturn(new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR));

        //When
        GetPlayersDto fetchedPlayers = apiFootballClient.fetchPlayers(2);

        //Then
        assertAll(() -> assertEquals(2, fetchedPlayers.getPaging().getCurrent()),
                () -> assertEquals(2, fetchedPlayers.getPaging().getTotal()),
                () -> assertEquals(0, fetchedPlayers.getResponse().size()));
    }
}
