package com.kodilla.fantasy.apifootball.client;

import com.kodilla.fantasy.apifootball.config.ApiFootballConfig;
import com.kodilla.fantasy.apifootball.dto.GetTeamsDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@Component
@Slf4j
@RequiredArgsConstructor
public class ApiFootballClient {

    private final RestTemplate restTemplate;
    private final ApiFootballConfig config;

    public GetTeamsDto fetchTeams() {
        URI url = buildTeamsUrl();
        HttpHeaders headers = new HttpHeaders();
        headers.set(config.getKeyHeader(), config.getKey());
        headers.set(config.getHostHeader(), config.getHost());

        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

        try {
            log.info("Started fetching teams");
            ResponseEntity<GetTeamsDto> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    requestEntity,
                    GetTeamsDto.class);
            log.info("Fetched teams");
            return response.getBody();
        } catch (RestClientException e) {
            log.error("Error fetching teams: " + e.getMessage());
            return new GetTeamsDto();
        }
    }

    private URI buildTeamsUrl() {
        return UriComponentsBuilder.fromHttpUrl(config.getUrl() + "/teams")
                .queryParam("league", config.getLeague())
                .queryParam("season", config.getSeason())
                .build()
                .encode()
                .toUri();
    }
}
