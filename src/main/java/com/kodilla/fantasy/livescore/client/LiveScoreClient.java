package com.kodilla.fantasy.livescore.client;

import com.kodilla.fantasy.livescore.config.LiveScoreConfig;
import com.kodilla.fantasy.livescore.domain.dto.GetLineupsDto;
import com.kodilla.fantasy.livescore.domain.dto.GetMatchesDto;
import com.kodilla.fantasy.livescore.domain.exception.NoResponseException;
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
            log.info("Started fetching matches");
            ResponseEntity<GetMatchesDto> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    headersEntity,
                    GetMatchesDto.class);
            log.info("Fetched matches");
            return Optional.ofNullable(response.getBody())
                    .orElse(new GetMatchesDto(Collections.emptyList()));
        } catch (RestClientException e) {
            log.error("Error fetching matches: " + e.getMessage());
            return new GetMatchesDto(Collections.emptyList());
        }
    }

    public GetLineupsDto fetchLineups(String matchId) throws NoResponseException {
        URI url = buildLineupsUrl(matchId);
        HttpEntity<Void> headersEntity = buildHeaders();

        try {
            log.info("Started fetching lineups");
            ResponseEntity<GetLineupsDto> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    headersEntity,
                    GetLineupsDto.class);
            log.info("Fetched lineups");
            return Optional.ofNullable(response.getBody())
                    .orElseThrow(() -> new NoResponseException("No response for fetching lineups"));
        } catch (RestClientException e) {
            log.error("Error fetching team: " + e.getMessage());
            throw new NoResponseException("Exception fetching lineups");
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

    private URI buildLineupsUrl(String matchId) {
        return UriComponentsBuilder.fromHttpUrl(config.getUrl() + "/match-lineups")
                .queryParam("match_id", matchId)
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
