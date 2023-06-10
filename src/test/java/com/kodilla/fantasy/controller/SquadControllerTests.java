package com.kodilla.fantasy.controller;

import com.google.gson.Gson;
import com.kodilla.fantasy.domain.Player;
import com.kodilla.fantasy.domain.Position;
import com.kodilla.fantasy.domain.Squad;
import com.kodilla.fantasy.domain.Team;
import com.kodilla.fantasy.domain.dto.PlayerDto;
import com.kodilla.fantasy.domain.dto.SquadDto;
import com.kodilla.fantasy.domain.dto.TeamDto;
import com.kodilla.fantasy.mapper.SquadMapper;
import com.kodilla.fantasy.service.SquadDbService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.math.BigDecimal;
import java.util.ArrayList;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringJUnitWebConfig
@WebMvcTest(SquadController.class)
public class SquadControllerTests {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private SquadDbService squadDbService;
    @MockBean
    private SquadMapper squadMapper;

    private Squad squad;
    private SquadDto squadDto;

    @BeforeEach
    void buildSquad() {
        squad = new Squad(1L, "Squad 1", BigDecimal.ONE, new ArrayList<>());
        Team team1 = new Team(2L, 4L, "Test", "TET", new ArrayList<>());
        Player player1 = new Player(3L, 5L, "Test firstname", "Test lastname", 21, BigDecimal.ONE, Position.ST, team1);
        team1.getPlayers().add(player1);
        squad.getPlayers().add(player1);
    }

    @BeforeEach
    void buildSquadDto() {
        squadDto = new SquadDto(1L, "Squad 1", BigDecimal.ONE, new ArrayList<>());
        TeamDto teamDto = new TeamDto(2L, "Test", "TET");
        PlayerDto playerDto = new PlayerDto(3L, "Test firstname", "Test lastname", 21, BigDecimal.ONE, Position.ST, teamDto);
        squadDto.getPlayers().add(playerDto);
    }

    @Test
    void shouldFetchSquad() throws Exception {
        //Given
        when(squadDbService.getSquad(1L)).thenReturn(squad);
        when(squadMapper.mapToSquadDto(squad)).thenReturn(squadDto);

        //When + Then
        mockMvc.perform(MockMvcRequestBuilders
                    .get("/fantasy/v1/squads/1")
                    .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", Matchers.is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name", Matchers.is("Squad 1")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.currentValue", Matchers.is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.players[0].id", Matchers.is(3)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.players[0].team.id", Matchers.is(2)));
    }

    @Test
    void shouldUpdateSquad() throws Exception {
        //Given
        Squad newSquad = new Squad(1L, "Squad updated", BigDecimal.ZERO, new ArrayList<>());
        Team team1 = new Team(2L, 4L, "Test", "TET", new ArrayList<>());
        Player player1 = new Player(3L, 5L, "Test", "Test lastname", 21, BigDecimal.ONE, Position.ST, team1);
        team1.getPlayers().add(player1);
        newSquad.getPlayers().add(player1);

        SquadDto newSquadDto = new SquadDto(1L, "Squad updated", BigDecimal.ZERO, new ArrayList<>());
        TeamDto teamDto = new TeamDto(2L, "Test", "TET");
        PlayerDto playerDto = new PlayerDto(3L, "Test firstname", "Test lastname", 21, BigDecimal.ONE, Position.ST, teamDto);
        newSquadDto.getPlayers().add(playerDto);

        when(squadDbService.getSquad(1L)).thenReturn(squad);
        when(squadDbService.saveSquad(any(Squad.class))).thenReturn(newSquad);
        when(squadMapper.mapToSquadDto(newSquad)).thenReturn(newSquadDto);

        Gson gson = new Gson();
        String jsonContent = gson.toJson(newSquadDto);

        //When + Then
        mockMvc.perform(MockMvcRequestBuilders
                        .put("/fantasy/v1/squads")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(jsonContent))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", Matchers.is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name", Matchers.is("Squad updated")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.currentValue", Matchers.is(0)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.players[0].id", Matchers.is(3)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.players[0].team.id", Matchers.is(2)));
    }

    @Test
    void shouldDeleteSquad() throws Exception {
        //Given

        //When + Then
        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/fantasy/v1/squads/1")
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void shouldAddSquad() throws Exception {
        //Given
        when(squadDbService.addPlayer(1L, 3L)).thenReturn(squad);
        when(squadMapper.mapToSquadDto(squad)).thenReturn(squadDto);

        //When + Then
        mockMvc.perform(MockMvcRequestBuilders
                        .put("/fantasy/v1/squads/1/addPlayer/3")
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", Matchers.is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name", Matchers.is("Squad 1")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.currentValue", Matchers.is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.players[0].id", Matchers.is(3)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.players[0].team.id", Matchers.is(2)));
    }

    @Test
    void shouldDeletePlayer() throws Exception {
        //Given
        when(squadDbService.removePlayer(1L, 3L)).thenReturn(squad);
        when(squadMapper.mapToSquadDto(squad)).thenReturn(squadDto);

        //When + Then
        mockMvc.perform(MockMvcRequestBuilders
                        .put("/fantasy/v1/squads/1/removePlayer/3")
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", Matchers.is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name", Matchers.is("Squad 1")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.currentValue", Matchers.is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.players[0].id", Matchers.is(3)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.players[0].team.id", Matchers.is(2)));
    }
}
