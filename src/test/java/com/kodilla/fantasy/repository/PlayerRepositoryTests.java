package com.kodilla.fantasy.repository;

import com.kodilla.fantasy.domain.Player;
import com.kodilla.fantasy.domain.Position;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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

    @Test
    void testSave() {
        //Given
        Player player = new Player.PlayerBuilder()
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
        Player player = new Player.PlayerBuilder()
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
        Player player1 = new Player.PlayerBuilder()
                .apiFootballId(2L)
                .firstname("Test1")
                .lastname("Test1")
                .age(21)
                .value(BigDecimal.ONE)
                .position(Position.ST)
                .build();
        Player player2 = new Player.PlayerBuilder()
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
    void testDeletePlayerById() {
        //Given
        Player player = new Player.PlayerBuilder()
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
