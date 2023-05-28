package com.kodilla.fantasy.mapper;

import com.kodilla.fantasy.apifootball.dto.ApiFootballTeamDto;
import com.kodilla.fantasy.apifootball.dto.PlayerResponseDto;
import com.kodilla.fantasy.domain.Player;
import com.kodilla.fantasy.domain.Team;
import com.kodilla.fantasy.validator.PlayerValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ApiFootballMapper {

    private final PlayerValidator validator;

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
                playerResponseDto.getStatistics()[0].getGames().getRating(),
                validator.validatePosition(playerResponseDto.getStatistics()[0].getGames().getPosition())
        );
    }
}
