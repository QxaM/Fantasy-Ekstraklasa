package com.kodilla.fantasy.mapper;

import com.kodilla.fantasy.domain.League;
import com.kodilla.fantasy.domain.Squad;
import com.kodilla.fantasy.domain.User;
import com.kodilla.fantasy.domain.dto.LeagueDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class LeagueMapperTests {

    @Autowired
    private LeagueMapper leagueMapper;

    @Test
    void testMapToLeague() {
        //Given
        LeagueDto leagueDto = new LeagueDto(1L, "Test League", new ArrayList<>());

        //Then
        League mappedLeague = leagueMapper.mapToLeague(leagueDto);

        //Given
        assertAll(() -> assertEquals(1L, mappedLeague.getId()),
                () -> assertEquals("Test League", mappedLeague.getName()));
    }

    @Test
    void testMapToLeagueDto() {
        //Given
        League league = new League(1L, "Test League", new ArrayList<>());
        Squad squad = new Squad(3L, "Squad 1", BigDecimal.ONE, new HashSet<>());
        User user = new User(2L, "User 1", new ArrayList<>(), squad);
        league.getUsers().add(user);

        //Then
        LeagueDto mappedLeague = leagueMapper.mapToLeagueDto(league);

        //Given
        assertAll(() -> assertEquals(1L, mappedLeague.getId()),
                () -> assertEquals("Test League", mappedLeague.getName()),
                () -> assertEquals(1, mappedLeague.getUsers().size()),
                () -> assertEquals(2L, mappedLeague.getUsers().get(0).getId()),
                () -> assertEquals("User 1", mappedLeague.getUsers().get(0).getUsername()));
    }
}
