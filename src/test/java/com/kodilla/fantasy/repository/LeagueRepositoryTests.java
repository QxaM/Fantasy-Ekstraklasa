package com.kodilla.fantasy.repository;

import com.kodilla.fantasy.domain.League;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class LeagueRepositoryTests {

    @Autowired
    private LeagueRepository leagueRepository;

    @Test
    void testSave() {
        //Given
        League league = new League("League 1");

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
        League league1 = new League("League 1");
        League league2 = new League("League 2");
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
        League league = new League("League 1");
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
    void testDeleteLeague() {
        //Given
        League league = new League("League 1");
        leagueRepository.save(league);
        Long id = league.getId();

        //When
        leagueRepository.deleteById(id);

        //Then
        Optional<League> foundLeague = leagueRepository.findById(id);
        assertFalse(foundLeague.isPresent());
    }
}
