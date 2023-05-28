package com.kodilla.fantasy.validator;

import com.kodilla.fantasy.domain.Position;

public class PlayerValidator {

    public Position validatePosition(String position) {
        return switch (position) {
            case "Goalkeeper" -> Position.GK;
            case "Defender" -> Position.DEF;
            case "Midfielder" -> Position.MID;
            case "Attacker" -> Position.ST;
        };
    }
}
