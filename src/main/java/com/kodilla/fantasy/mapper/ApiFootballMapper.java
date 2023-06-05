package com.kodilla.fantasy.mapper;

import com.kodilla.fantasy.apifootball.dto.ApiFootballTeamDto;
import com.kodilla.fantasy.apifootball.dto.PlayerResponseDto;
import com.kodilla.fantasy.decorator.DefaultPlayerValue;
import com.kodilla.fantasy.decorator.PlayerValue;
import com.kodilla.fantasy.decorator.PlayerValueByScore;
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
        PlayerValue playerValue = new DefaultPlayerValue();
        String playerRating = playerResponseDto.getStatistics().get(0).getGames().getRating();
        if(playerRating != null) {
            playerValue = new PlayerValueByScore(playerValue, playerRating);
        }
        return new Player(
                playerResponseDto.getPlayer().getId(),
                playerResponseDto.getPlayer().getFirstname(),
                playerResponseDto.getPlayer().getLastname(),
                playerResponseDto.getPlayer().getAge(),
                playerValue.getValue(),
                validator.validatePosition(playerResponseDto.getStatistics().get(0).getGames().getPosition())
        );

    }
}
