package com.kodilla.fantasy.service;

import com.kodilla.fantasy.apifootball.facade.ApiFootballFacade;
import com.kodilla.fantasy.decorator.DefaultPlayerDecorator;
import com.kodilla.fantasy.decorator.PlayerValues;
import com.kodilla.fantasy.domain.Player;
import com.kodilla.fantasy.domain.Team;
import com.kodilla.fantasy.factory.PlayerDecoratorFactory;
import com.kodilla.fantasy.livescore.domain.EventType;
import com.kodilla.fantasy.livescore.domain.Match;
import com.kodilla.fantasy.livescore.facade.LiveScoreFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class DataFetchingService {

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

    public void addScores(int round) {
        List<Match> matches = liveScoreFacade.fetchMatches(round);

        List<Player> players = fetchPlayerScores(matches);

        playerDbService.saveAllPlayers(players);
    }

    public List<Player> fetchPlayerScores(List<Match> matches) {
        return matches.stream()
                .map(Match::getEvents)
                .map(Map::entrySet)
                .flatMap(Collection::stream)
                .map(entry -> {
                    PlayerValues decorator = new DefaultPlayerDecorator();
                    for (EventType eventType : entry.getValue()) {
                        decorator = PlayerDecoratorFactory.makeDecorator(decorator, eventType);
                    }
                    Player player = entry.getKey();
                    player.addPoints(decorator);
                    return player;
                })
                .toList();
    }
}
