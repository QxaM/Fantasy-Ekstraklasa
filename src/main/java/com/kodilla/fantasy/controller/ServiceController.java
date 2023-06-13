package com.kodilla.fantasy.controller;

import com.kodilla.fantasy.service.DataFetchingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("service")
@RequiredArgsConstructor
public class ServiceController {

    private final DataFetchingService dataInitializer;

    @PostMapping("init/players")
    public ResponseEntity<Void> initPlayers() {
        dataInitializer.fetchPlayers();
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("init/teams")
    public ResponseEntity<Void> initTeams() {
        dataInitializer.fetchTeams();
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping("getScores/{roundId}")
    public ResponseEntity<Void> getScores(@PathVariable Integer roundId) {
        dataInitializer.addScores(roundId);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
