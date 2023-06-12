package com.kodilla.fantasy.livescore.mapper;

import com.kodilla.fantasy.domain.Player;
import com.kodilla.fantasy.domain.Team;
import com.kodilla.fantasy.domain.exception.ElementNotFoundException;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

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
        Player player = new Player();

        try {
            player = findPlayer(match.getEvents().keySet(),
                                eventDto.getPlayerName());
        } catch (CouldNotMapElement e) {
            log.error(e.getMessage());
        }
        EventType eventType = eventValidator.validateEvent(eventDto.getEvent());

        match.addEvent(player, eventType);
    }

    private Team findTeam(String name, List<Team> teams) throws CouldNotMapElement {
        return teams.stream()
                .filter(team -> name.contains(team.getName()) || team.getName().contains(name))
                .findFirst()
                    .orElseThrow(() -> new CouldNotMapElement("Couldn't map team " + name));
    }

    private Player findPlayer(Set<Player> players, String name) throws CouldNotMapElement {
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
                        .getPlayerByFirstnameAndLastnameAndTeamId(player.getFirstName(),
                                                        player.getLastName(),
                                                        teamId);

                match.addEvent(foundPlayer, EventType.LINEUP);
            } catch (ElementNotFoundException e) {
                log.error(e.getMessage());
            }
        }
    }
}
