package com.kodilla.fantasy.repository;

import com.kodilla.fantasy.domain.Player;
import com.kodilla.fantasy.domain.Position;
import com.kodilla.fantasy.domain.Squad;
import com.kodilla.fantasy.domain.Team;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class SquadRepositoryTests {

    @Autowired
    private SquadRepository squadRepository;
    @Autowired
    private PlayerRepository playerRepository;

    @Test
    void shouldSaveSquad() {
        //Given
        Squad squad = new Squad("Squad 1", new ArrayList<>());

        //When
        squadRepository.save(squad);
        Long id = squad.getId();

        //Then
        Optional<Squad> foundSquad = squadRepository.findById(id);
        assertTrue(foundSquad.isPresent());
        assertEquals("Squad 1", foundSquad.get().getName());

        //CleanUp
        squadRepository.deleteById(id);
    }

    @Test
    void shouldGetSquads() {
        //Given
        Squad squad1 = new Squad("Squad 1", new ArrayList<>());
        Squad squad2 = new Squad("Squad 2", new ArrayList<>());
        squadRepository.saveAll(List.of(squad1, squad2));
        Long id1 = squad1.getId();
        Long id2 = squad2.getId();

        //When
        List<Squad> squads = squadRepository.findAll();

        //Then
        assertAll(() -> assertEquals(2, squads.size()),
                () -> assertEquals("Squad 1", squads.get(0).getName()),
                () -> assertEquals("Squad 2", squads.get(1).getName()));

        //CleanUp
        squadRepository.deleteById(id1);
        squadRepository.deleteById(id2);
    }

    @Test
    void shouldGetSquad() {
        //Given
        Squad squad = new Squad("Squad 1", new ArrayList<>());
        squadRepository.save(squad);
        Long id = squad.getId();

        //When
        Optional<Squad> foundSquad = squadRepository.findById(id);

        //Then
        assertTrue(foundSquad.isPresent());
        assertEquals("Squad 1", foundSquad.get().getName());

        //CleanUp
        squadRepository.deleteById(id);
    }

    @Test
    void shouldGetSquadWithPlayers() {
        //Given
        Squad squad = new Squad("Squad 1", new ArrayList<>());
        Player player1 = new Player(2L, "Test1", "Test1", 21, BigDecimal.ONE, Position.ST);
        Player player2 = new Player(3L, "Test2", "Test2", 21, BigDecimal.ONE, Position.ST);
        playerRepository.save(player1);
        playerRepository.save(player2);
        Long player1Id = player1.getId();
        Long player2Id = player2.getId();

        squad.getPlayers().addAll(List.of(player1, player2));
        squadRepository.save(squad);
        Long id = squad.getId();

        //When
        Optional<Squad> foundSquad = squadRepository.findById(id);

        //Then
        assertTrue(foundSquad.isPresent());
        assertEquals("Test1", foundSquad.get().getPlayers().get(0).getFirstname());
        assertEquals("Test2", foundSquad.get().getPlayers().get(1).getFirstname());

        //CleanUp
        squadRepository.deleteById(id);
        playerRepository.deleteById(player1Id);
        playerRepository.deleteById(player2Id);
    }

    @Test
    void shouldDeleteSquad() {
        //Given
        Squad squad = new Squad("Squad 1", new ArrayList<>());
        squadRepository.save(squad);
        Long id = squad.getId();

        //When
        squadRepository.deleteById(id);

        //Then
        Optional<Squad> foundSquad = squadRepository.findById(id);
        assertFalse(foundSquad.isPresent());
    }
}
