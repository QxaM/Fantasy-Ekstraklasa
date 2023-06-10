package com.kodilla.fantasy.livescore.client;

import com.kodilla.fantasy.livescore.config.LiveScoreConfig;
import com.kodilla.fantasy.livescore.domain.dto.GetMatchesDto;
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
public class LiveScoreClient {

    private final RestTemplate restTemplate;
    private final LiveScoreConfig config;

    public GetMatchesDto fetchMatches(int round) {
        URI url = buildMatchesUrl(round);
        HttpEntity<Void> headersEntity = buildHeaders();

        try {
            log.info("Started fetching teams");
            ResponseEntity<GetMatchesDto> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    headersEntity,
                    GetMatchesDto.class);
            log.info("Fetched matches");
            return Optional.ofNullable(response.getBody())
                    .orElse(new GetMatchesDto(Collections.emptyList()));
        } catch (RestClientException e) {
            log.error("Error fetching teams: " + e.getMessage());
            return new GetMatchesDto(Collections.emptyList());
        }
    }

    private URI buildMatchesUrl(int round) {
        return UriComponentsBuilder.fromHttpUrl(config.getUrl() + "/matches-by-league")
                .queryParam("country_code", config.getCountryCode())
                .queryParam("league_code", config.getLeagueCode())
                .queryParam("round", round)
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
