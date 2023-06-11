package com.kodilla.fantasy.livescore.facade;

import com.kodilla.fantasy.livescore.client.LiveScoreClient;
import com.kodilla.fantasy.livescore.domain.Match;
import com.kodilla.fantasy.livescore.domain.dto.GetLineupsDto;
import com.kodilla.fantasy.livescore.domain.dto.GetMatchesDto;
import com.kodilla.fantasy.livescore.domain.exception.CouldNotMapTeam;
import com.kodilla.fantasy.livescore.domain.exception.NoResponseException;
import com.kodilla.fantasy.livescore.mapper.LiveScoreMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class LiveScoreFacade {

    private final LiveScoreClient client;
    private final LiveScoreMapper mapper;

    public List<Match> fetchMatches(int round) throws CouldNotMapTeam {
        List<Match> matches;

        GetMatchesDto fetchedMatches = client.fetchMatches(round);
        matches = mapper.mapToMatchList(fetchedMatches);



        return matches;
    }

    public List<Match> findMatches(int round) throws CouldNotMapTeam {
        GetMatchesDto fetchedMatches = client.fetchMatches(round);
        try{
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            log.error(e.getMessage());
        }
        return mapper.mapToMatchList(fetchedMatches);
    }

    public Match addLineup(Match match, String matchId) throws NoResponseException {
        GetLineupsDto fetchedLineups = client.fetchLineups(matchId);
        try{
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            log.error(e.getMessage());
        }
        return mapper.mapLineup(match, fetchedLineups);
    }
}
