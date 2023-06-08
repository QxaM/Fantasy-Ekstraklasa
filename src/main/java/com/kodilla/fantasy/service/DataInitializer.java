package com.kodilla.fantasy.service;

import com.kodilla.fantasy.apifootball.facade.ApiFootballFacade;
import com.kodilla.fantasy.domain.Player;
import com.kodilla.fantasy.domain.Team;
import com.kodilla.fantasy.domain.exception.ElementNotFoundException;
import com.kodilla.fantasy.service.PlayerDbService;
import com.kodilla.fantasy.service.TeamDbService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class DataInitializer {

    private final ApiFootballFacade apiFootballFacade;
    private final TeamDbService teamDbService;
    private final PlayerDbService playerDbService;

    public void fetchTeams() {
        List<Team> teamList = apiFootballFacade.getAllTeams();
        teamDbService.initTeams(teamList);
    }

    public void fetchPlayers() {
        List<Player> playerList = apiFootballFacade.getAllPlayers();
        playerDbService.initPlayers(playerList);
    }
}
