package com.kodilla.fantasy.decorator;

public class AssistPoints extends PlayerDecorator {

    private final static int ASSIST = 1;

    public AssistPoints(PlayerValues playerValue) {
        super(playerValue);
    }

    @Override
    public int getPoints(int points) {
        return super.getPoints(points) + ASSIST;
    }
}
