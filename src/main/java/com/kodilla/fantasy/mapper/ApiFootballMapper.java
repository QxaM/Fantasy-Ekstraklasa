package com.kodilla.fantasy.mapper;

import com.kodilla.fantasy.apifootball.dto.ApiFootballPlayerDto;
import com.kodilla.fantasy.apifootball.dto.ApiFootballTeamDto;
import com.kodilla.fantasy.apifootball.dto.PlayerResponseDto;
import com.kodilla.fantasy.domain.Player;
import com.kodilla.fantasy.domain.Team;
import org.springframework.stereotype.Service;

@Service
public class ApiFootballMapper {

    public Team mapToTeam(ApiFootballTeamDto apiFootballTeamDto) {
        return new Team(
                apiFootballTeamDto.getId(),
                apiFootballTeamDto.getName(),
                apiFootballTeamDto.getCode()
        );
    }

    public Player mapToPlayer(PlayerResponseDto playerResponseDto) {
        return new Player(
                playerResponseDto.getPlayer().getId(),
                playerResponseDto.getPlayer().getFirstname(),
                playerResponseDto.getPlayer().getLastname(),
                playerResponseDto.getPlayer().getAge(),
                playerResponseDto.getStatistics().getGames().getRating()
        );
    }
}
