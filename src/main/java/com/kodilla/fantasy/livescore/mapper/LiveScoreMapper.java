package com.kodilla.fantasy.livescore.mapper;

import com.kodilla.fantasy.domain.Player;
import com.kodilla.fantasy.domain.Team;
import com.kodilla.fantasy.domain.exception.ElementNotFoundException;
import com.kodilla.fantasy.livescore.domain.Match;
import com.kodilla.fantasy.livescore.domain.dto.GetLineupsDto;
import com.kodilla.fantasy.livescore.domain.dto.GetMatchesDto;
import com.kodilla.fantasy.livescore.domain.dto.LiveScorePlayerDto;
import com.kodilla.fantasy.livescore.domain.dto.MatchDto;
import com.kodilla.fantasy.livescore.domain.exception.CouldNotMapTeam;
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

    public Match mapToMatch(MatchDto matchDto) throws CouldNotMapTeam {
        Team team1 = findTeam(matchDto.getTeam1().getName());
        Team team2 = findTeam(matchDto.getTeam2().getName());
        return new Match(
                matchDto.getMatchId(),
                team1,
                team2,
                new ArrayList<>(),
                new ArrayList<>()
        );
    }

    public List<Match> mapToMatchList(GetMatchesDto matchDtoList) throws CouldNotMapTeam {
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

        //return match;
    }

    private Team findTeam(String name) throws CouldNotMapTeam {
        List<Team> teams = teamDbService.getTeams();

        return teams.stream()
                .filter(team -> name.contains(team.getName()))
                .findFirst().orElseThrow(() -> new CouldNotMapTeam("Couldn't map team " + name));
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
