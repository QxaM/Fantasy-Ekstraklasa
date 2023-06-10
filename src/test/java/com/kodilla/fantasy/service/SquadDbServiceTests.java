package com.kodilla.fantasy.service;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.kodilla.fantasy.domain.Squad;
import com.kodilla.fantasy.domain.User;
import com.kodilla.fantasy.domain.exception.ElementNotFoundException;
import com.kodilla.fantasy.repository.SquadRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SquadDbServiceTests {

    @InjectMocks
    private SquadDbService squadDbService;
    @Mock
    private SquadRepository squadRepository;

    @Test
    void shouldGetSquads() {
        //Given
        Squad squad1 = new Squad(1L, "Squad 1", new ArrayList<>());
        Squad squad2 = new Squad(2L, "Squad 2", new ArrayList<>());
        List<Squad> squads = List.of(squad1, squad2);
        when(squadRepository.findAll()).thenReturn(squads);

        //When
        List<Squad> foundSquads = squadDbService.getSquads();

        //Then
        assertAll(() -> assertEquals(2, foundSquads.size()),
                () -> assertEquals("Squad 1", foundSquads.get(0).getName()),
                () -> assertEquals("Squad 2", foundSquads.get(1).getName()));
    }

    @Test
    void shouldGetSquad() {
        //Given
        Squad squad = new Squad(1L, "Squad 1", new ArrayList<>());
        when(squadRepository.findById(1L)).thenReturn(Optional.of(squad));

        //When
        Squad foundSquad = new Squad();
        try {
            foundSquad = squadDbService.getSquad(1L);
        } catch (ElementNotFoundException e) {}

        //Then
        assertEquals("Squad 1", foundSquad.getName());
    }

    @Test
    void shouldNotGetSquad() {
        //Given

        //When

        //Then
        assertThrows(ElementNotFoundException.class, () -> squadDbService.getSquad(1L));
    }

    @Test
    void shouldSaveSquad() {
        //Given
        Squad squad = new Squad(1L, "Squad 1", new ArrayList<>());
        when(squadRepository.save(any(Squad.class))).thenReturn(squad);

        //Then
        Squad savedUser = squadDbService.saveSquad(squad);

        //When
        assertEquals("Squad 1", savedUser.getName());
    }

    @Test
    void shouldDeleteUser() {
        //Given

        //When
        squadDbService.deleteSquad(1L);

        //Then
        verify(squadRepository, times(1)).deleteById(1L);
    }
}
