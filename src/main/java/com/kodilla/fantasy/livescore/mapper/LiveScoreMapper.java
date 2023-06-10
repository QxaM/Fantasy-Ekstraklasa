package com.kodilla.fantasy.livescore.mapper;

import com.kodilla.fantasy.domain.Team;
import com.kodilla.fantasy.livescore.domain.Match;
import com.kodilla.fantasy.livescore.domain.dto.MatchDto;
import com.kodilla.fantasy.livescore.domain.exception.CouldNotMapTeam;
import com.kodilla.fantasy.service.TeamDbService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

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

    private Team findTeam(String name) throws CouldNotMapTeam {
        List<Team> teams = teamDbService.getTeams();

        return teams.stream()
                .filter(team -> name.contains(team.getName()))
                .findFirst().orElseThrow(() -> new CouldNotMapTeam("Couldn't map team " + name));
    }
}
