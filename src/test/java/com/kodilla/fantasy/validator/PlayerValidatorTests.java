package com.kodilla.fantasy.validator;

import com.kodilla.fantasy.domain.Position;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class PlayerValidatorTests {

    private final static String GOALKEEPER = "Goalkeeper";
    private final static String DEFENDER = "Defender";
    private final static String MIDFIELDER = "Midfielder";
    private final static String ATTACKER = "Attacker";

    @Autowired
    private PlayerValidator playerValidator;

    @Test
    void testValidatePosition() {
        //Given

        //When
        Position GK = playerValidator.validatePosition(GOALKEEPER);
        Position DEF = playerValidator.validatePosition(DEFENDER);
        Position MID = playerValidator.validatePosition(MIDFIELDER);
        Position ST = playerValidator.validatePosition(ATTACKER);
        Position other = playerValidator.validatePosition("Test");

        //Then
        assertAll(() -> assertEquals(Position.GK, GK),
                () -> assertEquals(Position.DEF, DEF),
                () -> assertEquals(Position.MID, MID),
                () -> assertEquals(Position.ST, ST),
                () -> assertNull(other));
    }
}
