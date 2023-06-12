package com.kodilla.fantasy.factory;

import com.kodilla.fantasy.decorator.*;
import com.kodilla.fantasy.livescore.domain.EventType;

public class PlayerDecoratorFactory {

    public static PlayerValues makeDecorator(PlayerValues playerValues, EventType event) {
        return switch (event) {
            case GOAL -> new GoalPoints(playerValues);
            case GOAL_ASSIST -> new AssistPoints(playerValues);
            case YELLOW_CARD -> new YellowCardPoints(playerValues);
            case RED_CARD -> new RedCardPoints(playerValues);
            case LINEUP -> new LineupPoints(playerValues);
            case UNKNOWN_EVENT -> new DefaultPlayerDecorator();
        };
    }

    public static PlayerValues makeLineupDecorator(PlayerValues playerValues) {
        return new LineupPoints(playerValues);
    }
}
