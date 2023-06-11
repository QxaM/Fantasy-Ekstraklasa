package com.kodilla.fantasy.decorator;

public class YellowCardPoints extends PlayerDecorator {

    public YellowCardPoints(PlayerValues playerValue) {
        super(playerValue);
    }

    @Override
    public int getPoints(int points) {
        return super.getPoints(points) + getRules().getYellowCard();
    }
}
