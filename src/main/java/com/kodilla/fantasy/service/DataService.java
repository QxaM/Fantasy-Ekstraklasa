package com.kodilla.fantasy.service;

import com.kodilla.fantasy.apifootball.facade.ApiFootballFacade;
import com.kodilla.fantasy.decorator.DefaultPlayerDecorator;
import com.kodilla.fantasy.decorator.LineupPoints;
import com.kodilla.fantasy.decorator.PlayerValues;
import com.kodilla.fantasy.domain.Player;
import com.kodilla.fantasy.domain.Team;
import com.kodilla.fantasy.livescore.domain.Match;
import com.kodilla.fantasy.livescore.facade.LiveScoreFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class DataService {

    private final ApiFootballFacade apiFootballFacade;
    private final LiveScoreFacade liveScoreFacade;
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

    public void addScores() {
        List<Match> matches = liveScoreFacade.fetchMatches(1);
    }
}
