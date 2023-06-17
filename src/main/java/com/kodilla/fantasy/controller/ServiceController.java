package com.kodilla.fantasy.controller;

import com.kodilla.fantasy.domain.ServiceData;
import com.kodilla.fantasy.service.DataFetchingService;
import com.kodilla.fantasy.service.ServiceDataDbService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("service")
@RequiredArgsConstructor
public class ServiceController {

    private final DataFetchingService dataInitializer;
    private final ServiceDataDbService serviceDataDbService;

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

    @PutMapping("getScores")
    public ResponseEntity<Void> getScores() {
        int roundId = serviceDataDbService.getLatestRound();
        dataInitializer.addScores(roundId);
        serviceDataDbService.saveRound(roundId);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
