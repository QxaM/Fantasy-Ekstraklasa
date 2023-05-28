package com.kodilla.fantasy.apifootball.facade;

import com.kodilla.fantasy.apifootball.client.ApiFootballClient;
import com.kodilla.fantasy.apifootball.dto.GetTeamsDto;
import com.kodilla.fantasy.apifootball.dto.TeamResponseDto;
import com.kodilla.fantasy.domain.Team;
import com.kodilla.fantasy.mapper.ApiFootballMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ApiFootballFacade {

    private final ApiFootballClient apiFootballClient;
    private final ApiFootballMapper apiFootballMapper;

    public List<Team> getAllTeams() {
        GetTeamsDto apiFootballResponse = apiFootballClient.fetchTeams();
        return Arrays.stream(apiFootballResponse.getTeamResponse())
                .map(TeamResponseDto::getTeam)
                .map(apiFootballMapper::mapToTeam)
                .collect(Collectors.toList());
    }
}
