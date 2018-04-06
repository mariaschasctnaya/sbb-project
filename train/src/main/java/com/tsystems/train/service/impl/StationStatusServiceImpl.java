package com.tsystems.train.service.impl;

import com.tsystems.train.entity.StationStatus;
import com.tsystems.train.repository.StationStatusRepository;
import com.tsystems.train.service.StationStatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StationStatusServiceImpl implements StationStatusService {

    @Autowired
    private StationStatusRepository stationStatusRepository;

    @Override
    public void update(StationStatus status) {
        stationStatusRepository.save(status);
    }
}
