package com.kodilla.fantasy.controller;

import com.google.gson.Gson;
import com.kodilla.fantasy.domain.League;
import com.kodilla.fantasy.domain.dto.LeagueDto;
import com.kodilla.fantasy.mapper.LeagueMapper;
import com.kodilla.fantasy.service.LeagueDbService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringJUnitWebConfig
@WebMvcTest(LeagueController.class)
public class LeagueControllerTests {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private LeagueDbService leagueDbService;
    @MockBean
    private LeagueMapper leagueMapper;

    private League league;
    private LeagueDto leagueDto;

    @BeforeEach
    void createLeague() {
        league = new League(1L, "League 1", new ArrayList<>());
        leagueDto = new LeagueDto(1L, "League 1", new ArrayList<>());
    }

    @Test
    void shouldFetchLeague() throws Exception {
        //Given
        when(leagueDbService.getLeague(1L)).thenReturn(league);
        when(leagueMapper.mapToLeagueDto(league)).thenReturn(leagueDto);

        //When + Then
        mockMvc.perform(MockMvcRequestBuilders
                    .get("/fantasy/v1/leagues/1")
                    .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", Matchers.is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name", Matchers.is("League 1")));
    }

    @Test
    void shouldCreateLeague() throws Exception {
        //Given
        when(leagueMapper.mapToLeague(any(LeagueDto.class))).thenReturn(league);
        when(leagueDbService.saveLeague(any(League.class))).thenReturn(league);
        when(leagueMapper.mapToLeagueDto(league)).thenReturn(leagueDto);

        //When + Then
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/fantasy/v1/leagues/League 1")
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", Matchers.is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name", Matchers.is("League 1")));
    }

    @Test
    void shouldUpdateLeague() throws Exception {
        //Given
        League newLeague = new League(1L, "League updated", new ArrayList<>());
        LeagueDto newLeagueDto = new LeagueDto(1L, "League updated", new ArrayList<>());

        when(leagueDbService.getLeague(1L)).thenReturn(newLeague);
        when(leagueDbService.saveLeague(any(League.class))).thenReturn(newLeague);
        when(leagueMapper.mapToLeagueDto(newLeague)).thenReturn(newLeagueDto);

        Gson gson = new Gson();
        String jsonContent = gson.toJson(newLeagueDto);

        //When + Then
        mockMvc.perform(MockMvcRequestBuilders
                        .put("/fantasy/v1/leagues")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(jsonContent))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", Matchers.is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name", Matchers.is("League updated")));
    }

    @Test
    void shouldDeleteLeague() throws Exception {
        //Given

        //When + Then
        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/fantasy/v1/leagues/1")
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void shouldAddUser() throws Exception {
        //Given

        //When + Then
        mockMvc.perform(MockMvcRequestBuilders
                        .put("/fantasy/v1/leagues/1/addUser/1")
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isCreated());
    }

    @Test
    void shouldRemoveUser() throws Exception {
        //Given

        //When + Then
        mockMvc.perform(MockMvcRequestBuilders
                        .put("/fantasy/v1/leagues/1/removeUser/1")
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
}
