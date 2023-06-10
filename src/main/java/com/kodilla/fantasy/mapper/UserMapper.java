package com.kodilla.fantasy.mapper;

import com.kodilla.fantasy.domain.User;
import com.kodilla.fantasy.domain.dto.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class UserMapper {

    private final SquadMapper squadMapper;

    public User mapToUser(UserDto userDto) {
        return new User(
                userDto.getId(),
                userDto.getUsername()
        );
    }

    public UserDto mapToUserDto(User user) {
        return new UserDto(
                user.getId(),
                user.getUsername(),
                squadMapper.mapToSquadDto(user.getSquad())
        );
    }
}
