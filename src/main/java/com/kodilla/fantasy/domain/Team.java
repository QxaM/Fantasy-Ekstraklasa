package com.kodilla.fantasy.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Team {
    private Long id;
    private Long apiFootballId;
    private String name;
    private String code;

    public Team(Long apiFootballId, String name, String code) {
        this.apiFootballId = apiFootballId;
        this.name = name;
        this.code = code;
    }
}
