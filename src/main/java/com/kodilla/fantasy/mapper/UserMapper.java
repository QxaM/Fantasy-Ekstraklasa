package com.kodilla.fantasy.mapper;

import com.kodilla.fantasy.domain.User;
import com.kodilla.fantasy.domain.dto.CreateUserDto;
import com.kodilla.fantasy.domain.dto.UserDto;
import com.kodilla.fantasy.domain.dto.UserInLeagueDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserMapper {

    private final SquadMapper squadMapper;

    public User mapToUser(UserDto userDto) {
        return new User(
                userDto.getId(),
                userDto.getUsername(),
                userDto.getEmail()
        );
    }

    public User mapToUser(CreateUserDto createUserDto) {
        return new User(
                createUserDto.getUsername(),
                createUserDto.getEmail()
        );
    }

    public UserDto mapToUserDto(User user) {
        return new UserDto(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                squadMapper.mapToSquadDto(user.getSquad()),
                user.getPoints()
        );
    }

    public UserInLeagueDto mapToUserInLeagueDto(User user) {
        return new UserInLeagueDto(
                user.getId(),
                user.getUsername(),
                user.getSquad().getName()
        );
    }

    public List<UserInLeagueDto> mapToUserInLeagueDtoList(List<User> users) {
        return users.stream()
                .map(this::mapToUserInLeagueDto)
                .collect(Collectors.toList());
    }
}
