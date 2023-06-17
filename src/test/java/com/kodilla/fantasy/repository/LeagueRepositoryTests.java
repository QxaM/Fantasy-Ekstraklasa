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
public class LeagueRepositoryTests {

    @Autowired
    private LeagueRepository leagueRepository;
    @Autowired
    private UserRepository userRepository;

    @Test
    void testSave() {
        //Given
        League league = new League("League 1", new ArrayList<>());

        //When
        leagueRepository.save(league);
        Long id = league.getId();

        //Then
        Optional<League> foundLeague = leagueRepository.findById(id);
        assertTrue(foundLeague.isPresent());
        assertEquals("League 1", foundLeague.get().getName());

        //CleanUp
        leagueRepository.deleteById(id);
    }

    @Test
    void testGetAll() {
        //Given
        League league1 = new League("League 1", new ArrayList<>());
        League league2 = new League("League 2", new ArrayList<>());
        leagueRepository.saveAll(List.of(league1, league2));
        Long id1 = league1.getId();
        Long id2 = league2.getId();

        //When
        List<League> leagues = leagueRepository.findAll();

        //Then
        assertAll(() -> assertEquals(2, leagues.size()),
                () -> assertEquals("League 1", leagues.get(0).getName()),
                () -> assertEquals("League 2", leagues.get(1).getName()));

        //CleanUp
        leagueRepository.deleteById(id1);
        leagueRepository.deleteById(id2);
    }

    @Test
    void testGetLeague() {
        //Given
        League league = new League("League 1", new ArrayList<>());
        leagueRepository.save(league);
        Long id = league.getId();

        //When
        Optional<League> foundLeague = leagueRepository.findById(id);

        //Then
        assertTrue(foundLeague.isPresent());
        assertEquals("League 1", foundLeague.get().getName());

        //CleanUp
        leagueRepository.deleteById(id);
    }

    @Test
    @Transactional
    void testGetLeagueWithUsers() {
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
        League league = new League("League 1", new ArrayList<>());
        league.getUsers().addAll(List.of(user1, user2));
        user1.getLeagues().add(league);
        user2.getLeagues().add(league);

        userRepository.saveAll(List.of(user1, user2));
        Long user1Id = user1.getId();
        Long user2Id = user2.getId();
        leagueRepository.save(league);
        Long id = league.getId();

        //When
        Optional<League> foundLeague = leagueRepository.findById(id);

        //Then
        assertTrue(foundLeague.isPresent());
        assertEquals(2, foundLeague.get().getUsers().size());
        assertEquals("User 1", foundLeague.get().getUsers().get(0).getUsername());
        assertEquals("User 2", foundLeague.get().getUsers().get(1).getUsername());

        //CleanUp
        userRepository.deleteById(user1Id);
        userRepository.deleteById(user2Id);
        leagueRepository.deleteById(id);
    }

    @Test
    void testGetLeaguesByUsers() {
        //Given
        User user = User.builder()
                .username("User 1")
                .leagues(new ArrayList<>())
                .squad(new Squad())
                .build();
        League league1 = new League("League 1", new ArrayList<>());
        League league2 = new League("League 2", new ArrayList<>());
        league1.getUsers().add(user);
        league2.getUsers().add(user);
        user.getLeagues().addAll(List.of(league1, league2));

        leagueRepository.saveAll(List.of(league1, league2));
        Long league1Id = league1.getId();
        Long league2Id = league2.getId();
        userRepository.save(user);
        Long id = user.getId();

        //When
        List<League> leagues = leagueRepository.findLeaguesByUsersId(id);

        //Then
        assertAll(() -> assertEquals(2, leagues.size()),
                () -> assertEquals("League 1", leagues.get(0).getName()),
                () -> assertEquals("League 2", leagues.get(1).getName()));

        //CleanUp
        userRepository.deleteById(id);
        leagueRepository.deleteById(league1Id);
        leagueRepository.deleteById(league2Id);
    }

    @Test
    void testDeleteLeague() {
        //Given
        League league = new League("League 1", new ArrayList<>());
        leagueRepository.save(league);
        Long id = league.getId();

        //When
        leagueRepository.deleteById(id);

        //Then
        Optional<League> foundLeague = leagueRepository.findById(id);
        assertFalse(foundLeague.isPresent());
    }
}
