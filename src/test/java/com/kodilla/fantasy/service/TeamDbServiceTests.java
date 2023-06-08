package com.kodilla.fantasy.service;

import com.kodilla.fantasy.domain.Team;
import com.kodilla.fantasy.domain.exception.ElementNotFoundException;
import com.kodilla.fantasy.repository.TeamRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TeamDbServiceTests {

    @InjectMocks
    private TeamDbService teamDbService;
    @Mock
    private TeamRepository teamRepository;

    @Test
    void shouldInitTeams() {
        //Given
        Team team1 = new Team(1L, 2L, "Test", "TET", new ArrayList<>());
        Team team2 = new Team(2L, 3L, "Test2", "TE2", new ArrayList<>());

        //When
        teamDbService.initTeams(List.of(team1, team2));

        //Then
        verify(teamRepository, times(1)).deleteAll();
        verify(teamRepository, times(1)).saveAll(anyList());
    }

    @Test
    void shouldGetTeams() {
        //Given
        Team team1 = new Team(1L, 2L, "Test", "TET", new ArrayList<>());
        Team team2 = new Team(2L, 3L, "Test2", "TE2", new ArrayList<>());
        when(teamRepository.findAll()).thenReturn(List.of(team1, team2));

        //When
        List<Team> teams = teamDbService.getTeams();

        //Then
        assertAll(() -> assertEquals(2, teams.size()),
                () -> assertEquals("Test", team1.getName()),
                () -> assertEquals("Test2", team2.getName()));
    }

    @Test
    void shouldFetchTeam() {
        //Given
        Team team = new Team(2L, 3L, "Test", "TET", new ArrayList<>());
        when(teamRepository.findById(2L)).thenReturn(Optional.of(team));

        //When
        Team foundTeam = new Team();
        try{
            foundTeam = teamDbService.getTeam(2L);
        } catch (ElementNotFoundException e) {}

        //Then
        assertEquals("Test", foundTeam.getName());
    }

    @Test
    void shouldNotFetchTeam() {
        //Given

        //When

        //Then
        assertThrows(ElementNotFoundException.class, () -> teamDbService.getTeam(2L));
    }

    @Test
    void testShouldUpdateTeam() {
        //Given
        Team team = new Team(2L, 3L, "Test", "TET", new ArrayList<>());
        when(teamRepository.findById(2L)).thenReturn(Optional.of(team));

        Team updatedTeam = new Team(2L, 3L, "Test update", "TET", new ArrayList<>());
        when(teamRepository.save(any(Team.class))).thenReturn(updatedTeam);

        //When
        Team testTeam = new Team();
        try{
            testTeam = teamDbService.updateTeam(team);
        } catch (ElementNotFoundException e) {}

        //Then
        assertEquals("Test update", testTeam.getName());
    }

    @Test
    void testShouldDeleteTeam() {
        //Given

        //When
        teamDbService.deleteTeam(2L);

        //Then
        verify(teamRepository, times(1)).deleteById(2L);
    }

    @Test
    void shouldFetchTeamByApiFootballId() {
        //Given
        Team team = new Team(1L, 3L, "Test", "TET", new ArrayList<>());
        when(teamRepository.findTeamByApiFootballId(3L)).thenReturn(Optional.of(team));

        //When
        Team foundTeam = teamDbService.getTeamByApiFootballId(3L);

        //Then
        assertAll(() -> assertEquals(1L, foundTeam.getId()),
                () -> assertEquals(3L, foundTeam.getApiFootballId()),
                () -> assertEquals("Test", foundTeam.getName()),
                () -> assertEquals("TET", foundTeam.getCode()));
    }

    @Test
    void shouldNotFetchTeamByApiFootballId() {
        //Given

        //When
        Team foundTeam = teamDbService.getTeamByApiFootballId(3L);
        //Then
        assertAll(() -> assertNotNull(foundTeam),
                () -> assertNull(foundTeam.getName()));
    }
}
