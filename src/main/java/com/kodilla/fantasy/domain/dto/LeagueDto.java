package com.kodilla.fantasy.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class LeagueDto {
    private Long id;
    private String name;
    private List<UserDto> users;
}
