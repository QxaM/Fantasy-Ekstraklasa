package com.kodilla.fantasy.controller;

import com.kodilla.fantasy.service.DataService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("service")
@RequiredArgsConstructor
public class ServiceController {

    private final DataService dataInitializer;

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
}
