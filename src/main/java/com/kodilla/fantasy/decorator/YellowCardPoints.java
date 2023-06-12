package com.kodilla.fantasy.decorator;

public class YellowCardPoints extends PlayerDecorator {

    private final static int YELLOW_CARD = -1;

    public YellowCardPoints(PlayerValues playerValue) {
        super(playerValue);
    }

    @Override
    public int getPoints(int points) {
        return super.getPoints(points) + YELLOW_CARD;
    }
}
