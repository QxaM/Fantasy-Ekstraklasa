package com.kodilla.fantasy.apifootball.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Getter
public class ApiFootballConfig {
    @Value("${api.football.url}")
    private String url;
    @Value("${api.football.key.header-name}")
    private String keyHeader;
    @Value("${api.football.host.header-name}")
    private String hostHeader;
    @Value("${api.football.key}")
    private String key;
    @Value("${api.football.host}")
    private String host;
    @Value("${api.football.ekstraklasa.id}")
    private String league;
    @Value("${api.football.season}")
    private String season;
}
