package com.kodilla.fantasy.repository;

import com.kodilla.fantasy.domain.Player;
import com.kodilla.fantasy.domain.Position;
import com.kodilla.fantasy.domain.Team;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class PlayerRepositoryTests {

    @Autowired
    private PlayerRepository repository;
    @Autowired
    private TeamRepository teamRepository;

    @Test
    void testSave() {
        //Given
        Player player = Player.builder()
                .apiFootballId(2L)
                .firstname("Test")
                .lastname("Test")
                .age(21)
                .value(BigDecimal.ONE)
                .position(Position.ST)
                .build();

        //When
        repository.save(player);

        //Then
        long id = player.getId();
        Optional<Player> savedPlayer = repository.findById(id);
        assertTrue(savedPlayer.isPresent());
        assertEquals("Test", savedPlayer.get().getFirstname());

        //CleanUp
        repository.deleteById(id);
    }

    @Test
    void testPlayerGetById() {
        //Given
        Player player = Player.builder()
                .apiFootballId(2L)
                .firstname("Test")
                .lastname("Test")
                .age(21)
                .value(BigDecimal.ONE)
                .position(Position.ST)
                .build();
        repository.save(player);

        //When
        long id = player.getId();
        Optional<Player> foundPlayer = repository.findById(id);

        //Then
        assertTrue(foundPlayer.isPresent());
        assertEquals("Test", foundPlayer.get().getFirstname());

        //CleanUp
        repository.deleteById(id);
    }

    @Test
    void testGetAllPlayers() {
        //Given
        Player player1 = Player.builder()
                .apiFootballId(2L)
                .firstname("Test1")
                .lastname("Test1")
                .age(21)
                .value(BigDecimal.ONE)
                .position(Position.ST)
                .build();
        Player player2 = Player.builder()
                .apiFootballId(3L)
                .firstname("Test2")
                .lastname("Test2")
                .age(21)
                .value(BigDecimal.ONE)
                .position(Position.ST)
                .build();
        repository.save(player1);
        repository.save(player2);
        Pageable firstPageWithOneElement = PageRequest.of(0, 1);

        //When
        Page<Player> playersPage = repository.findAll(firstPageWithOneElement);

        //Then
        List<Player> foundPlayer = playersPage.getContent();
        assertEquals(2, playersPage.getTotalPages());
        assertEquals(2, playersPage.getTotalElements());
        assertEquals(1, foundPlayer.size());
        assertEquals("Test1", foundPlayer.get(0).getFirstname());

        //CleanUp
        long playerId1 = player1.getId();
        long playerId2 = player2.getId();
        repository.deleteById(playerId1);
        repository.deleteById(playerId2);
    }

    @Test
    void testGetSortedPlayers() {
        //Given
        Team team1 = new Team(1L, "Team1", "TE1");
        Team team2 = new Team(2L, "Team2", "TE2");
        Team team3 = new Team(3L, "Team3", "TE3");
        Player player1 = Player.builder()
                .apiFootballId(2L)
                .firstname("Test1")
                .lastname("Test1")
                .age(21)
                .value(BigDecimal.ONE)
                .position(Position.ST)
                .team(team1)
                .build();
        Player player2 = Player.builder()
                .apiFootballId(3L)
                .firstname("Test2")
                .lastname("Test2")
                .age(21)
                .value(BigDecimal.valueOf(3))
                .position(Position.GK)
                .team(team2)
                .build();
        Player player3 = Player.builder()
                .apiFootballId(3L)
                .firstname("Test3")
                .lastname("Test3")
                .age(21)
                .value(BigDecimal.valueOf(3))
                .position(Position.MID)
                .team(team3)
                .build();
        teamRepository.saveAll(List.of(team1, team2, team3));
        repository.save(player1);
        repository.save(player2);
        repository.save(player3);

        Pageable pageIdAscending = PageRequest.of(0, 1, Sort.by("id").ascending());
        Pageable pageIdDescending = PageRequest.of(0, 2, Sort.by("id").descending());
        Pageable pageLastnameAscending = PageRequest.of(0, 3, Sort.by("lastname").ascending());
        Pageable pageLastnameDescending = PageRequest.of(0, 3, Sort.by("lastname").descending());
        Pageable pageValueAscending = PageRequest.of(0, 3, Sort.by("value").ascending());
        Pageable pageValueDescending = PageRequest.of(0, 3, Sort.by("value").descending());
        Pageable pageTeamAscending = PageRequest.of(0, 3, Sort.by("team").ascending());
        Pageable pageTeamDescending = PageRequest.of(0, 3, Sort.by("team").descending());

        //When
        Page<Player> idAscending = repository.findAll(pageIdAscending);
        Page<Player> idDescending = repository.findAll(pageIdDescending);
        Page<Player> lastnameAscending = repository.findAll(pageLastnameAscending);
        Page<Player> lastnameDescending = repository.findAll(pageLastnameDescending);
        Page<Player> valueAscending = repository.findAll(pageValueAscending);
        Page<Player> valueDescending = repository.findAll(pageValueDescending);
        Page<Player> teamAscending = repository.findAll(pageTeamAscending);
        Page<Player> teamDescending = repository.findAll(pageTeamDescending);

        //Then
        assertAll(() -> assertEquals("Test1", idAscending.getContent().get(0).getFirstname()),
                () -> assertEquals("Test3", idDescending.getContent().get(0).getFirstname()),
                () -> assertEquals("Test1", lastnameAscending.getContent().get(0).getFirstname()),
                () -> assertEquals("Test3", lastnameDescending.getContent().get(0).getFirstname()),
                () -> assertEquals("Test1", valueAscending.getContent().get(0).getFirstname()),
                () -> assertEquals("Test2", valueDescending.getContent().get(0).getFirstname()),
                () -> assertEquals("Test1", teamAscending.getContent().get(0).getFirstname()),
                () -> assertEquals("Test3", teamDescending.getContent().get(0).getFirstname()));

        //CleanUp
        repository.deleteById(player1.getId());
        repository.deleteById(player2.getId());
        repository.deleteById(player3.getId());
        teamRepository.deleteById(team1.getId());
        teamRepository.deleteById(team2.getId());
        teamRepository.deleteById(team3.getId());
    }

    @Test
    void testDeletePlayerById() {
        //Given
        Player player = Player.builder()
                .apiFootballId(2L)
                .firstname("Test")
                .lastname("Test")
                .age(21)
                .value(BigDecimal.ONE)
                .position(Position.ST)
                .build();
        repository.save(player);
        long id = player.getId();

        //When
        repository.deleteById(id);

        //Then
        Optional<Player> foundPlayer = repository.findById(id);
        assertFalse(foundPlayer.isPresent());
    }
}
