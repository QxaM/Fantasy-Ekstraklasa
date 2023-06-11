package com.kodilla.fantasy.livescore.domain;

import com.kodilla.fantasy.domain.Player;
import com.kodilla.fantasy.domain.Team;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Match {
    private String matchId;
    private Team team1;
    private Team team2;
    private List<Player> lineup1;
    private List<Player> lineup2;
    private List<Event> events;
}
