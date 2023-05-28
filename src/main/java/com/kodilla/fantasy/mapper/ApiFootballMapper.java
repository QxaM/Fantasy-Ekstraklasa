package com.kodilla.fantasy.mapper;

import com.kodilla.fantasy.apifootball.dto.ApiFootballTeamDto;
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
}
