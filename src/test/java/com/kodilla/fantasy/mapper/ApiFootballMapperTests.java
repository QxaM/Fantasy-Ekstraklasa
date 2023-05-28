package com.kodilla.fantasy.mapper;

import com.kodilla.fantasy.apifootball.dto.*;
import com.kodilla.fantasy.domain.Player;
import com.kodilla.fantasy.domain.Position;
import com.kodilla.fantasy.domain.Team;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class ApiFootballMapperTests {

    @Autowired
    private ApiFootballMapper apiFootballMapper;

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
        ApiFootballPlayerDto playerDto = new ApiFootballPlayerDto(357L, "Test name", "Test name", 21);
        ApiFootballTeamDto apiFootballTeamDto = new ApiFootballTeamDto(357L, "Test team", "TET");
        GamesDto gamesDto = new GamesDto("Goalkeeper", "6.0");
        StatisticsDto statisticsDto = new StatisticsDto(apiFootballTeamDto, gamesDto);
        PlayerResponseDto playerResponseDto = new PlayerResponseDto(
                playerDto,
                new StatisticsDto[]{statisticsDto}
        );

        //When
        Player mappedPlayer = apiFootballMapper.mapToPlayer(playerResponseDto);

        //Then
        assertAll(() -> assertEquals(357L, mappedPlayer.getApiFootballId()),
                () -> assertEquals("Test name", mappedPlayer.getFirstname()),
                () -> assertEquals("Test name", mappedPlayer.getLastname()),
                () -> assertEquals(21, mappedPlayer.getAge()),
                () -> assertEquals(Position.GK, mappedPlayer.getPosition()));
    }
}
