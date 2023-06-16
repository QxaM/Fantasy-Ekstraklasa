package com.kodilla.fantasy.controller;

import com.kodilla.fantasy.service.DataFetchingService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@SpringJUnitWebConfig
@WebMvcTest(ServiceController.class)
public class ServiceControllerTests {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private DataFetchingService dataInitializer;

    @Test
    void shouldInitTeams() throws Exception {
        //Given

        //When + Then
        mockMvc.perform(MockMvcRequestBuilders
                    .post("/fantasy/v1/service/init/teams")
                    .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isCreated());
        verify(dataInitializer, times(1)).fetchTeams();
    }

    @Test
    void shouldInitPlayers() throws Exception {
        //Given

        //When + Then
        mockMvc.perform(MockMvcRequestBuilders
                    .post("/fantasy/v1/service/init/players")
                    .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isCreated());
        verify(dataInitializer, times(1)).fetchPlayers();
    }

    @Test
    void shouldFetchScores() throws Exception {
        //Given

        //When + Then
        mockMvc.perform(MockMvcRequestBuilders
                    .put("/fantasy/v1/service/getScores/1")
                    .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isCreated());
        verify(dataInitializer, times(1)).addScores(1);
    }
}
