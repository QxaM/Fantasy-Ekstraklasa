package com.kodilla.fantasy.controller;

import com.kodilla.fantasy.domain.Squad;
import com.kodilla.fantasy.domain.dto.SquadDto;
import com.kodilla.fantasy.domain.exception.ElementNotFoundException;
import com.kodilla.fantasy.domain.exception.NotEnoughFundsException;
import com.kodilla.fantasy.domain.exception.PlayerAlreadyExistInSquadException;
import com.kodilla.fantasy.domain.exception.SquadAlreadyFullException;
import com.kodilla.fantasy.mapper.SquadMapper;
import com.kodilla.fantasy.service.SquadDbService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.awt.*;

@RestController
@RequestMapping("squads")
@RequiredArgsConstructor
public class SquadController {

    private final SquadDbService service;
    private final SquadMapper mapper;

    @GetMapping("{id}")
    public ResponseEntity<SquadDto> getSquad(@PathVariable Long id) throws ElementNotFoundException {
        Squad foundSquad = service.getSquad(id);
        SquadDto mappedSquad = mapper.mapToSquadDto(foundSquad);
        return ResponseEntity.ok(mappedSquad);
    }

    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SquadDto> updateSquad(@RequestBody SquadDto squadDto) throws ElementNotFoundException {
        Squad foundSquad = service.getSquad(squadDto.getId());
        Squad updatedSquad = new Squad(
                squadDto.getId(),
                squadDto.getName(),
                squadDto.getCurrentValue(),
                foundSquad.getPlayers()
        );
        Squad savedSquad = service.saveSquad(updatedSquad);
        SquadDto mappedSquad = mapper.mapToSquadDto(savedSquad);
        return ResponseEntity.ok(mappedSquad);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteSquad(@PathVariable Long id) {
        service.deleteSquad(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("{squadId}/addPlayer/{playerId}")
    public ResponseEntity<SquadDto> addPlayer(@PathVariable Long squadId,
                                              @PathVariable Long playerId)
                                                    throws ElementNotFoundException,
                                                    PlayerAlreadyExistInSquadException,
                                                    NotEnoughFundsException,
                                                    SquadAlreadyFullException {
        Squad savedSquad = service.addPlayer(squadId, playerId);
        SquadDto mappedSquad = mapper.mapToSquadDto(savedSquad);
        return ResponseEntity.ok(mappedSquad);
    }

    @PutMapping("{squadId}/removePlayer/{playerId}")
    public ResponseEntity<SquadDto> removePlayer (@PathVariable Long squadId,
                                                  @PathVariable Long playerId)
                                                        throws ElementNotFoundException {
        Squad savedSquad = service.removePlayer(squadId, playerId);
        SquadDto mappedSquad = mapper.mapToSquadDto(savedSquad);
        return ResponseEntity.ok(mappedSquad);
    }
}
