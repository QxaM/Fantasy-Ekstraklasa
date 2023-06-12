package com.kodilla.fantasy.decorator;

public class RedCardPoints extends PlayerDecorator {

    private final int RED_CARD = -3;

    public RedCardPoints(PlayerValues playerValue) {
        super(playerValue);
    }

    @Override
    public int getPoints(int points) {
        return super.getPoints(points) + RED_CARD;
    }
}
