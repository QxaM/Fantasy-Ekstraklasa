package com.kodilla.fantasy.scheduler;

import com.kodilla.fantasy.service.DataFetchingService;
import com.kodilla.fantasy.service.ServiceDataDbService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ScoreFetchingScheduler {

    private final DataFetchingService dataService;
    private final ServiceDataDbService dbService;

    @Scheduled(cron = "0 0 21 ? * MON")
    public void fetchScores() {
        int round = dbService.getLatestRound();
        dataService.addScores(round);
        dbService.saveRound(round);
    }
}
