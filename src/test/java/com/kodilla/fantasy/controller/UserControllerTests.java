package com.kodilla.fantasy.controller;

import com.fasterxml.jackson.databind.deser.DataFormatReaders;
import com.google.gson.Gson;
import com.kodilla.fantasy.domain.Squad;
import com.kodilla.fantasy.domain.User;
import com.kodilla.fantasy.domain.dto.CreateUserDto;
import com.kodilla.fantasy.domain.dto.SquadDto;
import com.kodilla.fantasy.domain.dto.UserDto;
import com.kodilla.fantasy.mapper.SquadMapper;
import com.kodilla.fantasy.mapper.UserMapper;
import com.kodilla.fantasy.service.UserDbService;
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
import java.util.HashSet;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringJUnitWebConfig
@WebMvcTest(UserController.class)
public class UserControllerTests {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private UserDbService userDbService;
    @MockBean
    private UserMapper userMapper;
    @MockBean
    private SquadMapper squadMapper;

    private User user;
    private UserDto userDto;

    @BeforeEach
    void createUser() {
        user = new User(1L, "User 1", new ArrayList<>(), new Squad());
        userDto = new UserDto(1L, "User 1", new SquadDto());
    }

    @Test
    void shouldFetchUser() throws Exception {
        //Given
        when(userDbService.getUser(1L)).thenReturn(user);
        when(userMapper.mapToUserDto(user)).thenReturn(userDto);

        //When + Then
        mockMvc.perform(MockMvcRequestBuilders
                    .get("/fantasy/v1/users/1")
                    .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", Matchers.is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.username", Matchers.is("User 1")));
    }

    @Test
    void shouldCreateUser() throws Exception {
        //Given
        when(userMapper.mapToUser(any(CreateUserDto.class))).thenReturn(user);
        when(userDbService.saveUser(any(User.class))).thenReturn(user);
        when(userMapper.mapToUserDto(user)).thenReturn(userDto);

        CreateUserDto createUserDto = new CreateUserDto("User 1");
        Gson gson = new Gson();
        String jsonContent = gson.toJson(createUserDto);

        //When + Then
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/fantasy/v1/users")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(jsonContent))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", Matchers.is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.username", Matchers.is("User 1")));
    }

    @Test
    void shouldUpdateUser() throws Exception {
        //Given
        User newUser = new User(1L, "User 2", new ArrayList<>(), new Squad());
        UserDto newUserDto = new UserDto(1L, "User 2", new SquadDto());
        when(userDbService.getUser(1L)).thenReturn(user);
        when(userDbService.saveUser(any(User.class))).thenReturn(newUser);
        when(userMapper.mapToUserDto(newUser)).thenReturn(newUserDto);

        UserDto sentUser = new UserDto(1L, "User 2", new SquadDto());
        Gson gson = new Gson();
        String jsonContent = gson.toJson(sentUser);

        //When + Then
        mockMvc.perform(MockMvcRequestBuilders
                        .put("/fantasy/v1/users")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(jsonContent))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", Matchers.is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.username", Matchers.is("User 2")));
    }

    @Test
    void shouldDeleteUser() throws Exception {
        //Given

        //When + Then
        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/fantasy/v1/users/1")
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void shouldCreateSquad() throws Exception {
        //Given
        SquadDto squadDto = new SquadDto(2L, "New Squad", BigDecimal.ZERO, new ArrayList<>());
        Squad squad = new Squad(2L, "New Squad", BigDecimal.ZERO, new HashSet<>());
        when(squadMapper.mapToSquad(squadDto)).thenReturn(squad);

        Gson gson = new Gson();
        String jsonContent = gson.toJson(squadDto);

        //When + Then
        mockMvc.perform(MockMvcRequestBuilders
                    .put("/fantasy/v1/users/1/createSquad/New Squad")
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .content(jsonContent))
                .andExpect(MockMvcResultMatchers.status().isCreated());
    }
}
