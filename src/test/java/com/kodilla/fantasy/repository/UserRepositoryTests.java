package com.kodilla.fantasy.repository;

import com.kodilla.fantasy.domain.League;
import com.kodilla.fantasy.domain.Squad;
import com.kodilla.fantasy.domain.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class UserRepositoryTests {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private LeagueRepository leagueRepository;

    @Test
    void shouldSaveUser() {
        //Given
        User user = new User("User 1", new ArrayList<>(), new Squad());

        //When
        userRepository.save(user);
        Long id = user.getId();

        //Then
        Optional<User> foundUser = userRepository.findById(id);
        assertTrue(foundUser.isPresent());
        assertEquals("User 1", foundUser.get().getUsername());

        //CleanUp
        userRepository.deleteById(id);
    }

    @Test
    void shouldGetUsers() {
        //Given
        User user1 = new User("User 1", new ArrayList<>(), new Squad());
        User user2 = new User("User 2", new ArrayList<>(), new Squad());
        userRepository.saveAll(List.of(user1, user2));
        Long id1 = user1.getId();
        Long id2 = user2.getId();

        //When
        List<User> users = userRepository.findAll();

        //Then
        assertAll(() -> assertEquals(2, users.size()),
                () -> assertEquals("User 1", users.get(0).getUsername()),
                () -> assertEquals("User 2", users.get(1).getUsername()));

        //CleanUp
        userRepository.deleteById(id1);
        userRepository.deleteById(id2);
    }

    @Test
    void shouldGetUser() {
        //Given
        User user = new User("User 1", new ArrayList<>(), new Squad());
        userRepository.save(user);
        Long id = user.getId();

        //When
        Optional<User> foundUser = userRepository.findById(id);

        //Then
        assertTrue(foundUser.isPresent());
        assertEquals("User 1", foundUser.get().getUsername());

        //CleanUp
        userRepository.deleteById(id);
    }

    @Test
    @Transactional
    void shouldGetUserWithLeagues() {
        //Given
        User user = new User("User 1", new ArrayList<>(), new Squad());
        League league1 = new League("League 1", new ArrayList<>());
        League league2 = new League("League 2", new ArrayList<>());
        user.getLeagues().addAll(List.of(league1, league2));
        league1.getUsers().add(user);
        league2.getUsers().add(user);

        userRepository.save(user);
        Long id = user.getId();
        leagueRepository.saveAll(List.of(league1, league2));
        Long league1Id = league1.getId();
        Long league2Id = league2.getId();

        //When
        Optional<User> foundUser = userRepository.findById(id);

        //Then
        assertTrue(foundUser.isPresent());
        assertEquals(2, foundUser.get().getLeagues().size());
        assertEquals("League 1", foundUser.get().getLeagues().get(0).getName());
        assertEquals("League 2", foundUser.get().getLeagues().get(1).getName());

        //CleanUp
        leagueRepository.deleteById(league1Id);
        leagueRepository.deleteById(league2Id);
        userRepository.deleteById(id);
    }

    @Test
    void shouldDeleteUser() {
        //Given
        User user = new User("User 1", new ArrayList<>(), new Squad());
        userRepository.save(user);
        Long id = user.getId();

        //When
        userRepository.deleteById(id);

        //Then
        Optional<User> foundUser = userRepository.findById(id);
        assertFalse(foundUser.isPresent());
    }
}
