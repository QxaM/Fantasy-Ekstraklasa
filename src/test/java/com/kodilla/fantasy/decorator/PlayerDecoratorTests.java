package com.kodilla.fantasy.decorator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PlayerDecoratorTests {

    private PlayerValues decorator;

    @BeforeEach
    void createDecorator() {
        decorator = new DefaultPlayerDecorator();
    }

    @Test
    void decorateAssist() {
        //Given
        decorator = new AssistPoints(decorator);

        //When
        int points = decorator.getPoints(5);

        //Then
        assertEquals(6, points);
    }

    @Test
    void decorateGoals() {
        //Given
        decorator = new GoalPoints(decorator);

        //When
        int points = decorator.getPoints(5);

        //Then
        assertEquals(8, points);
    }

    @Test
    void decoratorLineup() {
        //Given
        decorator = new LineupPoints(decorator);

        //When
        int points = decorator.getPoints(5);

        //Then
        assertEquals(6, points);
    }

    @Test
    void decoratorRedCard() {
        //Given
        decorator = new RedCardPoints(decorator);

        //When
        int points = decorator.getPoints(5);

        //Then
        assertEquals(2, points);
    }

    @Test
    void decoratorYellowCard() {
        //Given
        decorator = new YellowCardPoints(decorator);

        //When
        int points = decorator.getPoints(5);

        //Then
        assertEquals(4, points);
    }
}
