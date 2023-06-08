package com.kodilla.fantasy.controller;

import com.kodilla.fantasy.domain.Player;
import com.kodilla.fantasy.domain.dto.PlayerDto;
import com.kodilla.fantasy.domain.dto.PlayersPagedDto;
import com.kodilla.fantasy.domain.exception.ElementNotFoundException;
import com.kodilla.fantasy.mapper.PlayerMapper;
import com.kodilla.fantasy.service.PlayerDbService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("players")
@RequiredArgsConstructor
public class PlayerController {

    private final PlayerDbService service;
    private final PlayerMapper mapper;

    @GetMapping("/page/{pageNo}")
    public ResponseEntity<PlayersPagedDto> getPlayers(@PathVariable Integer pageNo) {
        Page<Player> foundPlayers = service.getPlayers(pageNo);
        PlayersPagedDto mappedPlayers = mapper.mapToPlayersPagedDto(foundPlayers);
        return ResponseEntity.ok(mappedPlayers);
    }

    @GetMapping("{id}")
    public ResponseEntity<PlayerDto> getPlayer(@PathVariable Long id) throws ElementNotFoundException {
        Player foundPlayer = service.getPlayer(id);
        PlayerDto mappedPlayer = mapper.mapToPlayerDto(foundPlayer);
        return ResponseEntity.ok(mappedPlayer);
    }

    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PlayerDto> updatePlayer(@RequestBody PlayerDto playerDto) throws ElementNotFoundException {
        Player mappedPlayer = mapper.mapToPlayer(playerDto);
        Player savedPlayer = service.updatePlayer(mappedPlayer);
        PlayerDto mappedPlayerDto = mapper.mapToPlayerDto(savedPlayer);
        return ResponseEntity.ok(mappedPlayerDto);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> deletePlayer(@PathVariable Long id) {
        service.deletePlayer(id);
        return ResponseEntity.ok().build();
    }
}
