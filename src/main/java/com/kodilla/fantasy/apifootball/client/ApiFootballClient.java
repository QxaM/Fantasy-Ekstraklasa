package com.kodilla.fantasy.apifootball.client;

import com.kodilla.fantasy.apifootball.config.ApiFootballConfig;
import com.kodilla.fantasy.apifootball.dto.*;
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
import java.util.Collections;
import java.util.Optional;

@Component
@Slf4j
@RequiredArgsConstructor
public class ApiFootballClient {

    private final RestTemplate restTemplate;
    private final ApiFootballConfig config;

    public GetTeamsDto fetchTeams() {
        URI url = buildTeamsUrl();
        HttpEntity<Void> headersEntity = buildHeaders();

        try {
            log.info("Started fetching teams");
            ResponseEntity<GetTeamsDto> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    headersEntity,
                    GetTeamsDto.class);
            log.info("Fetched teams");
            return Optional.ofNullable(response.getBody())
                    .orElse(new GetTeamsDto(Collections.emptyList()));
        } catch (RestClientException e) {
            log.error("Error fetching teams: " + e.getMessage());
            return new GetTeamsDto(Collections.emptyList());
        }
    }

    public GetPlayersDto fetchPlayers(int paging) {
        URI url = buildPlayersUrl(paging);
        HttpEntity<Void> headersEntity = buildHeaders();

        try {
            log.info("Started fetching players at page " + paging);
            ResponseEntity<GetPlayersDto> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    headersEntity,
                    GetPlayersDto.class);
            log.info("Finished fetching players at page " + paging);
            if(Optional.ofNullable(response.getBody()).isPresent()) {
                return response.getBody();
            } else {
                log.error("Response with empty body");
            }
            return Optional.ofNullable(response.getBody())
                    .orElse(new GetPlayersDto(
                                new PagingDto(paging, paging),
                                Collections.emptyList()));


        } catch (RestClientException e) {
            log.error("Error fetching players at page: " + paging + " " + e.getMessage());
            return new GetPlayersDto(
                    new PagingDto(paging, paging),
                    Collections.emptyList()
            );
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

    private URI buildPlayersUrl(int paging) {
        return UriComponentsBuilder.fromHttpUrl(config.getUrl() + "/players")
                .queryParam("league", config.getLeague())
                .queryParam("season", config.getSeason())
                .queryParam("page", paging)
                .build()
                .encode()
                .toUri();
    }

    private HttpEntity<Void> buildHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.set(config.getKeyHeader(), config.getKey());
        headers.set(config.getHostHeader(), config.getHost());
        return new HttpEntity<>(headers);
    }
}
