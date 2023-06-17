package com.kodilla.fantasy.livescore.domain;

import com.kodilla.fantasy.domain.Player;
import com.kodilla.fantasy.domain.Team;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class  Match {
    private String matchId;
    private Team team1;
    private Team team2;
    private Map<Player, List<EventType>> events;

    public void addEvent(Player key, EventType value) {
        events.computeIfAbsent(key, k -> new ArrayList<>()).add(value);
    }
}
