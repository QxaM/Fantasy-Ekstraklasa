package com.kodilla.fantasy.decorator;

import java.math.BigDecimal;

public class DefaultPlayerDecorator implements PlayerValues {
    @Override
    public BigDecimal getValue() {
        return new BigDecimal("10000");
    }

    @Override
    public int getPoints(int points) {
        return points;
    }
}
