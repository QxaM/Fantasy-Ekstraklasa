package com.kodilla.fantasy.validator;

import com.kodilla.fantasy.livescore.domain.EventType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class EventTypeValidatorTests {

    private final static String RED_CARD = "RED_CARD";
    private final static String YELLOW_CARD = "YELLOW_CARD";
    private final static String GOAL = "GOAL";
    private final static String GOAL_ASSIST = "GOAL_ASSIST";

    @Autowired
    private EventTypeValidator validator;

    @Test
    void testValidateEvent() {
        //Given

        //When
        EventType redCard = validator.validateEvent(RED_CARD);
        EventType yellowCard = validator.validateEvent(YELLOW_CARD);
        EventType goal = validator.validateEvent(GOAL);
        EventType goalAssist = validator.validateEvent(GOAL_ASSIST);
        EventType def = validator.validateEvent("1234");

        //Then
        assertAll(() -> assertEquals(EventType.RED_CARD, redCard),
                () -> assertEquals(EventType.YELLOW_CARD, yellowCard),
                () -> assertEquals(EventType.GOAL, goal),
                () -> assertEquals(EventType.GOAL_ASSIST, goalAssist),
                () -> assertEquals(EventType.UNKNOWN_EVENT, def));
    }
}
