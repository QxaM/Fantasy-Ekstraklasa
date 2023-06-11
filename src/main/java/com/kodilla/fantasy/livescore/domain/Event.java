package com.kodilla.fantasy.livescore.domain;

import com.kodilla.fantasy.domain.Player;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Event {
    private EventType event;
    private Player player;
}
