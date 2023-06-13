package com.kodilla.fantasy.repository;

import com.kodilla.fantasy.domain.ServiceData;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ServiceDataRepository extends CrudRepository<ServiceData, Long> {

    @Override
    List<ServiceData> findAll();
}
