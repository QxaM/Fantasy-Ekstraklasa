package com.kodilla.fantasy.service;

import com.kodilla.fantasy.domain.Player;
import com.kodilla.fantasy.domain.Position;
import com.kodilla.fantasy.repository.PlayerRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class PlayerDbServiceTests {

    @InjectMocks
    private PlayerDbService playerDbService;
    @Mock
    PlayerRepository playerRepository;

    @Test
    void shouldInitPlayers() {
        //Given
        Player player1 = new Player(1L, 2L, "Test", "Test", 21, BigDecimal.ONE, Position.ST);
        Player player2 = new Player(2L, 3L, "Test2", "Test2", 22, BigDecimal.TEN, Position.GK);

        //When
        playerDbService.initPlayers(List.of(player1, player2));

        //Then
        verify(playerRepository, times(1)).deleteAll();
        verify(playerRepository, times(1)).saveAll(anyList());
    }
}
