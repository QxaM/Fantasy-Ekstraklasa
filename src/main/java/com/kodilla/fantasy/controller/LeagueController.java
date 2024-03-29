package com.kodilla.fantasy.controller;

import com.kodilla.fantasy.domain.League;
import com.kodilla.fantasy.domain.dto.LeagueDto;
import com.kodilla.fantasy.domain.exception.ElementNotFoundException;
import com.kodilla.fantasy.domain.exception.UserAlreadyInLeagueException;
import com.kodilla.fantasy.mapper.LeagueMapper;
import com.kodilla.fantasy.service.LeagueDbService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("leagues")
@RequiredArgsConstructor
public class LeagueController {

    private final LeagueDbService service;
    private final LeagueMapper mapper;

    @GetMapping
    public ResponseEntity<List<LeagueDto>> getLeagues() {
        List<League> foundLeagues = service.getLeagues();
        List<LeagueDto> mappedLeagues = mapper.mapToLeagueDtoList(foundLeagues);
        return ResponseEntity.ok(mappedLeagues);
    }

    @GetMapping("{id}")
    public ResponseEntity<LeagueDto> getLeague(@PathVariable Long id) throws ElementNotFoundException {
        League foundLeague = service.getLeague(id);
        LeagueDto leagueDto = mapper.mapToLeagueDto(foundLeague);
        return ResponseEntity.ok(leagueDto);
    }

    @GetMapping("byUser/{userId}")
    public ResponseEntity<List<LeagueDto>> getUserLeagues(@PathVariable Long userId) {
        List<League> foundLeagues = service.getLeaguesByUserId(userId);
        List<LeagueDto> mappedLeagues = mapper.mapToLeagueDtoList(foundLeagues);
        return ResponseEntity.ok(mappedLeagues);
    }

    @PostMapping("{leagueName}")
    public ResponseEntity<LeagueDto> createLeague(@PathVariable String leagueName) {
        League leagueToSave = League.builder()
                .name(leagueName)
                .users(new ArrayList<>())
                .build();
        League savedLeague = service.saveLeague(leagueToSave);
        LeagueDto mappedLeague = mapper.mapToLeagueDto(savedLeague);
        return ResponseEntity.ok(mappedLeague);
    }

    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<LeagueDto> updateLeague(@RequestBody LeagueDto leagueDto) throws ElementNotFoundException {
        League foundLeague = service.getLeague(leagueDto.getId());

        League leagueToSave = League.builder()
                .name(leagueDto.getName())
                .users(foundLeague.getUsers())
                .build();
        leagueToSave.setId(foundLeague.getId());

        League savedLeague = service.saveLeague(leagueToSave);
        LeagueDto mappedLeague = mapper.mapToLeagueDto(savedLeague);
        return ResponseEntity.ok(mappedLeague);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteLeague(@PathVariable Long id) throws ElementNotFoundException {
        service.deleteLeague(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("{leagueId}/addUser/{userId}")
    public ResponseEntity<Void> addUser(@PathVariable Long leagueId,
                                        @PathVariable Long userId) throws ElementNotFoundException,
                                                                            UserAlreadyInLeagueException {
        service.addUser(leagueId, userId);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping("{leagueId}/removeUser/{userId}")
    public ResponseEntity<Void> removeUser(@PathVariable Long leagueId,
                                           @PathVariable Long userId) throws ElementNotFoundException {
        service.removeUser(leagueId, userId);
        return ResponseEntity.ok().build();
    }
}
