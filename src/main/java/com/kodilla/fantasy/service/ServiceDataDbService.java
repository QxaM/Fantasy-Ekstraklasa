package com.kodilla.fantasy.service;

import com.kodilla.fantasy.domain.ServiceData;
import com.kodilla.fantasy.repository.ServiceDataRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ServiceDataDbService {

    private final ServiceDataRepository serviceDataRepository;

    public int getLatestRound() {
        List<ServiceData> serviceDataList = serviceDataRepository.findAll();

        return serviceDataList.stream()
                .mapToInt(ServiceData::getRound)
                .max().orElse(1);
    }

    public void saveRound(int round) {
        Integer nextRound = ++round;
        ServiceData serviceData = new ServiceData(nextRound);
        serviceDataRepository.save(serviceData);
    }
}
