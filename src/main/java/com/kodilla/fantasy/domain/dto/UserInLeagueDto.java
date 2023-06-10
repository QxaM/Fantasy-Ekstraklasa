package com.kodilla.fantasy.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserInLeagueDto {
    private Long id;
    private String username;
    private String squadName;
}
