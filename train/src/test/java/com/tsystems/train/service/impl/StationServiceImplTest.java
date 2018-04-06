package com.tsystems.train.service.impl;

import com.tsystems.train.entity.EntityStatus;
import com.tsystems.train.entity.Station;
import com.tsystems.train.repository.StationRepository;
import org.junit.*;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class StationServiceImplTest {
    @Mock
    private StationRepository stationRepositoryMock;

    @InjectMocks
    private StationServiceImpl stationServiceImpl;

    private Station station;

    @Before
    public void setUp() {
        station = new Station();
        station.setId("1234");
        station.setName("MockStation");
        station.setStatus(EntityStatus.ACTIVE);
    }

    @Test
    public void createStationTest() {
        stationServiceImpl.createStation(station);
        verify(stationRepositoryMock, times(1)).save(station);
    }

    @Test
    public void getStationByNameTest() {
        when(stationRepositoryMock.findByName("MockStation")).thenReturn(station);
        Station mockStation = stationServiceImpl.getStationByName("MockStation");
        assertEquals(station, mockStation);
    }

    @Test
    public void getStationByIdTest() {
        when(stationRepositoryMock.findById("1234")).thenReturn(Optional.ofNullable(station));
        Station mockStation = stationServiceImpl.getStationById("1234");
        assertEquals(station, mockStation);
    }

    @Test
    public void archiveTest() {
        stationServiceImpl.archive(station);
        verify(stationRepositoryMock, times(1)).updateStatus("1234", EntityStatus.ARCHIVED);
    }

    @Test
    public void getStationsListByNameTest() {
        stationServiceImpl.getStations("MockStation");
        verify(stationRepositoryMock, times(1)).findByNameIsLike("MockStation");
    }
    @Test
    public void getStationsListNullTest(){
        stationServiceImpl.getStations(null);
        verify(stationRepositoryMock,times(1)).findAll();
    }

}

