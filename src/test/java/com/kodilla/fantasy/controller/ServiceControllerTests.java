package com.kodilla.fantasy.controller;

import com.kodilla.fantasy.domain.Team;
import com.kodilla.fantasy.service.DataInitializer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringJUnitWebConfig
@WebMvcTest(ServiceController.class)
public class ServiceControllerTests {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private DataInitializer dataInitializer;

    @Test
    void shouldInitTeams() throws Exception {
        //Given

        //When + Then
        mockMvc.perform(MockMvcRequestBuilders
                    .post("/fantasy/v1/service/init/teams")
                    .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isCreated());
    }

    @Test
    void shouldInitPlayers() throws Exception {
        //Given

        //When + Then
        mockMvc.perform(MockMvcRequestBuilders
                    .post("/fantasy/v1/service/init/players")
                    .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isCreated());
    }
}