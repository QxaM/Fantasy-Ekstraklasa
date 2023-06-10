package com.kodilla.fantasy.service;

import com.kodilla.fantasy.domain.*;
import com.kodilla.fantasy.domain.exception.ElementNotFoundException;
import com.kodilla.fantasy.domain.exception.NotEnoughFundsException;
import com.kodilla.fantasy.domain.exception.PlayerAlreadyExistInSquadException;
import com.kodilla.fantasy.domain.exception.SquadAlreadyFullException;
import com.kodilla.fantasy.repository.SquadRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SquadDbServiceTests {

    @InjectMocks
    private SquadDbService squadDbService;
    @Mock
    private SquadRepository squadRepository;
    @Mock
    private PlayerDbService playerDbService;

    private Set<Player> buildFullSquad() {
        return IntStream.range(0,11)
                .mapToObj(i -> new Player())
                .collect(Collectors.toSet());
    }

    @Test
    void shouldGetSquads() {
        //Given
        Squad squad1 = new Squad(1L, "Squad 1", BigDecimal.ONE, new HashSet<>());
        Squad squad2 = new Squad(2L, "Squad 2", BigDecimal.ONE, new HashSet<>());
        List<Squad> squads = List.of(squad1, squad2);
        when(squadRepository.findAll()).thenReturn(squads);

        //When
        List<Squad> foundSquads = squadDbService.getSquads();

        //Then
        assertAll(() -> assertEquals(2, foundSquads.size()),
                () -> assertEquals("Squad 1", foundSquads.get(0).getName()),
                () -> assertEquals("Squad 2", foundSquads.get(1).getName()));
    }

    @Test
    void shouldGetSquad() {
        //Given
        Squad squad = new Squad(1L, "Squad 1", BigDecimal.ONE, new HashSet<>());
        when(squadRepository.findById(1L)).thenReturn(Optional.of(squad));

        //When
        Squad foundSquad = new Squad();
        try {
            foundSquad = squadDbService.getSquad(1L);
        } catch (ElementNotFoundException e) {}

        //Then
        assertEquals("Squad 1", foundSquad.getName());
    }

    @Test
    void shouldNotGetSquad() {
        //Given

        //When

        //Then
        assertThrows(ElementNotFoundException.class, () -> squadDbService.getSquad(1L));
    }

    @Test
    void shouldSaveSquad() {
        //Given
        Squad squad = new Squad(1L, "Squad 1", BigDecimal.ONE, new HashSet<>());
        when(squadRepository.save(any(Squad.class))).thenReturn(squad);

        //Then
        Squad savedUser = squadDbService.saveSquad(squad);

        //When
        assertEquals("Squad 1", savedUser.getName());
    }

    @Test
    void shouldAddPlayer() throws ElementNotFoundException {
        //Given
        Squad squad = new Squad(1L, "Squad 1", BigDecimal.ONE, new HashSet<>());
        Team team1 = new Team(1L, 2L, "Test", "TET", new ArrayList<>());
        Player player1 = new Player(1L, 2L, "Test", "Test", 21, BigDecimal.ONE, Position.ST, team1);
        team1.getPlayers().add(player1);

        Squad squadWithPlayer = new Squad(1L, "Squad 1", BigDecimal.ONE, new HashSet<>());
        squadWithPlayer.getPlayers().add(player1);

        when(squadRepository.findById(1L)).thenReturn(Optional.of(squad));
        when(playerDbService.getPlayer(1L)).thenReturn(player1);
        when(squadRepository.save(any(Squad.class))).thenReturn(squadWithPlayer);

        //When
        Squad savedSquad = new Squad();
        try {
            savedSquad = squadDbService.addPlayer(1L, 1L);
        } catch (SquadAlreadyFullException | NotEnoughFundsException | PlayerAlreadyExistInSquadException e) {}

        //Then
        assertEquals(1, savedSquad.getPlayers().size());
    }

    @Test
    void shouldNotAddPlayer() {
        //Given

        //When

        //Then
        assertThrows(ElementNotFoundException.class, () -> squadDbService.addPlayer(1L, 1L));
    }

    @Test
    void shouldBeFull() throws ElementNotFoundException {
        //Given
        Set<Player> players = buildFullSquad();
        Squad squad = new Squad(1L, "Squad 1", BigDecimal.ONE, players);
        Team team1 = new Team(1L, 2L, "Test", "TET", new ArrayList<>());
        Player player1 = new Player(1L, 2L, "Test", "Test", 21, BigDecimal.ONE, Position.ST, team1);
        team1.getPlayers().add(player1);

        when(squadRepository.findById(1L)).thenReturn(Optional.of(squad));
        when(playerDbService.getPlayer(1L)).thenReturn(player1);

        //When + Then
        assertThrows(SquadAlreadyFullException.class, () -> squadDbService.addPlayer(1L, 1L));
    }

    @Test
    void shouldNotEnoughFunds() throws ElementNotFoundException {
        //Given
        Squad squad = new Squad(1L, "Squad 1", BigDecimal.valueOf(30000000), new HashSet<>());
        Team team1 = new Team(1L, 2L, "Test", "TET", new ArrayList<>());
        Player player1 = new Player(1L, 2L, "Test", "Test", 21, BigDecimal.ONE, Position.ST, team1);
        team1.getPlayers().add(player1);

        when(squadRepository.findById(1L)).thenReturn(Optional.of(squad));
        when(playerDbService.getPlayer(1L)).thenReturn(player1);

        //When + Then
        assertThrows(NotEnoughFundsException.class, () -> squadDbService.addPlayer(1L, 1L));
    }

    @Test
    void shouldRemovePlayer() throws ElementNotFoundException {
        //Given
        Squad squad = new Squad(1L, "Squad 1", BigDecimal.ONE, new HashSet<>());
        Team team1 = new Team(1L, 2L, "Test", "TET", new ArrayList<>());
        Player player1 = new Player(1L, 2L, "Test", "Test", 21, BigDecimal.ONE, Position.ST, team1);
        team1.getPlayers().add(player1);
        squad.getPlayers().add(player1);

        Squad squadWithoutPlayer = new Squad(1L, "Squad 1", BigDecimal.ONE, new HashSet<>());

        when(squadRepository.findById(1L)).thenReturn(Optional.of(squad));
        when(playerDbService.getPlayer(1L)).thenReturn(player1);
        when(squadRepository.save(squad)).thenReturn(squadWithoutPlayer);

        //When
        Squad savedSquad = squadDbService.removePlayer(1L, 1L);

        //Then
        assertEquals(0, savedSquad.getPlayers().size());
    }

    @Test
    void shouldNotRemovePlayer() {
        //Given

        //When

        //Then
        assertThrows(ElementNotFoundException.class, () -> squadDbService.removePlayer(1L, 1L));
    }
}
