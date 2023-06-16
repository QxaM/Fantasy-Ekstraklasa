package com.kodilla.fantasy.mapper;

import com.kodilla.fantasy.domain.Squad;
import com.kodilla.fantasy.domain.User;
import com.kodilla.fantasy.domain.dto.CreateUserDto;
import com.kodilla.fantasy.domain.dto.SquadDto;
import com.kodilla.fantasy.domain.dto.UserDto;
import com.kodilla.fantasy.domain.dto.UserInLeagueDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class UserMapperTests {

    @Autowired
    private UserMapper userMapper;

    @Test
    void testMapToUser() {
        //Given
        UserDto userDto = new UserDto(1L, "User 1", "user@test.com", new SquadDto(), 0);

        //When
        User mappedUser = userMapper.mapToUser(userDto);

        //Then
        assertAll(() -> assertEquals(1L, mappedUser.getId()),
                () -> assertEquals("User 1", mappedUser.getUsername()),
                () -> assertEquals("user@test.com", mappedUser.getEmail()),
                () -> assertNotNull(mappedUser.getSquad()),
                () -> assertNotNull(mappedUser.getLeagues()));
    }

    @Test
    void testMapToUser_CreatedUser() {
        //Given
        CreateUserDto createUserDto = new CreateUserDto("User 1", "user@test.com");

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
        User user = new User(1L, "User 1", "user@test.com", new ArrayList<>(), squad, 0);

        //When
        UserDto mappedUserDto = userMapper.mapToUserDto(user);

        //Then
        assertAll(() -> assertEquals(1L, mappedUserDto.getId()),
                () -> assertEquals("User 1", mappedUserDto.getUsername()),
                () -> assertEquals("user@test.com", mappedUserDto.getEmail()),
                () -> assertEquals(2L, mappedUserDto.getSquad().getId()),
                () -> assertEquals("Squad 1", mappedUserDto.getSquad().getName()),
                () -> assertEquals(BigDecimal.ONE, mappedUserDto.getSquad().getCurrentValue()));
    }

    @Test
    void testMapToUserInLeagueDto() {
        //Given
        Squad squad = new Squad(2L, "Squad 1", BigDecimal.ONE, new HashSet<>());
        User user = new User(1L, "User 1", "user@test.com", new ArrayList<>(), squad, 1);

        //When
        UserInLeagueDto userInLeague = userMapper.mapToUserInLeagueDto(user);

        //Then
        assertAll(() -> assertEquals(1L, userInLeague.getId()),
                ()-> assertEquals("User 1", userInLeague.getUsername()),
                () -> assertEquals("Squad 1", userInLeague.getSquadName()),
                () -> assertEquals(1, userInLeague.getPoints()));

    }

    @Test
    void testMapToUserInLeagueDtoList() {
        //Given
        Squad squad1 = new Squad(2L, "Squad 1", BigDecimal.ONE, new HashSet<>());
        User user1 = new User(1L, "User 1", "user1@test.com", new ArrayList<>(), squad1, 1);
        Squad squad2 = new Squad(4L, "Squad 2", BigDecimal.ONE, new HashSet<>());
        User user2 = new User(3L, "User 2", "user2@test.com", new ArrayList<>(), squad2, 2);

        //When
        List<UserInLeagueDto> usersInLeague = userMapper.mapToUserInLeagueDtoList(List.of(user1, user2));

        //Then
        assertAll(() -> assertEquals(2, usersInLeague.size()),
                () -> assertEquals(3L, usersInLeague.get(1).getId()),
                ()-> assertEquals("User 2", usersInLeague.get(1).getUsername()),
                () -> assertEquals("Squad 2", usersInLeague.get(1).getSquadName()),
                () -> assertEquals(2, usersInLeague.get(1).getPoints()));

    }
}
