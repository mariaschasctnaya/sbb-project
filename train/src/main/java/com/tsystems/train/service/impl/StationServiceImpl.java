package com.tsystems.train.service.impl;

import com.tsystems.train.entity.EntityStatus;
import com.tsystems.train.entity.Station;
import com.tsystems.train.repository.StationRepository;
import com.tsystems.train.service.StationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
public class StationServiceImpl implements StationService {

    @Autowired
    private StationRepository stationRepository;

    @Override
    @Transactional
    public void createStation(Station station) {
        log.debug("StationService: {} save to repository", station.getName());
        stationRepository.save(station);
        log.debug("StationService: {} successfully saved to repository", station.getName());
    }

    @Override
    public List<Station> getStations(String station) {
        if (station == null) {
            log.debug("StationService: request all stations from repository");
            return stationRepository.findAll();
        } else {
            log.debug("StationService: request stations like {} from repository", station);
            return stationRepository.findByNameIsLike(station);
        }
    }

    @Override
    public Station getStationByName(String name) {
        log.debug("StationService: request station {} from repository", name);
        return stationRepository.findByName(name);
    }

    @Override
    public Station getStationById(String id) {
        return stationRepository.findById(id).orElse(null);
    }

    @Override
    @Transactional
    public void archive(Station station) {
        stationRepository.updateStatus(station.getId(), EntityStatus.ARCHIVED);
    }
}
