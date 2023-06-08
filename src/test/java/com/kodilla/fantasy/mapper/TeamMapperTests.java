package com.kodilla.fantasy.mapper;

import com.kodilla.fantasy.domain.Team;
import com.kodilla.fantasy.domain.dto.TeamDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class TeamMapperTests {

    @Autowired
    private TeamMapper teamMapper;


    @Test
    void testMapToTeam() {
        //Given
        TeamDto teamDto = new TeamDto(3L, "Test", "TET");

        //When
        Team mappedTeam = teamMapper.mapToTeam(teamDto);

        //Then
        assertAll(() -> assertEquals(3L, mappedTeam.getId()),
                () -> assertEquals("Test", mappedTeam.getName()),
                () -> assertEquals("TET", mappedTeam.getCode()));
    }

    @Test
    void testMapToTeamDto() {
        //Given
        Team team = new Team(3L, 4L, "Test", "TET", new ArrayList<>());

        //When
        TeamDto mappedTeam = teamMapper.mapToTeamDto(team);

        //Then
        assertAll(() -> assertEquals(3L, mappedTeam.getId()),
                () -> assertEquals("Test", mappedTeam.getName()),
                () -> assertEquals("TET", mappedTeam.getCode()));
    }

    @Test
    void testMapToTeamDtoList() {
        //Given
        Team team1 = new Team(3L, 4L, "Test1", "TE1", new ArrayList<>());
        Team team2 = new Team(4L, 5L, "Test2", "TE2", new ArrayList<>());
        Team team3 = new Team(5L, 6L, "Test3", "TE3", new ArrayList<>());
        List<Team> teams = List.of(team1, team2, team3);

        //When
        List<TeamDto> mappedTeams = teamMapper.mapToTeamDtoList(teams);

        //Then
        assertAll(() -> assertEquals(3, mappedTeams.size()),
                () -> assertEquals(5L, mappedTeams.get(2).getId()),
                () -> assertEquals("Test3", mappedTeams.get(2).getName()),
                () -> assertEquals("TE3", mappedTeams.get(2).getCode()));
    }
}
