package com.kodilla.fantasy.mapper;

import com.kodilla.fantasy.domain.Player;
import com.kodilla.fantasy.domain.Position;
import com.kodilla.fantasy.domain.Squad;
import com.kodilla.fantasy.domain.Team;
import com.kodilla.fantasy.domain.dto.PlayerDto;
import com.kodilla.fantasy.domain.dto.SquadDto;
import com.kodilla.fantasy.domain.dto.TeamDto;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class SquadMapperTests {

    @Autowired
    private SquadMapper squadMapper;

    @Test
    void testMapToSquad() {
        //Given
        SquadDto squadDto = new SquadDto(1L, "Squad 1", BigDecimal.valueOf(30000000), new ArrayList<>());
        TeamDto teamDto1 = new TeamDto(1L, "Test", "TET");
        PlayerDto playerDto1 = new PlayerDto(1L, "Test", "Test", 21, BigDecimal.ONE, Position.ST, teamDto1, 1);
        squadDto.getPlayers().add(playerDto1);

        //When
        Squad mappedSquad = squadMapper.mapToSquad(squadDto);

        //Then
        assertAll(() -> assertEquals(1L, mappedSquad.getId()),
                () -> assertEquals("Squad 1", mappedSquad.getName()),
                () -> assertEquals(BigDecimal.valueOf(30000000), mappedSquad.getCurrentValue()),
                () -> assertEquals(1, squadDto.getPlayers().size()));
    }

    @Test
    void testMapToSquadDto() {
        //Given
        Squad squad = new Squad(1L, "Squad 1", BigDecimal.valueOf(30000000), new HashSet<>());
        Team team1 = new Team(1L, 2L, "Test", "TET", new ArrayList<>());
        Player player1 = new Player.PlayerBuilder()
                .apiFootballId(2L)
                .firstname("Test")
                .lastname("Test")
                .age(21)
                .value(BigDecimal.ONE)
                .position(Position.ST)
                .team(team1)
                .build();
        team1.getPlayers().add(player1);
        squad.getPlayers().add(player1);

        //When
        SquadDto squadDto = squadMapper.mapToSquadDto(squad);

        //Then
        assertAll(() -> assertEquals(1L, squadDto.getId()),
                () -> assertEquals("Squad 1", squadDto.getName()),
                () -> assertEquals(BigDecimal.valueOf(30000000), squadDto.getCurrentValue()),
                () -> assertEquals("Test", squadDto.getPlayers().get(0).getFirstname()),
                () -> assertEquals("TET", squadDto.getPlayers().get(0).getTeam().getCode()));
    }
}
