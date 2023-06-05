package com.kodilla.fantasy.decorator;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class PlayerValueByScore extends PlayerValueDecorator {

    private final static BigDecimal LOW_VALUE = BigDecimal.ZERO;
    private final static BigDecimal HIGH_VALUE = new BigDecimal("5000000");
    private final static double LOW_SCORE = 6.0;
    private final static double HIGH_SCORE = 8.0;


    private final String score;

    public PlayerValueByScore(PlayerValue playerValue, String score) {
        super(playerValue);
        this.score = score;
    }

    @Override
    public BigDecimal getValue() {
        BigDecimal defaultValue = super.getValue();
        return defaultValue.add(calculateValue(score));
    }

    private BigDecimal calculateValue(String score) {
        double convertedScore = Double.parseDouble(score);
        BigDecimal valueDifference = HIGH_VALUE.subtract(LOW_VALUE);
        double scoreDifference = HIGH_SCORE - LOW_SCORE;
        BigDecimal valueToScore = valueDifference.divide(BigDecimal.valueOf(scoreDifference), RoundingMode.HALF_EVEN);
        return valueToScore.multiply(BigDecimal.valueOf(convertedScore - LOW_SCORE))
                .subtract(LOW_VALUE);
    }
}
