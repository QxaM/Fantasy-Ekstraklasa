package com.kodilla.fantasy.scheduler;

import com.kodilla.fantasy.service.DataFetchingService;
import com.kodilla.fantasy.service.ServiceDataDbService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ScoreFetchingSchedulerTests {

    @InjectMocks
    private ScoreFetchingScheduler scheduler;
    @Mock
    private DataFetchingService dataService;
    @Mock
    private ServiceDataDbService dbService;

    @Test
    void shouldFetchScores() {
        //Given
        when(dbService.getLatestRound()).thenReturn(1);

        //When
        scheduler.fetchScores();

        //Then
        verify(dataService, times(1)).addScores(1);
        verify(dbService, times(1)).saveRound(1);
    }
}
