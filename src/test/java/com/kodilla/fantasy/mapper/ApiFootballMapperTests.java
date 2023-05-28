package com.kodilla.fantasy.mapper;

import com.kodilla.fantasy.apifootball.dto.ApiFootballTeamDto;
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
}
