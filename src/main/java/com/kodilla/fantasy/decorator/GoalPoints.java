package com.kodilla.fantasy.decorator;

public class GoalPoints extends PlayerDecorator {

    private final static int GOAL = 3;

    public GoalPoints(PlayerValues playerValue) {
        super(playerValue);
    }

    @Override
    public int getPoints(int points) {
        return super.getPoints(points) + GOAL;
    }
}
