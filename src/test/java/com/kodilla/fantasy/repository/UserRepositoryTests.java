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
        User user = User.builder()
                .username("User 1")
                .leagues(new ArrayList<>())
                .squad(new Squad())
                .build();

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
        User user1 = User.builder()
                .username("User 1")
                .leagues(new ArrayList<>())
                .squad(new Squad())
                .build();
        User user2 = User.builder()
                .username("User 2")
                .leagues(new ArrayList<>())
                .squad(new Squad())
                .build();
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
        User user = User.builder()
                .username("User 1")
                .leagues(new ArrayList<>())
                .squad(new Squad())
                .build();
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
        User user = User.builder()
                .username("User 1")
                .leagues(new ArrayList<>())
                .squad(new Squad())
                .build();
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
    void shouldGetUserByLeagueId() {
        //Given
        User user1 = User.builder()
                .username("User 1")
                .leagues(new ArrayList<>())
                .squad(new Squad())
                .build();
        User user2 = User.builder()
                .username("User 2")
                .leagues(new ArrayList<>())
                .squad(new Squad())
                .build();
        League league1 = new League("League 1", new ArrayList<>());
        user1.getLeagues().add(league1);
        user2.getLeagues().add(league1);
        league1.getUsers().addAll(List.of(user1, user2));

        leagueRepository.save(league1);
        Long id = league1.getId();
        userRepository.saveAll(List.of(user1, user2));
        Long user1Id = user1.getId();
        Long user2Id = user2.getId();

        //When
        List<User> foundUsers = userRepository.findUserByLeaguesId(id);

        //Then
        assertAll(() -> assertEquals(2, foundUsers.size()),
                () -> assertEquals("User 1", foundUsers.get(0).getUsername()),
                () -> assertEquals("User 2", foundUsers.get(1).getUsername()));

        //CleanUp
        userRepository.deleteById(user1Id);
        userRepository.deleteById(user2Id);
        leagueRepository.deleteById(id);
    }

    @Test
    void shouldDeleteUser() {
        //Given
        User user = User.builder()
                .username("User 1")
                .leagues(new ArrayList<>())
                .squad(new Squad())
                .build();
        userRepository.save(user);
        Long id = user.getId();

        //When
        userRepository.deleteById(id);

        //Then
        Optional<User> foundUser = userRepository.findById(id);
        assertFalse(foundUser.isPresent());
    }
}
