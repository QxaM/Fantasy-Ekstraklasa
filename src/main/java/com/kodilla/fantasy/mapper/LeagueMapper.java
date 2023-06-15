package com.kodilla.fantasy.mapper;

import com.kodilla.fantasy.domain.League;
import com.kodilla.fantasy.domain.dto.LeagueDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LeagueMapper {

    private final UserMapper userMapper;

    public League mapToLeague(LeagueDto leagueDto) {
        return new League(
                leagueDto.getId(),
                leagueDto.getName()
        );
    }

    public LeagueDto mapToLeagueDto(League league) {
        return new LeagueDto(
                league.getId(),
                league.getName(),
                userMapper.mapToUserInLeagueDtoList(league.getUsers())
        );
    }

    public List<LeagueDto> mapToLeagueDtoList(List<League> leagues) {
        return leagues.stream()
                .map(this::mapToLeagueDto)
                .collect(Collectors.toList());
    }
}
