package com.kodilla.fantasy.validator;

import com.kodilla.fantasy.livescore.domain.EventType;
import org.springframework.stereotype.Service;

@Service
public class EventTypeValidator {

    public EventType validateEvent(String event) {
        return switch (event) {
            case "RED_CARD" -> EventType.RED_CARD;
            case "YELLOW_CARD" -> EventType.YELLOW_CARD;
            case "GOAL" -> EventType.GOAL;
            case "GOAL_ASSIST" -> EventType.GOAL_ASSIST;
            default -> EventType.UNKNOWN_EVENT;
        };
    }
}
