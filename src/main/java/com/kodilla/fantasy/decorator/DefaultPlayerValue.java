package com.kodilla.fantasy.decorator;

import java.math.BigDecimal;

public class DefaultPlayerValue implements PlayerValue {
    @Override
    public BigDecimal getValue() {
        return new BigDecimal("10000");
    }
}
