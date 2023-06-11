package com.kodilla.fantasy.mapper;

import com.kodilla.fantasy.apifootball.dto.ApiFootballTeamDto;
import com.kodilla.fantasy.apifootball.dto.PlayerResponseDto;
import com.kodilla.fantasy.decorator.DefaultPlayerDecorator;
import com.kodilla.fantasy.decorator.PlayerValues;
import com.kodilla.fantasy.decorator.PlayerValueByScore;
import com.kodilla.fantasy.domain.Player;
import com.kodilla.fantasy.domain.Team;
import com.kodilla.fantasy.service.TeamDbService;
import com.kodilla.fantasy.validator.PlayerValidator;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ApiFootballMapper {

    private final PlayerValidator validator;
    private final TeamDbService teamDbService;

    public Team mapToTeam(ApiFootballTeamDto apiFootballTeamDto) {
        return new Team(
                apiFootballTeamDto.getId(),
                StringUtils.stripAccents(apiFootballTeamDto.getName()),
                apiFootballTeamDto.getCode()
        );
    }

    public Player mapToPlayer(PlayerResponseDto playerResponseDto) {
        PlayerValues playerValue = new DefaultPlayerDecorator();
        String playerRating = playerResponseDto.getStatistics().get(0).getGames().getRating();
        if(playerRating != null) {
            playerValue = new PlayerValueByScore(playerValue, playerRating);
        }

        Long teamApiFootballId = playerResponseDto.getStatistics().get(0)
                                                    .getTeam().getId();
        Team foundTeam = teamDbService.getTeamByApiFootballId(teamApiFootballId);

        return new Player(
                playerResponseDto.getPlayer().getId(),
                StringUtils.stripAccents(playerResponseDto.getPlayer().getFirstname()),
                StringUtils.stripAccents(playerResponseDto.getPlayer().getLastname()),
                playerResponseDto.getPlayer().getAge(),
                playerValue.getValue(),
                validator.validatePosition(playerResponseDto.getStatistics().get(0).getGames().getPosition()),
                foundTeam
        );
    }
}
