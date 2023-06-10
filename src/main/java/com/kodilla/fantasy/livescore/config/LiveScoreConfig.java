package com.kodilla.fantasy.livescore.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Getter
public class LiveScoreConfig {
    @Value("${livescore.url}")
    private String url;
    @Value("${livescore.key.header-name}")
    private String keyHeader;
    @Value("${livescore.host.header-name}")
    private String hostHeader;
    @Value("${livescore.key}")
    private String key;
    @Value("${livescore.host}")
    private String host;
    @Value("${livescore.country-code}")
    private String countryCode;
    @Value("${livescore.league-code}")
    private String leagueCode;
}
