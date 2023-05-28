package com.kodilla.fantasy.scheduler;

import com.kodilla.fantasy.apifootball.facade.ApiFootballFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class FetchingScheduler {

    private final ApiFootballFacade apiFootballFacade;

    @Scheduled(fixedRate = 10000)
    public void fetchTeams() {
        apiFootballFacade.getAllTeams();
    }
}
