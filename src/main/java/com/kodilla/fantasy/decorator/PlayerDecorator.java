package com.kodilla.fantasy.decorator;

import com.kodilla.fantasy.config.RulesConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;

@RequiredArgsConstructor
public abstract class PlayerDecorator extends DefaultPlayerDecorator {

    private final PlayerValues playerValue;
    private final RulesConfig rulesConfig = new RulesConfig();

    @Override
    public BigDecimal getValue() {
        return playerValue.getValue();
    }

    @Override
    public int getPoints(int points) {
        return playerValue.getPoints(points);
    }

    protected RulesConfig getRules() {
        return this.rulesConfig;
    }
}
