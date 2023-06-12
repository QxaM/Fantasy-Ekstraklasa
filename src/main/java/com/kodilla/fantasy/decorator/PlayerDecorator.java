package com.kodilla.fantasy.decorator;

import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;

@RequiredArgsConstructor
public abstract class PlayerDecorator extends DefaultPlayerDecorator {

    private final PlayerValues playerValue;

    @Override
    public BigDecimal getValue() {
        return playerValue.getValue();
    }

    @Override
    public int getPoints(int points) {
        return playerValue.getPoints(points);
    }
}
