package com.kodilla.fantasy.decorator;

import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;

@RequiredArgsConstructor
public abstract class PlayerValueDecorator extends DefaultPlayerValue {

    private final PlayerValue playerValue;

    @Override
    public BigDecimal getValue() {
        return playerValue.getValue();
    }
}
