package com.kodilla.fantasy.decorator;

public class AssistPoints extends PlayerDecorator {

    public AssistPoints(PlayerValues playerValue) {
        super(playerValue);
    }

    @Override
    public int getPoints(int points) {
        return super.getPoints(points) + super.getRules().getAssist();
    }
}
