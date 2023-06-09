package com.kodilla.fantasy.service;

import com.kodilla.fantasy.domain.League;
import com.kodilla.fantasy.domain.exception.ElementNotFoundException;
import com.kodilla.fantasy.repository.LeagueRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class LeagueDbServiceTests {

    @InjectMocks
    private LeagueDbService leagueDbService;
    @Mock
    private LeagueRepository leagueRepository;

    @Test
    void shouldFetchLeagues() {
        //Given
        League league1 = new League(1L, "League 1", new ArrayList<>());
        League league2 = new League(2L, "League 2", new ArrayList<>());
        List<League> leagues = List.of(league1, league2);
        when(leagueRepository.findAll()).thenReturn(leagues);

        //When
        List<League> foundLeagues = leagueDbService.getLeagues();

        //Then
        assertAll(() -> assertEquals(2, foundLeagues.size()),
                () -> assertEquals("League 1", leagues.get(0).getName()),
                () -> assertEquals("League 2", leagues.get(1).getName()));
    }

    @Test
    void shouldFetchLeague() {
        //Given
        League league1 = new League(1L, "League 1", new ArrayList<>());
        when(leagueRepository.findById(1L)).thenReturn(Optional.of(league1));

        //When
        League foundLeague = new League();
        try {
            foundLeague = leagueDbService.getLeague(1L);
        } catch (ElementNotFoundException e) {}

        //Then
        assertEquals("League 1", foundLeague.getName());
    }

    @Test
    void shouldNotFetchLeague() {
        //Given

        //When

        //Then
        assertThrows(ElementNotFoundException.class, () -> leagueDbService.getLeague(1L));
    }

    @Test
    void shouldSaveLeague() {
        //Given
        League league1 = new League(1L, "League 1", new ArrayList<>());
        when(leagueRepository.save(any(League.class))).thenReturn(league1);

        //When
        League savedLeague = leagueDbService.saveLeague(league1);

        //Then
        assertEquals("League 1", savedLeague.getName());
    }

    @Test
    void shouldDeleteLeague() {
        //Given

        //Then
        leagueDbService.deleteLeague(1L);

        //Then
        verify(leagueRepository, times(1)).deleteById(1L);
    }
}
