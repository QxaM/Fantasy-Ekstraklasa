package com.kodilla.fantasy.mapper;

import com.kodilla.fantasy.domain.Squad;
import com.kodilla.fantasy.domain.User;
import com.kodilla.fantasy.domain.dto.CreateUserDto;
import com.kodilla.fantasy.domain.dto.UserDto;
import com.kodilla.fantasy.domain.dto.UserInLeagueDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserMapper {

    private final SquadMapper squadMapper;

    public User mapToUser(UserDto userDto) {
        User user = User.builder()
                .username(userDto.getUsername())
                .email(userDto.getEmail())
                .leagues(new ArrayList<>())
                .squad(new Squad())
                .build();
        user.setId(userDto.getId());
        return user;
    }

    public User mapToUser(CreateUserDto createUserDto) {
        return User.builder()
                .username(createUserDto.getUsername())
                .email(createUserDto.getEmail())
                .leagues(new ArrayList<>())
                .squad(new Squad())
                .build();
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
                user.getSquad().getName(),
                user.getPoints()
        );
    }

    public List<UserInLeagueDto> mapToUserInLeagueDtoList(List<User> users) {
        return users.stream()
                .map(this::mapToUserInLeagueDto)
                .collect(Collectors.toList());
    }
}
