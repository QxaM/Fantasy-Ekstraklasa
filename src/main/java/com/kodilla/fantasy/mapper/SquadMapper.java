package com.kodilla.fantasy.mapper;

import com.kodilla.fantasy.domain.Squad;
import com.kodilla.fantasy.domain.dto.SquadDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SquadMapper {

    private final PlayerMapper playerMapper;

    public Squad mapToSquad(SquadDto squadDto) {
        return new Squad(
                squadDto.getId(),
                squadDto.getName(),
                squadDto.getCurrentValue(),
                playerMapper.mapToPlayerList(squadDto.getPlayers())
        );
    }

    public SquadDto mapToSquadDto(Squad squad) {
        return new SquadDto(
                squad.getId(),
                squad.getName(),
                squad.getCurrentValue(),
                playerMapper.mapToPlayerDtoList(squad.getPlayers())
        );
    }
}
