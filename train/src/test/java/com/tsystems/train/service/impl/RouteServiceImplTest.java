package com.tsystems.train.service.impl;

import com.tsystems.train.entity.EntityStatus;
import com.tsystems.train.entity.Route;
import com.tsystems.train.entity.RouteEntry;
import com.tsystems.train.entity.Station;
import com.tsystems.train.repository.RouteRepository;
import com.tsystems.train.repository.StationRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;


import java.util.*;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class RouteServiceImplTest {
    @Mock
    private RouteRepository routeRepositoryMock;
    @Mock
    private StationRepository stationRepositoryMock;

    @InjectMocks
    private RouteServiceImpl routeServiceImpl;

    private Route route;
    private Station station;

    @Before public void setUp() {
        route = new Route();
        route.setId("1234");
        route.setStatus(EntityStatus.ACTIVE);

        station = new Station();
        station.setId("2345");
        station.setName("superStation");
        station.setStatus(EntityStatus.ACTIVE); }

    @Test
    public void createRouteTest() {
        routeServiceImpl.createRoute(route);
        verify(routeRepositoryMock, times(1)).save(route);
    }

    @Test
    public void getRoutesTest() {
        routeServiceImpl.getRoutes();
        verify(routeRepositoryMock, times(1)).findAll();
    }

    @Test
    public void getRouteByIdTest() {
        when(routeRepositoryMock.findById("1234")).thenReturn(Optional.ofNullable(route));
        Route mockRoute = routeServiceImpl.getRouteById("1234");
        assertEquals(route, mockRoute);
    }
    @Test
    public void archiveRouteTest(){
        routeServiceImpl.archive(route);
        verify(routeRepositoryMock,times(1)).updateStatus("1234",EntityStatus.ARCHIVED);
    }

    @Test
    public void getRoutesByStations(){
        Station departureStation = new Station();
        departureStation.setId("1234");
        departureStation.setName("departure");
        departureStation.setStatus(EntityStatus.ACTIVE);

        Station arriveStation = new Station();
        arriveStation.setId("4321");
        arriveStation.setName("arrive");
        arriveStation.setStatus(EntityStatus.ACTIVE);

        Station crossStation = new Station();
        crossStation.setId("123");
        crossStation.setName("CrossStation");
        crossStation.setStatus(EntityStatus.ACTIVE);

        RouteEntry routeEntry1 = new RouteEntry();
        routeEntry1.setId("1");
        routeEntry1.setRoute(route);
        routeEntry1.setStation(departureStation);

        RouteEntry routeEntry2 = new RouteEntry();
        routeEntry2.setId("2");
        routeEntry2.setRoute(route);
        routeEntry2.setStation(crossStation);

        RouteEntry routeEntry3 = new RouteEntry();
        routeEntry3.setId("3");
        routeEntry3.setRoute(route);
        routeEntry3.setStation(arriveStation);

        List<RouteEntry> routeEntryList = new LinkedList<>();
        routeEntryList.add(routeEntry1);
        routeEntryList.add(routeEntry2);
        routeEntryList.add(routeEntry3);

        route.setRouteEntries(routeEntryList);

        List<RouteEntry> wrongRouteEntryList = new LinkedList<>();
        wrongRouteEntryList.add(routeEntry2);
        wrongRouteEntryList.add(routeEntry3);
        wrongRouteEntryList.add(routeEntry1);

        Route wrongRoute = new Route();
        wrongRoute.setId("2");
        wrongRoute.setStatus(EntityStatus.ACTIVE);
        wrongRoute.setRouteEntries(wrongRouteEntryList);

        List<Route> fullRouteList = new ArrayList<>();
        fullRouteList.add(route);
        fullRouteList.add(wrongRoute);

        List<Route> exceptedRouteList = new ArrayList<>();
        exceptedRouteList.add(route);

        when(stationRepositoryMock.findByName(departureStation.getName())).thenReturn(departureStation);
        when(stationRepositoryMock.findByName(arriveStation.getName())).thenReturn(arriveStation);
        when(routeRepositoryMock.findByStations(departureStation, arriveStation)).thenReturn(fullRouteList);
        List<Route> actualRouteList = routeServiceImpl.getRoutesByStations("departure", "arrive");
        assertEquals(exceptedRouteList, actualRouteList);
    }

    @Test
    public void getRoutesByStationsNullTest(){
        when(stationRepositoryMock.findByName(station.getName())).thenReturn(station);
        List<Route> mockRoutes = routeServiceImpl.getRoutesByStations(station.getName(),null);
        assertEquals(Collections.emptyList(), mockRoutes);
    }

    @Test
    public void getRoutesByStation(){

        when(stationRepositoryMock.findByName(station.getName())).thenReturn(station);
        routeServiceImpl.getRoutesByStation(station.getName());
        verify(routeRepositoryMock, times(1)).findByStation(station);
    }

    @Test(expected = RuntimeException.class)
    public void getRoutesByNullStation(){
        routeServiceImpl.getRoutesByStation(null);
        verify(stationRepositoryMock, times(1)).findByName(null);
    }
}

