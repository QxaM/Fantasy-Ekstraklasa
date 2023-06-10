package com.kodilla.fantasy.mapper;

import com.kodilla.fantasy.domain.Player;
import com.kodilla.fantasy.domain.Position;
import com.kodilla.fantasy.domain.Squad;
import com.kodilla.fantasy.domain.Team;
import com.kodilla.fantasy.domain.dto.SquadDto;
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
    void testMapToSquadDto() {
        //Given
        Squad squad = new Squad(1L, "Squad 1", BigDecimal.valueOf(30000000), new HashSet<>());
        Team team1 = new Team(1L, 2L, "Test", "TET", new ArrayList<>());
        Player player1 = new Player(1L, 2L, "Test", "Test", 21, BigDecimal.ONE, Position.ST, team1);
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
