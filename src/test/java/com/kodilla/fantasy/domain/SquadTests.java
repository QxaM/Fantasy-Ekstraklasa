package com.kodilla.fantasy.domain;

import com.kodilla.fantasy.domain.exception.NotEnoughFundsException;
import com.kodilla.fantasy.domain.exception.SquadAlreadyFullException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

public class SquadTests {

    private List<Player> buildFullSquad() {
        return IntStream.range(0,11)
                .mapToObj(i -> new Player())
                .collect(Collectors.toList());
    }

    @Test
    void testAddPlayer() {
        //Given
        Squad squad = new Squad("Squad 1", new ArrayList<>());
        Team team1 = new Team(1L, 2L, "Test", "TET", new ArrayList<>());
        Player player1 = new Player(1L, 2L, "Test", "Test", 21, BigDecimal.ONE, Position.ST, team1);
        team1.getPlayers().add(player1);

        //When + Then
        assertDoesNotThrow(() -> squad.addPlayer(player1));
        assertEquals(1, squad.getPlayers().size());
        assertEquals("Test", squad.getPlayers().get(0).getFirstname());
        assertEquals(BigDecimal.ONE, squad.getCurrentValue());
    }

    @Test
    void testAddPlayer_AlreadyFull() {
        //Given
        List<Player> players = buildFullSquad();
        Squad squad = new Squad("Squad 1", players);

        Team team1 = new Team(1L, 2L, "Test", "TET", new ArrayList<>());
        Player player1 = new Player(1L, 2L, "Test", "Test", 21, BigDecimal.ONE, Position.ST, team1);
        team1.getPlayers().add(player1);

        //When + Then
        assertThrows(SquadAlreadyFullException.class, () -> squad.addPlayer(player1));
    }

    @Test
    void testAddPlayer_NotEnoughFunds() {
        //Given
        Squad squad = new Squad(1L, "Squad 1", BigDecimal.valueOf(30000000), new ArrayList<>());

        Team team1 = new Team(1L, 2L, "Test", "TET", new ArrayList<>());
        Player player1 = new Player(1L, 2L, "Test", "Test", 21, BigDecimal.ONE, Position.ST, team1);
        team1.getPlayers().add(player1);

        //When + Then
        assertThrows(NotEnoughFundsException.class, () -> squad.addPlayer(player1));
    }

    @Test
    void testRemovePlayer() {
        //Given
        Team team1 = new Team(1L, 2L, "Test", "TET", new ArrayList<>());
        Player player1 = new Player(1L, 2L, "Test", "Test", 21, BigDecimal.ONE, Position.ST, team1);
        team1.getPlayers().add(player1);
        Squad squad = new Squad(1L, "Squad 1", BigDecimal.valueOf(30000000), new ArrayList<>());
        squad.getPlayers().add(player1);

        //When
        squad.removePlayer(player1);

        //Then
        assertEquals(0, squad.getPlayers().size());
        assertEquals(BigDecimal.valueOf(29999999), squad.getCurrentValue());

    }
}
