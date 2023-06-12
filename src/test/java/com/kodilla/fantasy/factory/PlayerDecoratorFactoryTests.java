package com.kodilla.fantasy.factory;

import com.kodilla.fantasy.decorator.*;
import com.kodilla.fantasy.livescore.domain.EventType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class PlayerDecoratorFactoryTests {

    @Test
    void testPlayerDecoratorFactory() {
        //Given
        PlayerValues playerValues = new DefaultPlayerDecorator();

        //When
        PlayerValues goal = PlayerDecoratorFactory.makeDecorator(playerValues, EventType.GOAL);
        PlayerValues goalAssist = PlayerDecoratorFactory.makeDecorator(playerValues, EventType.GOAL_ASSIST);
        PlayerValues yellowCard = PlayerDecoratorFactory.makeDecorator(playerValues, EventType.YELLOW_CARD);
        PlayerValues redCard = PlayerDecoratorFactory.makeDecorator(playerValues, EventType.RED_CARD);
        PlayerValues def = PlayerDecoratorFactory.makeDecorator(playerValues, EventType.UNKNOWN_EVENT);
        PlayerValues lineup = PlayerDecoratorFactory.makeDecorator(playerValues, EventType.LINEUP);

        //Then
        assertAll(() -> assertEquals(GoalPoints.class, goal.getClass()),
                () -> assertEquals(AssistPoints.class, goalAssist.getClass()),
                () -> assertEquals(YellowCardPoints.class, yellowCard.getClass()),
                () -> assertEquals(RedCardPoints.class, redCard.getClass()),
                () -> assertEquals(DefaultPlayerDecorator.class, def.getClass()),
                () -> assertEquals(LineupPoints.class, lineup.getClass()));
    }
}
