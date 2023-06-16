package com.kodilla.fantasy.service;

import com.kodilla.fantasy.domain.Player;
import com.kodilla.fantasy.domain.Position;
import com.kodilla.fantasy.domain.SortType;
import com.kodilla.fantasy.domain.Team;
import com.kodilla.fantasy.domain.exception.ElementNotFoundException;
import com.kodilla.fantasy.repository.PlayerRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PlayerDbServiceTests {

    @InjectMocks
    private PlayerDbService playerDbService;
    @Mock
    PlayerRepository playerRepository;

    private Page<Player> createPlayersPage() {
        List<Player> players = IntStream.range(0, 20)
                .mapToObj((i) -> new Player.PlayerBuilder()
                        .apiFootballId(Integer.toUnsignedLong(i))
                        .firstname("Test" + i)
                        .lastname("Test" + i)
                        .age(i)
                        .value(BigDecimal.valueOf(i))
                        .position(Position.GK)
                        .team(new Team())
                        .build())
                .toList();
        return new PageImpl<>(players);
    }

    @Test
    void shouldInitPlayers() {
        //Given
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
        Player player2 = new Player.PlayerBuilder()
                .apiFootballId(3L)
                .firstname("Test2")
                .lastname("Test2")
                .age(22)
                .value(BigDecimal.ONE)
                .position(Position.ST)
                .team(team1)
                .build();
        team1.getPlayers().add(player1);
        team1.getPlayers().add(player2);

        //When
        playerDbService.initPlayers(List.of(player1, player2));

        //Then
        verify(playerRepository, times(1)).deleteAll();
        verify(playerRepository, times(1)).saveAll(anyList());
    }

    @Test
    void shouldGetPlayers() {
        //Given
        Page<Player> players = createPlayersPage();
        Pageable pageWith20Elements = PageRequest.of(2, 20, Sort.by("id").ascending());
        when(playerRepository.findAll(pageWith20Elements)).thenReturn(players);

        //Then
        Page<Player> foundPlayers = playerDbService.getPlayers(2, SortType.ID_ASCENDING);

        //When
        assertAll(() -> assertEquals(20L, foundPlayers.getNumberOfElements()),
                () -> assertEquals(19, players.getContent().get(19).getAge()));
    }

    @Test
    void shouldGetPlayer() {
        //Given
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
        player1.setId(1L);
        team1.getPlayers().add(player1);
        when(playerRepository.findById(1L)).thenReturn(Optional.of(player1));

        //When
        Player foundPlayer = new Player();
        try {
            foundPlayer = playerDbService.getPlayer(1L);
        } catch (ElementNotFoundException e) {}

        //Then
        assertEquals(21, foundPlayer.getAge());
    }

    @Test
    void shouldNotGetPlayer() {
        //Given

        //when

        //Then
        assertThrows(ElementNotFoundException.class, () -> playerDbService.getPlayer(1L));
    }

    @Test
    void shouldGetPlayersByTeamId() {
        //Given
        Team team = new Team(1L, 1L, "Team 1", "TE1", new ArrayList<>());
        Player player1 = new Player.PlayerBuilder()
                .firstname("Firstname 1")
                .lastname("Lastname 1")
                .team(team)
                .build();
        Player player2 = new Player.PlayerBuilder()
                .firstname("Firstname 2")
                .lastname("Lastname 2")
                .team(team)
                .build();

        when(playerRepository.findPlayersByTeamId(1L)).thenReturn(List.of(player1, player2));

        //When
        List<Player> foundPlayers = playerDbService.getPlayersByTeamId(1L);

        //Then
        assertAll(() -> assertEquals("Firstname 2", foundPlayers.get(1).getFirstname()),
                () -> assertEquals("Lastname 2", foundPlayers.get(1).getLastname()));
    }

    @Test
    void shouldUpdatePlayer() {
        //Given
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
        Player changedPlayer = new Player.PlayerBuilder()
                .apiFootballId(3L)
                .firstname("Test changed")
                .lastname("Test")
                .age(21)
                .value(BigDecimal.ONE)
                .position(Position.ST)
                .team(team1)
                .build();
        player1.setId(1L);
        changedPlayer.setId(1L);
        when(playerRepository.findById(1L)).thenReturn(Optional.of(player1));
        when(playerRepository.save(any(Player.class))).thenReturn(changedPlayer);

        //When
        Player updatedPlayer = new Player();
        try {
            updatedPlayer = playerDbService.updatePlayer(changedPlayer);
        } catch (ElementNotFoundException e) {}

        //Then
        assertEquals("Test changed", updatedPlayer.getFirstname());
    }

    @Test
    void shouldSaveAllPlayers() {
        //Given
        Team team = new Team(1L, 1L, "Team 1", "TE1", new ArrayList<>());
        Player player1 = new Player.PlayerBuilder()
                .firstname("Firstname 1")
                .lastname("Lastname 1")
                .team(team)
                .build();
        Player player2 = new Player.PlayerBuilder()
                .firstname("Firstname 2")
                .lastname("Lastname 2")
                .team(team)
                .build();
        List<Player> players = List.of(player1, player2);

        //When
        playerDbService.saveAllPlayers(players);

        //Then
        verify(playerRepository, times(1)).saveAll(players);
    }

    @Test
    void shouldDeletePlayer() {
        //Given

        //When
        playerDbService.deletePlayer(1L);

        //Then
        verify(playerRepository, times(1)).deleteById(1L);
    }
}
