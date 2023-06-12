package com.kodilla.fantasy.livescore.domain;

import com.kodilla.fantasy.domain.Player;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Event {
    private Map<Player, List<EventType>> eventMap;

    public void addToMap(Player key, EventType value) {
        eventMap.computeIfAbsent(key, k -> new ArrayList<>()).add(value);
    }
}
