package com.kodilla.fantasy.mapper;

import com.kodilla.fantasy.domain.Player;
import com.kodilla.fantasy.domain.Squad;
import com.kodilla.fantasy.domain.dto.PageDto;
import com.kodilla.fantasy.domain.dto.PlayerDto;
import com.kodilla.fantasy.domain.dto.PlayersPagedDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PlayerMapper {

    private final TeamMapper teamMapper;

    public Player mapToPlayer(PlayerDto playerDto) {
        Player player = new Player.PlayerBuilder()
                .firstname(playerDto.getFirstname())
                .lastname(playerDto.getLastname())
                .age(playerDto.getAge())
                .value(playerDto.getValue())
                .position(playerDto.getPosition())
                .team(teamMapper.mapToTeam(playerDto.getTeam()))
                .build();

        player.setId(playerDto.getId());
        return player;
    }

    public Set<Player> mapToPlayerSet(List<PlayerDto> playerDtos) {
        return playerDtos.stream()
                .map(this::mapToPlayer)
                .collect(Collectors.toSet());
    }

    public PlayerDto mapToPlayerDto(Player player) {
        return new PlayerDto(
                player.getId(),
                player.getFirstname(),
                player.getLastname(),
                player.getAge(),
                player.getValue(),
                player.getPosition(),
                teamMapper.mapToTeamDto(player.getTeam()),
                player.getPoints()
        );
    }

    public List<PlayerDto> mapToPlayerDtoList(List<Player> players) {
        return players.stream()
                .map(this::mapToPlayerDto)
                .collect(Collectors.toList());
    }

    public PlayersPagedDto mapToPlayersPagedDto(Page<Player> players) {
        PageDto page = new PageDto(
                players.getNumber(),
                players.getTotalPages()
        );
        List<PlayerDto> mappedPlayers = mapToPlayerDtoList(players.getContent());

        return new PlayersPagedDto(
                page,
                mappedPlayers
        );
    }
}
