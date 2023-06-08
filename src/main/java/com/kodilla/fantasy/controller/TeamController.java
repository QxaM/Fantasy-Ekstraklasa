package com.kodilla.fantasy.controller;

import com.kodilla.fantasy.domain.Team;
import com.kodilla.fantasy.domain.dto.TeamDto;
import com.kodilla.fantasy.domain.exception.ElementNotFoundException;
import com.kodilla.fantasy.mapper.TeamMapper;
import com.kodilla.fantasy.service.TeamDbService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("teams")
@RequiredArgsConstructor
public class TeamController {

    private final TeamDbService service;
    private final TeamMapper mapper;

    @GetMapping
    public ResponseEntity<List<TeamDto>> getTeams() {
        List<Team> foundTeams = service.getTeams();
        List<TeamDto> mappedTeams = mapper.mapToTeamDtoList(foundTeams);
        return ResponseEntity.ok(mappedTeams);
    }

    @GetMapping({"{id}"})
    public ResponseEntity<TeamDto> getTeam(@PathVariable Long id) throws ElementNotFoundException {
        Team foundTeam = service.getTeam(id);
        TeamDto mappedTeam = mapper.mapToTeamDto(foundTeam);
        return ResponseEntity.ok(mappedTeam);
    }

    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TeamDto> updateTeam(@RequestBody TeamDto teamDto) throws ElementNotFoundException {
        Team mappedTeam = mapper.mapToTeam(teamDto);
        Team savedTeam = service.updateTeam(mappedTeam);
        TeamDto mappedTeamDto = mapper.mapToTeamDto(savedTeam);
        return ResponseEntity.ok(mappedTeamDto);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteTeam(@PathVariable Long id) throws ElementNotFoundException {
        service.deleteTeam(id);
        return ResponseEntity.ok().build();
    }
}
