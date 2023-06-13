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
import java.util.Set;
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
                .mapToObj((i) -> new Player.PlayerBuilder()
                        .apiFootballId(Integer.toUnsignedLong(i))
                        .firstname("Test" + i)
                        .lastname("Test" + i)
                        .age(21)
                        .value(BigDecimal.valueOf(i))
                        .position(Position.GK)
                        .team(new Team())
                        .build())
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
        Player player = new Player.PlayerBuilder()
                .apiFootballId(6L)
                .firstname("Test firstname")
                .lastname("Test lastname")
                .age(21)
                .value(BigDecimal.ONE)
                .position(Position.ST)
                .team(team)
                .build();
        player.setId(4L);
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
    void testMapToPlayerSet() {
        //Given
        TeamDto teamDto = new TeamDto(3L, "Test", "TET");
        PlayerDto playerDto1 = new PlayerDto(4L, "Test firstname", "Test lastname", 21, BigDecimal.ONE, Position.ST, teamDto);
        PlayerDto playerDto2 = new PlayerDto(5L, "Test firstname", "Test lastname", 21, BigDecimal.ONE, Position.ST, teamDto);

        Team team = new Team(3L, 5L, "Test", "TET", new ArrayList<>());
        Player player = new Player.PlayerBuilder()
                .apiFootballId(6L)
                .firstname("Test firstname")
                .lastname("Test lastname")
                .age(21)
                .value(BigDecimal.ONE)
                .position(Position.ST)
                .team(team)
                .build();
        player.setId(4L);
        team.getPlayers().add(player);
        when(teamMapper.mapToTeam(teamDto)).thenReturn(team);

        //When
        Set<Player> mappedPlayer = playerMapper.mapToPlayerSet(List.of(playerDto1, playerDto2));

        //Then
        assertEquals(2, mappedPlayer.size());
    }

    @Test
    void testMapToPlayerDto() {
        //Given
        TeamDto teamDto = new TeamDto(3L, "Test", "TET");

        Team team = new Team(3L, 5L, "Test", "TET", new ArrayList<>());
        Player player = new Player.PlayerBuilder()
                .apiFootballId(6L)
                .firstname("Test firstname")
                .lastname("Test lastname")
                .age(21)
                .value(BigDecimal.ONE)
                .position(Position.ST)
                .team(team)
                .build();
        player.setId(4L);
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
        Player player1 = new Player.PlayerBuilder()
                .apiFootballId(6L)
                .firstname("Test firstname")
                .lastname("Test lastname")
                .age(21)
                .value(BigDecimal.ONE)
                .position(Position.ST)
                .team(team)
                .build();
        Player player2 = new Player.PlayerBuilder()
                .apiFootballId(7L)
                .firstname("Test firstname 2")
                .lastname("Test lastname 2")
                .age(22)
                .value(BigDecimal.valueOf(2))
                .position(Position.MID)
                .team(team)
                .build();
        Player player3 = new Player.PlayerBuilder()
                .apiFootballId(8L)
                .firstname("Test firstname 3")
                .lastname("Test lastname 3")
                .age(23)
                .value(BigDecimal.valueOf(3))
                .position(Position.GK)
                .team(team)
                .build();
        player1.setId(4L);
        player2.setId(5L);
        player3.setId(6L);
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
