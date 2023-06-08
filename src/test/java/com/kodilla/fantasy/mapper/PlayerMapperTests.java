package com.kodilla.fantasy.mapper;

import com.kodilla.fantasy.domain.Player;
import com.kodilla.fantasy.domain.Position;
import com.kodilla.fantasy.domain.Team;
import com.kodilla.fantasy.domain.dto.PlayerDto;
import com.kodilla.fantasy.domain.dto.PlayersPagedDto;
import com.kodilla.fantasy.domain.dto.TeamDto;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@SpringBootTest
public class PlayerMapperTests {

    @Autowired
    private PlayerMapper playerMapper;
    @Mock
    private TeamMapper teamMapper;

    private Page<Player> createPlayersPage() {
        List<Player> players = IntStream.range(0, 20)
                .mapToObj((i) -> new Player(
                        Integer.toUnsignedLong(i),
                        Integer.toUnsignedLong(i),
                        "Test" + i,
                        "Test" + i, i,
                        BigDecimal.valueOf(i),
                        Position.GK,
                        new Team()))
                .toList();

        Pageable pageable = PageRequest.of(0, 9);

        return new PageImpl<>(players, pageable, 3);
    }

    @Test
    void testMapToPlayer() {
        //Given
        TeamDto teamDto = new TeamDto(3L, "Test", "TET");
        PlayerDto playerDto = new PlayerDto(4L, "Test firstname", "Test lastname", 21, BigDecimal.ONE, Position.ST, teamDto);

        Team team = new Team(3L, 5L, "Test", "TET", new ArrayList<>());
        Player player = new Player(4L, 6L, "Test firstname", "Test lastname", 21, BigDecimal.ONE, Position.ST, team);
        team.getPlayers().add(player);
        when(teamMapper.mapToTeam(teamDto)).thenReturn(team);

        //When
        Player mappedPlayer = playerMapper.mapToPlayer(playerDto);

        //Then
        assertAll(() -> assertEquals(4L, mappedPlayer.getId()),
                () -> assertEquals("Test firstname", mappedPlayer.getFirstname()),
                () -> assertEquals("Test lastname", mappedPlayer.getLastname()),
                () -> assertEquals(21, mappedPlayer.getAge()),
                () -> assertEquals(Position.ST, mappedPlayer.getPosition()),
                () -> assertEquals(3L, mappedPlayer.getTeam().getId()),
                () -> assertEquals("Test", mappedPlayer.getTeam().getName()),
                () -> assertEquals("TET", mappedPlayer.getTeam().getCode()));
    }

    @Test
    void testMapToPlayerDto() {
        //Given
        TeamDto teamDto = new TeamDto(3L, "Test", "TET");

        Team team = new Team(3L, 5L, "Test", "TET", new ArrayList<>());
        Player player = new Player(4L, 6L, "Test firstname", "Test lastname", 21, BigDecimal.ONE, Position.ST, team);
        team.getPlayers().add(player);
        when(teamMapper.mapToTeamDto(team)).thenReturn(teamDto);

        //Then
        PlayerDto mappedPlayer = playerMapper.mapToPlayerDto(player);

        //When
        assertAll(() -> assertEquals(4L, mappedPlayer.getId()),
                () -> assertEquals("Test firstname", mappedPlayer.getFirstname()),
                () -> assertEquals("Test lastname", mappedPlayer.getLastname()),
                () -> assertEquals(21, mappedPlayer.getAge()),
                () -> assertEquals(BigDecimal.ONE, mappedPlayer.getValue()),
                () -> assertEquals(3L, mappedPlayer.getTeam().getId()),
                () -> assertEquals("Test", mappedPlayer.getTeam().getName()),
                () -> assertEquals("TET", mappedPlayer.getTeam().getCode()));
    }

    @Test
    void testMapToPlayerDtoList() {
        //Given
        TeamDto teamDto = new TeamDto(3L, "Test", "TET");

        Team team = new Team(3L, 5L, "Test", "TET", new ArrayList<>());
        Player player1 = new Player(4L, 6L, "Test firstname 1", "Test lastname 1", 21, BigDecimal.ONE, Position.ST, team);
        Player player2 = new Player(5L, 7L, "Test firstname 2", "Test lastname 2", 22, BigDecimal.valueOf(2), Position.MID, team);
        Player player3 = new Player(6L, 8L, "Test firstname 3", "Test lastname 3", 23, BigDecimal.valueOf(3), Position.GK, team);
        team.getPlayers().add(player1);
        team.getPlayers().add(player2);
        team.getPlayers().add(player3);
        when(teamMapper.mapToTeamDto(team)).thenReturn(teamDto);

        List<Player> players = List.of(player1, player2, player3);

        //When
        List<PlayerDto> mappedPlayers = playerMapper.mapToPlayerDtoList(players);

        //Then
        assertAll(() -> assertEquals(3, mappedPlayers.size()),
                () -> assertEquals(6L, mappedPlayers.get(2).getId()),
                () -> assertEquals("Test firstname 3", mappedPlayers.get(2).getFirstname()),
                () -> assertEquals("Test lastname 3", mappedPlayers.get(2).getLastname()),
                () -> assertEquals(23, mappedPlayers.get(2).getAge()),
                () -> assertEquals(BigDecimal.valueOf(3), mappedPlayers.get(2).getValue()),
                () -> assertEquals(Position.GK, mappedPlayers.get(2).getPosition()));
    }

    @Test
    void testMapToPlayersPagedDto() {
        //Given
        Page<Player> players = createPlayersPage();

        //Then
        PlayersPagedDto mappedPlayers = playerMapper.mapToPlayersPagedDto(players);

        //When
        assertAll(() -> assertEquals(0, mappedPlayers.getPage().getCurrentPage()),
                () -> assertEquals(3, mappedPlayers.getPage().getFinalPage()),
                () -> assertEquals(20, mappedPlayers.getPlayer().size()));
    }
}
