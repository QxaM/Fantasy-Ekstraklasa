package com.kodilla.fantasy.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;

@Getter
public class RulesConfig {
    @Value("${rules.goal}")
    private int goal;
    @Value("${rules.assist}")
    private int assist;
    @Value("${rules.lineup}")
    private int lineup;
    @Value("${rules.yellow-card}")
    private int yellowCard;
    @Value("${rules.red-card}")
    private int redCard;
}
