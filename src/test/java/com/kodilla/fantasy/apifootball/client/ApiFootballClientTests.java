package com.kodilla.fantasy.apifootball.client;

import com.kodilla.fantasy.apifootball.config.ApiFootballConfig;
import com.kodilla.fantasy.apifootball.dto.ApiFootballTeamDto;
import com.kodilla.fantasy.apifootball.dto.GetTeamsDto;
import com.kodilla.fantasy.apifootball.dto.TeamResponseDto;
import org.hibernate.annotations.Fetch;
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

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ApiFootballClientTests {

    private final static String API_FOOTBALL_URL = "https://api-football-v1.p.rapidapi.com/v3";
    private final static String TEAMS_URL = "https://api-football-v1.p.rapidapi.com/v3/teams?league=106&season=2022";

    @InjectMocks
    private ApiFootballClient apiFootballClient;

    @Mock
    private RestTemplate restTemplate;
    @Mock
    private ApiFootballConfig apiFootballConfig;

    private URI url;
    private HttpEntity<Void> requestEntity;

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

    @BeforeEach
    void buildHeaders() throws URISyntaxException {
        url = new URI(TEAMS_URL);
        HttpHeaders headers = new HttpHeaders();
        headers.set("Test_key", "1234");
        headers.set("Test_host", "host");
        requestEntity = new HttpEntity<>(headers);
    }

    @Test
    void shouldFetchTeams() {
        //Given
        ApiFootballTeamDto apiFootballTeam = new ApiFootballTeamDto(357L, "Test", "TET");
        TeamResponseDto teamResponse = new TeamResponseDto(apiFootballTeam);
        GetTeamsDto getTeamsDto = new GetTeamsDto(new TeamResponseDto[]{teamResponse});

        when(restTemplate.exchange(url, HttpMethod.GET, requestEntity, GetTeamsDto.class)).thenReturn(new ResponseEntity<>(getTeamsDto, HttpStatus.OK));

        //When
        GetTeamsDto fetchedResponse = apiFootballClient.fetchTeams();

        //Then
        assertAll(() -> assertEquals(1, fetchedResponse.getTeamResponse().length),
                () -> assertEquals(357L, fetchedResponse.getTeamResponse()[0].getTeam().getId()),
                () -> assertEquals("Test", fetchedResponse.getTeamResponse()[0].getTeam().getName()),
                () -> assertEquals("TET", fetchedResponse.getTeamResponse()[0].getTeam().getCode()));
    }

    @Test
    void shouldFetchEmptyTeams() {
        //Given
        when(restTemplate.exchange(url, HttpMethod.GET, requestEntity, GetTeamsDto.class)).thenReturn(new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR));

        //When
        GetTeamsDto fetchedResponse = apiFootballClient.fetchTeams();

        //Then
        assertEquals(0, fetchedResponse.getTeamResponse().length);
    }
}
