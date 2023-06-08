package com.kodilla.fantasy.controller;

import com.google.gson.Gson;
import com.kodilla.fantasy.domain.Player;
import com.kodilla.fantasy.domain.Position;
import com.kodilla.fantasy.domain.Team;
import com.kodilla.fantasy.domain.dto.PageDto;
import com.kodilla.fantasy.domain.dto.PlayerDto;
import com.kodilla.fantasy.domain.dto.PlayersPagedDto;
import com.kodilla.fantasy.domain.dto.TeamDto;
import com.kodilla.fantasy.mapper.PlayerMapper;
import com.kodilla.fantasy.service.PlayerDbService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringJUnitWebConfig
@WebMvcTest(PlayerController.class)
public class PlayerControllerTests {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private PlayerDbService playerDbService;
    @MockBean
    private PlayerMapper playerMapper;

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

    private List<PlayerDto> createPlayersDtoList() {
        return IntStream.range(0, 20)
                .mapToObj((i) -> new PlayerDto(
                        Integer.toUnsignedLong(i),
                        "Test" + i,
                        "Test" + i,
                        i,
                        BigDecimal.valueOf(i),
                        Position.GK,
                        new TeamDto()))
                .toList();
    }

    @Test
    void shouldFetchPlayers() throws Exception {
        //Given
        Page<Player> players = createPlayersPage();

        PageDto pageDto = new PageDto(0, 3);
        PlayersPagedDto mappedPlayers = new PlayersPagedDto(
                pageDto,
                createPlayersDtoList()
        );
        when(playerDbService.getPlayers(0)).thenReturn(players);
        when(playerMapper.mapToPlayersPagedDto(any())).thenReturn(mappedPlayers);

        //When + Then
        mockMvc.perform(MockMvcRequestBuilders
                    .get("/fantasy/v1/players/page/0")
                    .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.page.currentPage", Matchers.is(0)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.page.finalPage", Matchers.is(3)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.player", Matchers.hasSize(20)));
    }

    @Test
    void shouldFetchPlayer() throws Exception {
        //Given
        TeamDto teamDto = new TeamDto(3L, "Test", "TET");
        PlayerDto playerDto = new PlayerDto(4L, "Test firstname", "Test lastname", 21, BigDecimal.ONE, Position.ST, teamDto);

        Team team = new Team(3L, 5L, "Test", "TET", new ArrayList<>());
        Player player = new Player(4L, 6L, "Test firstname", "Test lastname", 21, BigDecimal.ONE, Position.ST, team);
        team.getPlayers().add(player);

        when(playerDbService.getPlayer(3L)).thenReturn(player);
        when(playerMapper.mapToPlayerDto(player)).thenReturn(playerDto);

        //When + Then
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/fantasy/v1/players/3")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", Matchers.is(4)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.firstname", Matchers.is("Test firstname")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.lastname", Matchers.is("Test lastname")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.age", Matchers.is(21)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.value", Matchers.is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.position", Matchers.is("ST")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.team.id", Matchers.is(3)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.team.name", Matchers.is("Test")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.team.code", Matchers.is("TET")));
    }

    @Test
    void shouldUpdatePlayer() throws Exception {
        //Given
        TeamDto teamDto = new TeamDto(3L, "Test", "TET");
        PlayerDto playerDto = new PlayerDto(4L, "Test firstname", "Test lastname", 21, BigDecimal.ONE, Position.ST, teamDto);

        Team team = new Team(3L, 5L, "Test", "TET", new ArrayList<>());
        Player player = new Player(4L, 6L, "Test firstname", "Test lastname", 21, BigDecimal.ONE, Position.ST, team);
        team.getPlayers().add(player);

        when(playerMapper.mapToPlayer(any(PlayerDto.class))).thenReturn(player);
        when(playerDbService.updatePlayer(any(Player.class))).thenReturn(player);
        when(playerMapper.mapToPlayerDto(any(Player.class))).thenReturn(playerDto);

        Gson gson = new Gson();
        String jsonContent = gson.toJson(playerDto);

        //When + Then
        mockMvc.perform(MockMvcRequestBuilders
                        .put("/fantasy/v1/players")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonContent))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", Matchers.is(4)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.firstname", Matchers.is("Test firstname")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.lastname", Matchers.is("Test lastname")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.age", Matchers.is(21)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.value", Matchers.is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.position", Matchers.is("ST")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.team.id", Matchers.is(3)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.team.name", Matchers.is("Test")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.team.code", Matchers.is("TET")));
    }

    @Test
    void shouldDeletePlayer() throws Exception {
        //Given

        //When + Then
        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/fantasy/v1/players/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
}
