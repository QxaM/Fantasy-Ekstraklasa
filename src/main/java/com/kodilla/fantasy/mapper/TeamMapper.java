package com.kodilla.fantasy.mapper;

import com.kodilla.fantasy.domain.Team;
import com.kodilla.fantasy.domain.dto.TeamDto;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TeamMapper {

    public Team mapToTeam(TeamDto teamDto) {
        return new Team(
                teamDto.getId(),
                0L,
                teamDto.getName(),
                teamDto.getCode(),
                new ArrayList<>()
        );
    }
    public TeamDto mapToTeamDto(Team team) {
        return new TeamDto(
                team.getId(),
                team.getName(),
                team.getCode()
        );
    }

    public List<TeamDto> mapToTeamDtoList(List<Team> teams) {
        return teams.stream()
                .map(this::mapToTeamDto)
                .collect(Collectors.toList());
    }
}
