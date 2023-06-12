package com.kodilla.fantasy.livescore.facade;

import com.kodilla.fantasy.livescore.client.LiveScoreClient;
import com.kodilla.fantasy.livescore.domain.Match;
import com.kodilla.fantasy.livescore.domain.dto.GetEventsDto;
import com.kodilla.fantasy.livescore.domain.dto.GetLineupsDto;
import com.kodilla.fantasy.livescore.domain.dto.GetMatchesDto;
import com.kodilla.fantasy.livescore.domain.exception.CouldNotMapElement;
import com.kodilla.fantasy.livescore.domain.exception.NoResponseException;
import com.kodilla.fantasy.livescore.mapper.LiveScoreMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class LiveScoreFacade {

    private final LiveScoreClient client;
    private final LiveScoreMapper mapper;

    public List<Match> fetchMatches(int round) {
        List<Match> matches = new ArrayList<>();
        try {
            matches = findMatches(round);
        } catch (CouldNotMapElement e) {
            log.error(e.getMessage());
        }

        matches.forEach((match -> {
            try {
                addLineup(match);
            } catch (NoResponseException e) {
                log.error(e.getMessage());
            }})
        );

        matches.forEach(this::addEvents);

        return matches;
    }

    public List<Match> findMatches(int round) throws CouldNotMapElement {
        GetMatchesDto fetchedMatches = client.fetchMatches(round);
        try{
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            log.error(e.getMessage());
        }
        return mapper.mapToMatchList(fetchedMatches);
    }

    public void addLineup(Match match) throws NoResponseException {
        GetLineupsDto fetchedLineups = client.fetchLineups(match.getMatchId());
        try{
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            log.error(e.getMessage());
        }
        mapper.mapLineup(match, fetchedLineups);
    }

    public void addEvents(Match match) {
        GetEventsDto fetchedEvents = client.fetchEvents(match.getMatchId());
        try{
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            log.error(e.getMessage());
        }
        mapper.mapEvents(match, fetchedEvents);
    }
}
