package com.kodilla.fantasy.mapper;

import com.kodilla.fantasy.apifootball.dto.*;
import com.kodilla.fantasy.domain.Player;
import com.kodilla.fantasy.domain.Position;
import com.kodilla.fantasy.domain.Team;
import com.kodilla.fantasy.service.TeamDbService;
import com.kodilla.fantasy.validator.PlayerValidator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ApiFootballMapperTests {

    @InjectMocks
    private ApiFootballMapper apiFootballMapper;
    @Mock
    private PlayerValidator playerValidator;
    @Mock
    private TeamDbService teamDbService;

    @Test
    void testMapToTeam() {
        //Given
        ApiFootballTeamDto apiFootballTeamDto = new ApiFootballTeamDto(
                357L,
                "Test team",
                "TET"
        );
        //When
        Team mappedTeam = apiFootballMapper.mapToTeam(apiFootballTeamDto);

        //Then
        assertAll(() -> assertEquals(357L, mappedTeam.getApiFootballId()),
                () -> assertEquals("Test team", mappedTeam.getName()),
                () -> assertEquals("TET", mappedTeam.getCode() ));
    }

    @Test
    void testMapToPlayer() {
        //Given
        ApiFootballPlayerDto playerDto = new ApiFootballPlayerDto(357L, "Test nickname", "Test name", "Test name", 21);
        ApiFootballTeamDto apiFootballTeamDto = new ApiFootballTeamDto(357L, "Test team", "TET");
        GamesDto gamesDto = new GamesDto("Goalkeeper", "6.0");
        StatisticsDto statisticsDto = new StatisticsDto(apiFootballTeamDto, gamesDto);
        PlayerResponseDto playerResponseDto = new PlayerResponseDto(
                playerDto,
                List.of(statisticsDto)
        );
        when(playerValidator.validatePosition("Goalkeeper")).thenReturn(Position.GK);

        //When
        Player mappedPlayer = apiFootballMapper.mapToPlayer(playerResponseDto);

        //Then
        assertAll(() -> assertEquals(357L, mappedPlayer.getApiFootballId()),
                () -> assertEquals("Test nickname", mappedPlayer.getName()),
                () -> assertEquals("Test name", mappedPlayer.getFirstname()),
                () -> assertEquals("Test name", mappedPlayer.getLastname()),
                () -> assertEquals(21, mappedPlayer.getAge()),
                () -> assertEquals(Position.GK, mappedPlayer.getPosition()));
    }

    @Test
    void testMapToPlayerWithScore() {
        //Given
        ApiFootballPlayerDto playerDto = new ApiFootballPlayerDto(357L, "name", "Test name", "Test name", 21);
        ApiFootballTeamDto apiFootballTeamDto = new ApiFootballTeamDto(357L, "Test team", "TET");
        GamesDto gamesDto = new GamesDto("Goalkeeper", "8.0");
        StatisticsDto statisticsDto = new StatisticsDto(apiFootballTeamDto, gamesDto);
        PlayerResponseDto playerResponseDto = new PlayerResponseDto(
                playerDto,
                List.of(statisticsDto)
        );
        when(playerValidator.validatePosition("Goalkeeper")).thenReturn(Position.GK);

        //When
        Player mappedPlayer = apiFootballMapper.mapToPlayer(playerResponseDto);

        //Then
        assertEquals(BigDecimal.valueOf(5010000.0), mappedPlayer.getValue());
    }

    @Test
    void testMapToPlayerWithTeam() {
        //Given
        ApiFootballPlayerDto playerDto = new ApiFootballPlayerDto(357L, "name",  "Test name", "Test name", 21);
        ApiFootballTeamDto apiFootballTeamDto = new ApiFootballTeamDto(357L, "Test team", "TET");
        GamesDto gamesDto = new GamesDto("Goalkeeper", "8.0");
        StatisticsDto statisticsDto = new StatisticsDto(apiFootballTeamDto, gamesDto);
        PlayerResponseDto playerResponseDto = new PlayerResponseDto(
                playerDto,
                List.of(statisticsDto)
        );
        Team team = new Team(2L,357L, "Test team", "TET", new ArrayList<>());
        when(playerValidator.validatePosition("Goalkeeper")).thenReturn(Position.GK);
        when(teamDbService.getTeamByApiFootballId(357L)).thenReturn(team);

        //When
        Player mappedPlayer = apiFootballMapper.mapToPlayer(playerResponseDto);

        //Then
        assertAll(() -> assertEquals(2L, mappedPlayer.getTeam().getId()),
                () -> assertEquals("Test team", mappedPlayer.getTeam().getName()),
                () -> assertEquals("TET", mappedPlayer.getTeam().getCode()));
    }
    @Test
    void testMapToPlayerWithEmptyTeam(){
        //Given
        ApiFootballPlayerDto playerDto = new ApiFootballPlayerDto(357L, "name", "Test name", "Test name", 21);
        ApiFootballTeamDto apiFootballTeamDto = new ApiFootballTeamDto(357L, "Test team", "TET");
        GamesDto gamesDto = new GamesDto("Goalkeeper", "8.0");
        StatisticsDto statisticsDto = new StatisticsDto(apiFootballTeamDto, gamesDto);
        PlayerResponseDto playerResponseDto = new PlayerResponseDto(
                playerDto,
                List.of(statisticsDto)
        );
        when(teamDbService.getTeamByApiFootballId(357L)).thenReturn(new Team());
        when(playerValidator.validatePosition("Goalkeeper")).thenReturn(Position.GK);

        //When
        Player mappedPlayer = apiFootballMapper.mapToPlayer(playerResponseDto);

        //Then
        assertAll(() -> assertNull(mappedPlayer.getTeam().getId()),
                () -> assertNull(mappedPlayer.getTeam().getName()),
                () -> assertNull(mappedPlayer.getTeam().getCode()));
    }
}
