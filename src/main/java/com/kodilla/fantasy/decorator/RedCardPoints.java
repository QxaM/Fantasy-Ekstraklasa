package com.kodilla.fantasy.decorator;

public class RedCardPoints extends PlayerDecorator {

    public RedCardPoints(PlayerValues playerValue) {
        super(playerValue);
    }

    @Override
    public int getPoints(int points) {
        return super.getPoints(points) + super.getRules().getRedCard();
    }
}
