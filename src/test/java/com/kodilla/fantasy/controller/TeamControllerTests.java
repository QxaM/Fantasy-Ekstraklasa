package com.kodilla.fantasy.controller;

import com.google.gson.Gson;
import com.kodilla.fantasy.domain.Team;
import com.kodilla.fantasy.domain.dto.TeamDto;
import com.kodilla.fantasy.mapper.TeamMapper;
import com.kodilla.fantasy.service.TeamDbService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.when;

@SpringJUnitWebConfig
@WebMvcTest(TeamController.class)
public class TeamControllerTests {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private TeamDbService teamDbService;
    @MockBean
    private TeamMapper teamMapper;

    @Test
    void shouldFetchTeams() throws Exception {
        //Given
        Team team1 = new Team(3L, 4L, "Test1", "TE1", new ArrayList<>());
        Team team2 = new Team(4L, 5L, "Test2", "TE2", new ArrayList<>());
        Team team3 = new Team(5L, 6L, "Test3", "TE3", new ArrayList<>());
        List<Team> teams = List.of(team1, team2, team3);

        TeamDto teamDto1 = new TeamDto(3L, "Test1", "TE1");
        TeamDto teamDto2 = new TeamDto(4L, "Test2", "TE2");
        TeamDto teamDto3 = new TeamDto(5L, "Test3", "TE3");
        List<TeamDto> teamDtos = List.of(teamDto1, teamDto2, teamDto3);

        when(teamDbService.getTeams()).thenReturn(teams);
        when(teamMapper.mapToTeamDtoList(anyList())).thenReturn(teamDtos);

        //When + Then
        mockMvc.perform(MockMvcRequestBuilders
                    .get("/fantasy/v1/teams")
                    .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(3)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[2].id", Matchers.is(5)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[2].name", Matchers.is("Test3")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[2].code", Matchers.is("TE3")));
    }

    @Test
    void shouldFetchTeam() throws Exception {
        //Given
        Team team1 = new Team(3L, 4L, "Test1", "TE1", new ArrayList<>());
        TeamDto teamDto1 = new TeamDto(3L, "Test1", "TE1");

        when(teamDbService.getTeam(3L)).thenReturn(team1);
        when(teamMapper.mapToTeamDto(any(Team.class))).thenReturn(teamDto1);

        //When + Then
        mockMvc.perform(MockMvcRequestBuilders
                    .get("/fantasy/v1/teams/3")
                    .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", Matchers.is(3)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name", Matchers.is("Test1")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.code", Matchers.is("TE1")));
    }

    @Test
    void shouldUpdateTeam() throws Exception {
        //Given
        Team team1 = new Team(3L, 4L, "Test1", "TE1", new ArrayList<>());
        TeamDto teamDto1 = new TeamDto(3L, "Test1", "TE1");

        when(teamMapper.mapToTeam(any(TeamDto.class))).thenReturn(team1);
        when(teamDbService.updateTeam(any(Team.class))).thenReturn(team1);
        when(teamMapper.mapToTeamDto(any(Team.class))).thenReturn(teamDto1);

        Gson gson = new Gson();
        String jsonContent = gson.toJson(teamDto1);

        //When + Then
        mockMvc.perform(MockMvcRequestBuilders
                        .put("/fantasy/v1/teams")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonContent))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", Matchers.is(3)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name", Matchers.is("Test1")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.code", Matchers.is("TE1")));
    }

    @Test
    void shouldDeleteTeam() throws Exception {
        //Given

        //When + Then
        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/fantasy/v1/teams/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
}
