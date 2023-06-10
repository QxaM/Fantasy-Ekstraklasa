package com.kodilla.fantasy.livescore.domain;

import com.kodilla.fantasy.domain.Team;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Match {
    private String matchId;
    private Team team1;
    private Team team2;
}
