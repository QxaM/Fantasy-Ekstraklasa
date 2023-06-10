package com.kodilla.fantasy.mapper;

import com.kodilla.fantasy.domain.Squad;
import com.kodilla.fantasy.domain.User;
import com.kodilla.fantasy.domain.dto.CreateUserDto;
import com.kodilla.fantasy.domain.dto.SquadDto;
import com.kodilla.fantasy.domain.dto.UserDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class UserMapperTests {

    @Autowired
    private UserMapper userMapper;

    @Test
    void testMapToUser() {
        //Given
        UserDto userDto = new UserDto(1L, "User 1", new SquadDto());

        //When
        User mappedUser = userMapper.mapToUser(userDto);

        //Then
        assertAll(() -> assertEquals(1L, mappedUser.getId()),
                () -> assertEquals("User 1", mappedUser.getUsername()),
                () -> assertNotNull(mappedUser.getSquad()),
                () -> assertNotNull(mappedUser.getLeagues()));
    }

    @Test
    void testMapToUser_CreatedUser() {
        //Given
        CreateUserDto createUserDto = new CreateUserDto("User 1");

        //When
        User mappedUser = userMapper.mapToUser(createUserDto);

        //Then
        assertAll(() -> assertEquals("User 1", mappedUser.getUsername()),
                () -> assertNotNull(mappedUser.getSquad()),
                () -> assertNotNull(mappedUser.getLeagues()));
    }

    @Test
    void testMapToUserDto() {
        //Given
        Squad squad = new Squad(2L, "Squad 1", BigDecimal.ONE, new HashSet<>());
        User user = new User(1L, "User 1", new ArrayList<>(), squad);

        //When
        UserDto mappedUserDto = userMapper.mapToUserDto(user);

        //Then
        assertAll(() -> assertEquals(1L, mappedUserDto.getId()),
                () -> assertEquals("User 1", mappedUserDto.getUsername()),
                () -> assertEquals(2L, mappedUserDto.getSquad().getId()),
                () -> assertEquals("Squad 1", mappedUserDto.getSquad().getName()),
                () -> assertEquals(BigDecimal.ONE, mappedUserDto.getSquad().getCurrentValue()));
    }
}
