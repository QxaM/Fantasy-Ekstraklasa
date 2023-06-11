package com.kodilla.fantasy.livescore.mapper;

import com.kodilla.fantasy.domain.Team;
import com.kodilla.fantasy.livescore.domain.Match;
import com.kodilla.fantasy.livescore.domain.dto.GetMatchesDto;
import com.kodilla.fantasy.livescore.domain.dto.MatchDto;
import com.kodilla.fantasy.livescore.domain.exception.CouldNotMapTeam;
import com.kodilla.fantasy.service.TeamDbService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LiveScoreMapper {

    private final TeamDbService teamDbService;

    public Match mapToMatch(MatchDto matchDto) throws CouldNotMapTeam {
        Team team1 = findTeam(matchDto.getTeam1().getName());
        Team team2 = findTeam(matchDto.getTeam2().getName());
        return new Match(
                matchDto.getMatchId(),
                team1,
                team2
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

    private Team findTeam(String name) throws CouldNotMapTeam {
        List<Team> teams = teamDbService.getTeams();

        return teams.stream()
                .filter(team -> name.contains(team.getName()))
                .findFirst().orElseThrow(() -> new CouldNotMapTeam("Couldn't map team " + name));
    }
}
