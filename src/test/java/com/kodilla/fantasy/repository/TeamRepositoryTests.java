package com.kodilla.fantasy.repository;

import com.kodilla.fantasy.domain.Player;
import com.kodilla.fantasy.domain.Position;
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
public class TeamRepositoryTests {

    @Autowired
    private TeamRepository repository;
    @Autowired
    private PlayerRepository playerRepository;

    @Test
    void testTeamSave() {
        //Given
        Team team = new Team(2L, "Test", "TET", new ArrayList<>());

        //When
        repository.save(team);

        //Then
        long id = team.getId();
        Optional<Team> savedTeam = repository.findById(id);
        assertTrue(savedTeam.isPresent());
        assertEquals("Test", savedTeam.get().getName());

        //CleanUp
        repository.deleteById(id);
    }

    @Test
    void testTeamGetById() {
        //Given
        Team team = new Team(2L, "Test", "TET", new ArrayList<>());
        repository.save(team);

        //When
        long id = team.getId();
        Optional<Team> foundTeam = repository.findById(id);

        //Then
        assertTrue(foundTeam.isPresent());
        assertEquals("Test", foundTeam.get().getName());

        //CleanUp
        repository.deleteById(id);
    }

    @Test
    void testTeamGetAll() {
        //Given
        Team team1 = new Team(2L, "Test1", "TE1", new ArrayList<>());
        Team team2 = new Team(3L, "Test2", "TE2", new ArrayList<>());
        repository.saveAll(List.of(team1, team2));

        //When
        long id1 = team1.getId();
        long id2 = team2.getId();
        List<Team> foundTeams = repository.findAll();

        //Then
        assertEquals(2, foundTeams.size());
        assertEquals("Test1", foundTeams.get(0).getName());
        assertEquals("Test2", foundTeams.get(1).getName());

        //CleanUp
        repository.deleteById(id1);
        repository.deleteById(id2);
    }

    @Test
    void testTeamGetByApiFootballId() {
        //Given
        Team team1 = new Team(2L, "Test1", "TE1", new ArrayList<>());
        Team team2 = new Team(3L, "Test2", "TE2", new ArrayList<>());
        repository.saveAll(List.of(team1, team2));

        //When
        long id1 = team1.getId();
        long id2 = team2.getId();
        Optional<Team> foundTeam = repository.findTeamByApiFootballId(3L);

        //Then
        assertTrue(foundTeam.isPresent());
        assertEquals("Test2", foundTeam.get().getName());
        assertEquals("TE2", foundTeam.get().getCode());

        //CleanUp
        repository.deleteById(id1);
        repository.deleteById(id2);
    }

    @Test
    void testSaveTeam_PlayersShouldNotBeSaved() {
        //Given
        Team team = new Team(2L, "Test", "TET", new ArrayList<>());
        Player player1 = new Player.PlayerBuilder()
                .apiFootballId(2L)
                .firstname("Test1")
                .lastname("Test1")
                .age(21)
                .value(BigDecimal.ONE)
                .position(Position.ST)
                .team(team)
                .build();
        Player player2 = new Player.PlayerBuilder()
                .apiFootballId(3L)
                .firstname("Test2")
                .lastname("Test2")
                .age(22)
                .value(BigDecimal.TEN)
                .position(Position.ST)
                .team(team)
                .build();

        player1.setId(1L);
        player2.setId(2L);
        team.getPlayers().add(player1);
        team.getPlayers().add(player2);

        //When
        repository.save(team);
        long teamId = team.getId();
        long playerId1 = player1.getId();
        long playerId2 = player2.getId();

        //Then
        Optional<Team> savedTeam = repository.findById(teamId);
        Optional<Player> savedPlayer1 = playerRepository.findById(playerId1);
        Optional<Player> savedPlayer2 = playerRepository.findById(playerId2);
        assertTrue(savedTeam.isPresent());
        assertFalse(savedPlayer1.isPresent());
        assertFalse(savedPlayer2.isPresent());

        //CleanUp
        repository.deleteById(teamId);
    }

    @Test
    void testTeamDelete_PlayersShouldDelete() {
        //Given
        Team team = new Team(2L, "Test", "TET", new ArrayList<>());
        Player player1 = new Player.PlayerBuilder()
                .apiFootballId(2L)
                .firstname("Test1")
                .lastname("Test1")
                .age(21)
                .value(BigDecimal.ONE)
                .position(Position.ST)
                .team(team)
                .build();
        Player player2 = new Player.PlayerBuilder()
                .apiFootballId(3L)
                .firstname("Test2")
                .lastname("Test2")
                .age(22)
                .value(BigDecimal.TEN)
                .position(Position.ST)
                .team(team)
                .build();
        team.getPlayers().add(player1);
        team.getPlayers().add(player2);
        repository.save(team);
        playerRepository.saveAll(List.of(player1, player2));

        //When
        long teamId = team.getId();
        long playerId1 = player1.getId();
        long playerId2 = player2.getId();
        repository.deleteById(teamId);

        //Then
        Optional<Player> foundPlayer1 = playerRepository.findById(playerId1);
        Optional<Player> foundPlayer2 = playerRepository.findById(playerId2);
        assertFalse(foundPlayer1.isPresent());
        assertFalse(foundPlayer2.isPresent());
    }
}
