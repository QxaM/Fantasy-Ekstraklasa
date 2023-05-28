package com.kodilla.fantasy.scheduler;

import com.kodilla.fantasy.apifootball.facade.ApiFootballFacade;
import com.kodilla.fantasy.domain.Player;
import com.kodilla.fantasy.domain.Team;
import com.kodilla.fantasy.service.PlayerDbService;
import com.kodilla.fantasy.service.TeamDbService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class FetchingScheduler {

    private final ApiFootballFacade apiFootballFacade;
    private final TeamDbService teamDbService;
    private final PlayerDbService playerDbService;

    public void fetchTeams() {
        List<Team> teamList = apiFootballFacade.getAllTeams();
        teamDbService.initTeams(teamList);
    }

    @Scheduled(fixedDelay = 10000)
    public void fetchPlayers() {
        List<Player> playerList = apiFootballFacade.getAllPlayers();
        playerDbService.initPlayers(playerList);
    }
}
