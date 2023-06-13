package com.kodilla.fantasy.livescore.mapper;

import com.kodilla.fantasy.domain.Player;
import com.kodilla.fantasy.domain.Team;
import com.kodilla.fantasy.livescore.domain.EventType;
import com.kodilla.fantasy.livescore.domain.Match;
import com.kodilla.fantasy.livescore.domain.dto.*;
import com.kodilla.fantasy.livescore.domain.exception.CouldNotMapElement;
import com.kodilla.fantasy.service.PlayerDbService;
import com.kodilla.fantasy.service.TeamDbService;
import com.kodilla.fantasy.validator.EventTypeValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class LiveScoreMapper {

    private final TeamDbService teamDbService;
    private final PlayerDbService playerDbService;
    private final EventTypeValidator eventValidator;

    public Match mapToMatch(MatchDto matchDto, List<Team> teams) throws CouldNotMapElement {
        Team team1 = findTeam(matchDto.getTeam1().getName(), teams);
        Team team2 = findTeam(matchDto.getTeam2().getName(), teams);
        return new Match(
                matchDto.getMatchId(),
                team1,
                team2,
                new HashMap<>()
        );
    }

    public List<Match> mapToMatchList(GetMatchesDto matchDtoList) throws CouldNotMapElement {
        List<Team> teams = teamDbService.getTeams();
        List<Match> matches = new ArrayList<>();
        for(MatchDto matchDto: matchDtoList.getMatches()) {
            Match match = mapToMatch(matchDto, teams);
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
            if(eventDto.getEvents() == null) {
                addEvent(match, eventDto);
            } else {
                eventDto.getEvents()
                        .forEach(nestedEventDto -> addEvent(match, nestedEventDto));
            }
        }
    }

    private void addEvent(Match match, EventDto eventDto) {
        Optional<Player> player;

        List<String> names = List.of(eventDto.getPlayerName().split(" "));
        List<Player> players;

        if(eventDto.getTeam() == 1) {
            players = match.getTeam1().getPlayers();
        } else {
            players = match.getTeam2().getPlayers();
        }

        if(names.size() == 1) {
            player = findPlayer(players, null, names.get(0));
        } else {
            player = findPlayer(players, names.get(0), names.get(1));
        }
        if(player.isPresent()) {
            EventType eventType = eventValidator.validateEvent(eventDto.getEvent());
            match.addEvent(player.get(), eventType);
        } else {
            log.error("Could not map player: " + eventDto.getPlayerName());
        }
    }

    private Team findTeam(String name, List<Team> teams) throws CouldNotMapElement {
        return teams.stream()
                .filter(team -> name.contains(team.getName()) || team.getName().contains(name))
                .findFirst()
                    .orElseThrow(() -> new CouldNotMapElement("Couldn't map team " + name));
    }

    private Optional<Player> findPlayer(Collection<Player> players, String firstName, String lastName) {
        Optional<Player> foundPlayer;
        if(firstName != null) {
            foundPlayer = players.stream()
                    .filter(actualPlayer ->
                            actualPlayer.getFirstname().contains(firstName)
                                    && actualPlayer.getLastname().contains(lastName))
                    .findFirst();
        } else {
            foundPlayer = players.stream().
                    filter(actualPlayer ->
                            actualPlayer.getName().contains(lastName))
                    .findFirst();
        }
        return foundPlayer;
    }

    private void populateLineup(Match match, List<LiveScorePlayerDto> players, Long teamId) {
        List<Player> foundPlayers = playerDbService.getPlayerByTeamId(teamId);

        for(LiveScorePlayerDto player: players) {

            Optional<Player> foundPlayer = findPlayer(foundPlayers, player.getFirstName(), player.getLastName());

            if(foundPlayer.isPresent()) {
                match.addEvent(foundPlayer.get(), EventType.LINEUP);
            } else {
                log.error("Could not map player: " + player.getFirstName() + " " + player.getLastName());
            }
        }
    }
}
