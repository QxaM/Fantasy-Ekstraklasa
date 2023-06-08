package com.kodilla.fantasy.repository;

import com.kodilla.fantasy.domain.Squad;
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
public class SquadRepositoryTests {

    @Autowired
    private SquadRepository squadRepository;

    @Test
    void shouldSaveSquad() {
        //Given
        Squad squad = new Squad("Squad 1");

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
        Squad squad1 = new Squad("Squad 1");
        Squad squad2 = new Squad("Squad 2");
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
        Squad squad = new Squad("Squad 1");
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
    void shouldDeleteSquad() {
        //Given
        Squad squad = new Squad("Squad 1");
        squadRepository.save(squad);
        Long id = squad.getId();

        //When
        squadRepository.deleteById(id);

        //Then
        Optional<Squad> foundSquad = squadRepository.findById(id);
        assertFalse(foundSquad.isPresent());
    }
}
