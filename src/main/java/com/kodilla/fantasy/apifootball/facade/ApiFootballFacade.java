package com.kodilla.fantasy.apifootball.facade;

import com.kodilla.fantasy.apifootball.client.ApiFootballClient;
import com.kodilla.fantasy.apifootball.dto.GetPlayersDto;
import com.kodilla.fantasy.apifootball.dto.GetTeamsDto;
import com.kodilla.fantasy.apifootball.dto.TeamResponseDto;
import com.kodilla.fantasy.domain.Player;
import com.kodilla.fantasy.domain.Team;
import com.kodilla.fantasy.mapper.ApiFootballMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Slf4j
public class ApiFootballFacade {

    private final ApiFootballClient apiFootballClient;
    private final ApiFootballMapper apiFootballMapper;

    public List<Team> getAllTeams() {
        GetTeamsDto apiFootballResponse = apiFootballClient.fetchTeams();
        return apiFootballResponse.getTeamResponse().stream()
                .map(TeamResponseDto::getTeam)
                .map(apiFootballMapper::mapToTeam)
                .collect(Collectors.toList());
    }

    public List<Player> getAllPlayers() {
        int paging = 1;
        List<GetPlayersDto> playersResponse = playerPaging(paging, new ArrayList<>());
        return playersResponse.stream()
                .flatMap((players) -> players.getResponse().stream())
                .map(apiFootballMapper::mapToPlayer)
                .collect(Collectors.toList());
    }

    private List<GetPlayersDto> playerPaging(int paging, List<GetPlayersDto> playersList) {
        GetPlayersDto apiFootballResponse = apiFootballClient.fetchPlayers(paging);
        playersList.add(apiFootballResponse);

        if(apiFootballResponse.getPaging().getCurrent() < apiFootballResponse.getPaging().getTotal()) {
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                log.error("Waiting interrupted " + e.getMessage());
            }
            paging++;
            playerPaging(paging, playersList);
        }
        return playersList;
    }
}
