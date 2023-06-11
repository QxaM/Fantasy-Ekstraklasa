package com.kodilla.fantasy.livescore.mapper;

import com.kodilla.fantasy.domain.Player;
import com.kodilla.fantasy.domain.Team;
import com.kodilla.fantasy.domain.exception.ElementNotFoundException;
import com.kodilla.fantasy.livescore.domain.Event;
import com.kodilla.fantasy.livescore.domain.Match;
import com.kodilla.fantasy.livescore.domain.dto.*;
import com.kodilla.fantasy.livescore.domain.exception.CouldNotMapElement;
import com.kodilla.fantasy.service.PlayerDbService;
import com.kodilla.fantasy.service.TeamDbService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class LiveScoreMapper {

    private final TeamDbService teamDbService;
    private final PlayerDbService playerDbService;

    public Match mapToMatch(MatchDto matchDto) throws CouldNotMapElement {
        Team team1 = findTeam(matchDto.getTeam1().getName());
        Team team2 = findTeam(matchDto.getTeam2().getName());
        return new Match(
                matchDto.getMatchId(),
                team1,
                team2,
                new ArrayList<>(),
                new ArrayList<>(),
                new ArrayList<>()
        );
    }

    public List<Match> mapToMatchList(GetMatchesDto matchDtoList) throws CouldNotMapElement {
        List<Match> matches = new ArrayList<>();
        for(MatchDto matchDto: matchDtoList.getMatches()) {
            Match match = mapToMatch(matchDto);
            matches.add(match);
        }
        return matches;
    }

    public void mapLineup(Match match, GetLineupsDto lineupsDto) {
        List<LiveScorePlayerDto> players1 = lineupsDto.getData().getTeam1().getPlayers();
        List<LiveScorePlayerDto> players2 = lineupsDto.getData().getTeam2().getPlayers();

        populateLineup(match, players1, match.getTeam1().getId());
        populateLineup(match, players2, match.getTeam2().getId());
    }

    public void mapEvents(Match match, GetEventsDto eventsDto) {
        for(EventDataDto eventDto: eventsDto.getEvents()) {
            if(eventDto.getEvents().isEmpty()) {
                addPlayer(match, eventDto);
            } else {
                eventDto.getEvents()
                        .forEach(nestedEventDto -> addPlayer(match, nestedEventDto));
            }
        }
    }

    private void addPlayer(Match match, EventDto eventDto) {
        Player player = new Player();
        try {
            if(eventDto.getTeam() == 1) {
                player = findPlayer(match.getLineup1(), eventDto.getPlayerName());
            } else {
                player = findPlayer(match.getLineup2(), eventDto.getPlayerName());
            }
        } catch (CouldNotMapElement e) {
            log.error(e.getMessage());
        }

        Event event = new Event(
                eventDto.getEvent(),
                player);
        match.getEvents().add(event);
    }

    private Team findTeam(String name) throws CouldNotMapElement {
        List<Team> teams = teamDbService.getTeams();

        return teams.stream()
                .filter(team -> name.contains(team.getName()))
                .findFirst()
                    .orElseThrow(() -> new CouldNotMapElement("Couldn't map team " + name));
    }

    private Player findPlayer(List<Player> players, String name) throws CouldNotMapElement {
        return players.stream()
                .filter(player -> {
                    String playerName = player.getFirstname() + " " + player.getLastname();
                    return playerName.equals(name);
                })
                .findFirst().orElseThrow(() -> new CouldNotMapElement("Couldn't map player " + name));
    }

    private void populateLineup(Match match, List<LiveScorePlayerDto> players, Long teamId) {
        for(LiveScorePlayerDto player: players) {
            try {
                Player foundPlayer = playerDbService
                        .getPlayerByFirstnameAndLastname(player.getFirstName(),
                                                        player.getLastName(),
                                                        teamId);
                if(match.getTeam1().getId().equals(teamId)) {
                    match.getLineup1().add(foundPlayer);
                } else if (match.getTeam2().getId().equals(teamId)) {
                    match.getLineup2().add(foundPlayer);
                }
            } catch (ElementNotFoundException e) {
                log.error(e.getMessage());
            }
        }
    }
}
