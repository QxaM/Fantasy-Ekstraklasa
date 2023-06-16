package com.kodilla.fantasy.service;

import com.kodilla.fantasy.domain.ServiceData;
import com.kodilla.fantasy.repository.ServiceDataRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ServiceDataDbServiceTests {

    @InjectMocks
    private ServiceDataDbService service;
    @Mock
    private ServiceDataRepository serviceDataRepository;

    @Test
    void shouldFetchLatestRound() {
        //Given
        ServiceData serviceData1 = new ServiceData(1L, 1);
        ServiceData serviceData2 = new ServiceData(2L, 5);
        ServiceData serviceData3 = new ServiceData(3L, 4);

        when(serviceDataRepository.findAll()).thenReturn(List.of(serviceData1, serviceData2, serviceData3));

        //When
        int foundRound = service.getLatestRound();

        //Then
        assertEquals(5, foundRound);
    }

    @Test
    void shouldNotFetchLatestRound() {
        //Given
        when(serviceDataRepository.findAll()).thenReturn(Collections.emptyList());

        //When
        int foundRound = service.getLatestRound();

        //Then
        assertEquals(1, foundRound);
    }

    @Test
    void shouldSaveRound() {
        //Given
        int round = 5;

        //When
        service.saveRound(round);

        //Then
        verify(serviceDataRepository, times(1)).save(any(ServiceData.class));
    }
}
