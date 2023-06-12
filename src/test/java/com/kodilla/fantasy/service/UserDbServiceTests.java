package com.kodilla.fantasy.service;

import com.kodilla.fantasy.domain.League;
import com.kodilla.fantasy.domain.Squad;
import com.kodilla.fantasy.domain.User;
import com.kodilla.fantasy.domain.exception.ElementNotFoundException;
import com.kodilla.fantasy.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserDbServiceTests {

    @InjectMocks
    private UserDbService userDbService;
    @Mock
    private UserRepository userRepository;
    @Mock
    private SquadDbService squadDbService;

    @Test
    void shouldGetUsers() {
        //Given
        User user1 = new User(1L, "User 1", new ArrayList<>(), new Squad(), 0);
        User user2 = new User(2L, "User 2", new ArrayList<>(), new Squad(), 0);
        List<User> users = List.of(user1, user2);
        when(userRepository.findAll()).thenReturn(users);

        //When
        List<User> foundUsers = userDbService.getUsers();

        //Then
        assertAll(() -> assertEquals(2, foundUsers.size()),
                () -> assertEquals("User 1", foundUsers.get(0).getUsername()),
                () -> assertEquals("User 2", foundUsers.get(1).getUsername()));
    }

    @Test
    void shouldGetUser() {
        //Given
        User user = new User(1L, "User 1", new ArrayList<>(), new Squad(), 0);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        //When
        User foundUser = new User();
        try {
            foundUser = userDbService.getUser(1L);
        } catch (ElementNotFoundException e) {}

        //Then
        assertEquals("User 1", foundUser.getUsername());
    }

    @Test
    void shouldNotGetUser() {
        //Given

        //When

        //Then
        assertThrows(ElementNotFoundException.class, () -> userDbService.getUser(1L));
    }

    @Test
    void shouldSaveUser() {
        //Given
        User user = new User(1L, "User 1", new ArrayList<>(), new Squad(), 0);
        when(userRepository.save(any(User.class))).thenReturn(user);

        //Then
        User savedUser = userDbService.saveUser(user);

        //When
        assertEquals("User 1", savedUser.getUsername());
    }

    @Test
    void shouldDeleteUser() throws ElementNotFoundException {
        //Given
        when(userRepository.existsById(1L)).thenReturn(true);

        //When
        userDbService.deleteUser(1L);

        //Then
        verify(userRepository, times(1)).deleteById(1L);
    }

    @Test
    void shouldNotDeleteUser() {
        //Given

        //Then

        //When
        assertThrows(ElementNotFoundException.class, () -> userDbService.deleteUser(1L));
    }

    @Test
    void shouldCreateSquad() throws ElementNotFoundException {
        //Given
        User user = new User(1L, "User 1", new ArrayList<>(), new Squad(), 0);
        Squad squad = new Squad(1L, "Squad 1", BigDecimal.ONE, new HashSet<>());
        User userWithSquad = new User(1L, "User 1", new ArrayList<>(), squad, 0);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.save(user)).thenReturn(userWithSquad);

        //When
        User savedUser = userDbService.createSquad(1L, squad.getName());

        //Then
        assertEquals("Squad 1", savedUser.getSquad().getName());
    }

    @Test
    void shouldNotCreateSquad() {
        //Given
        Squad squad = new Squad(1L, "Squad 1", BigDecimal.ONE, new HashSet<>());

        //When + Then
        assertThrows(ElementNotFoundException.class, () -> userDbService.createSquad(1L, squad.getName()));
    }

    @Test
    void shouldDetachUsers() {
        //Given
        League league = new League(1L, "Test League", new ArrayList<>());
        User user = new User(1L, "Test 1");
        user.getLeagues().add(league);
        when(userRepository.findUserByLeaguesId(1L)).thenReturn(List.of(user));

        //When
        userDbService.detachUsers(league);

        //Then
        assertFalse(user.getLeagues().contains(league));
    }
}
