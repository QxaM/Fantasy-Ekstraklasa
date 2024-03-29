package com.kodilla.fantasy.decorator;

public class LineupPoints extends PlayerDecorator {

    private final static int LINEUP = 1;

    public LineupPoints(PlayerValues playerValue) {
        super(playerValue);
    }

    @Override
    public int getPoints(int points) {
        return super.getPoints(points) + LINEUP;
    }
}
