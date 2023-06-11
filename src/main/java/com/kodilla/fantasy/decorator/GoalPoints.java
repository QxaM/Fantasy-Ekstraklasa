package com.kodilla.fantasy.decorator;

public class GoalPoints extends PlayerDecorator {

    public GoalPoints(PlayerValues playerValue) {
        super(playerValue);
    }

    @Override
    public int getPoints(int points) {
        return super.getPoints(points) + super.getRules().getGoal();
    }
}
