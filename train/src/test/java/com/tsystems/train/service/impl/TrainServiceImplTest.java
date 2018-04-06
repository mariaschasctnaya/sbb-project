package com.tsystems.train.service.impl;

import com.tsystems.train.entity.*;
import com.tsystems.train.exception.TrainIsOffException;
import com.tsystems.train.repository.TrainRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatcher;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.argThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@RunWith(MockitoJUnitRunner.class)
public class TrainServiceImplTest {
    @Mock
    private TrainRepository trainRepositoryMock;
    @Mock
    private RouteServiceImpl routeServiceImplMock;

    @InjectMocks
    private TrainServiceImpl trainServiceImpl;

    private Train train;
    private Route route;

    @Before
    public void setUp() {
        train = new Train();
        train.setId("12345");
        train.setNumber("525");
    }

    @Test
    public void createTrainServiceTest() {
        trainServiceImpl.createTrain(train);
        verify(trainRepositoryMock, times(1)).save(train);
    }

    @Test
    public void getTrainByNumberTest() {
        when(trainRepositoryMock.findByNumber("525")).thenReturn(train);
        Train mockTrain = trainServiceImpl.getTrainByNumber("525");
        assertEquals(train, mockTrain);
    }

    @Test
    public void getTrainByIdTest(){
        when(trainRepositoryMock.findById("12345")).thenReturn(Optional.ofNullable(train));
        Train mockTrain = trainServiceImpl.getTrainById("12345");
        assertEquals(train,mockTrain);
    }

    @Test
    public void archiveTrainTest(){
        trainServiceImpl.archive(train);
        verify(trainRepositoryMock,times(1)).updateStatus("12345", EntityStatus.ARCHIVED);
    }

    @Test
    public void archiveTrainByRouteTest(){
        trainServiceImpl.archiveTrainByRoute(route);
        verify(trainRepositoryMock,times(1)).updateStatusByRoute(route,EntityStatus.ARCHIVED);
    }
    @Test
    public void getTrainsByNullStationTest(){
        trainServiceImpl.getTrains(null);
        verify(trainRepositoryMock, times(1)).findAll();
    }

    @Test
    public void getTrainsTest(){
        trainServiceImpl.getTrains("Station name");
        when(routeServiceImplMock.getRoutesByStation("Station Name")).thenReturn(Collections.emptyList());
        verify(trainRepositoryMock,times(1)).findByRouteIn(Collections.emptyList());
    }

    @Test
    public void searchTrainTest(){
        Station stationA = new Station();
        stationA .setId("1234");
        stationA .setName("StationA");
        stationA .setStatus(EntityStatus.ACTIVE);

        Station stationB = new Station();
        stationB .setId("5678");
        stationB .setName("StationB");
        stationB .setStatus(EntityStatus.ACTIVE);

        Schedule schedule1 = new Schedule();
        schedule1.setId("999");
        schedule1.setDeparture(LocalDateTime.now());
        schedule1.setArrival(LocalDateTime.now().plusDays(1));

        Schedule schedule2 = new Schedule();
        schedule2.setId("888");
        schedule2.setDeparture(LocalDateTime.now().plusMinutes(1));
        schedule2.setArrival(LocalDateTime.now().plusDays(1));

        Schedule schedule3 = new Schedule();
        schedule3.setId("777");
        schedule3.setDeparture(LocalDateTime.now().plusMinutes(5));
        schedule3.setArrival(LocalDateTime.now().plusDays(1));

        Schedule schedule4 = new Schedule();
        schedule4.setId("666");
        schedule4.setDeparture(LocalDateTime.now().plusMinutes(10));
        schedule4.setArrival(LocalDateTime.now().plusDays(1));

        RouteEntry routeEntry1 = new RouteEntry();
        routeEntry1.setId("1212");
        routeEntry1.setRoute(route);
        routeEntry1.setStation(stationA);
        routeEntry1.setSchedule(schedule1);

        RouteEntry routeEntry2 = new RouteEntry();
        routeEntry2.setId("1313");
        routeEntry2.setRoute(route);
        routeEntry2.setStation(stationB);
        routeEntry2.setSchedule(schedule2);

        RouteEntry routeEntry3 = new RouteEntry();
        routeEntry3.setId("1414");
        routeEntry3.setRoute(route);
        routeEntry3.setStation(stationA);
        routeEntry3.setSchedule(schedule3);

        RouteEntry routeEntry4 = new RouteEntry();
        routeEntry4.setId("1515");
        routeEntry4.setRoute(route);
        routeEntry4.setStation(stationB);
        routeEntry4.setSchedule(schedule4);


        List<RouteEntry> routeEntryList1 = new LinkedList<>();
        routeEntryList1.add(routeEntry1);
        routeEntryList1.add(routeEntry2);

        List<RouteEntry> routeEntryList2 = new LinkedList<>();
        routeEntryList2.add(routeEntry2);
        routeEntryList2.add(routeEntry3);

        List<RouteEntry> routeEntryList3 = new LinkedList<>();
        routeEntryList3.add(routeEntry3);
        routeEntryList3.add(routeEntry4);

        Route routeA = new Route();
        routeA.setId("111");
        routeA.setStatus(EntityStatus.ACTIVE);
        routeA.setRouteEntries(routeEntryList1);

        Route routeB = new Route();
        routeB.setId("222");
        routeB.setStatus(EntityStatus.ACTIVE);
        routeB.setRouteEntries(routeEntryList2);

        Route routeC = new Route();
        routeC.setId("333");
        routeC.setStatus(EntityStatus.ACTIVE);
        routeC.setRouteEntries(routeEntryList3);

        when(routeServiceImplMock.getRoutesByStations(stationA.getName(),stationB.getName())).thenReturn(Arrays.asList(routeA,routeB,routeC));
        trainServiceImpl.searchTrain(stationA.getName(),stationB.getName(),LocalDateTime.now(), LocalDateTime.now().plusMinutes(100));
        verify(trainRepositoryMock).findByRouteIn(argThat(new RouteMatcher("333")));

    }
    private class RouteMatcher extends ArgumentMatcher<List<Route>> {

        private final List<String> routeIds;

        RouteMatcher(String ...routeIds) {

            this.routeIds = Arrays.asList(routeIds);
        }

        @Override
        public boolean matches(Object o) {
            return routeIds.size() == ((List<Route>) o).size() && ((List<Route>) o).stream().map(Route::getId).allMatch(routeIds::contains);
        }
    }
    @Test(expected = TrainIsOffException.class)
    public void checkAvailableTrainByStationTest(){
        Station stationA = new Station();
        stationA.setId("11");
        stationA.setName("stationA");
        stationA.setStatus(EntityStatus.ACTIVE);

        Station stationB = new Station();
        stationB.setId("12");
        stationB.setName("stationB");
        stationB.setStatus(EntityStatus.ACTIVE);

        Station stationC = new Station();
        stationC.setId("13");
        stationC.setName("stationC");
        stationC.setStatus(EntityStatus.ACTIVE);

        Station  stationD = new Station();
        stationD.setId("14");
        stationD.setName("stationD");
        stationD.setStatus(EntityStatus.ACTIVE);

        Schedule schedule1 = new Schedule();
        schedule1.setId("1");
        schedule1.setDeparture(LocalDateTime.now().minusMinutes(5));
        schedule1.setArrival(LocalDateTime.now().plusDays(1));

        Schedule schedule2 = new Schedule();
        schedule2.setId("2");
        schedule2.setDeparture(LocalDateTime.now().plusMinutes(10));
        schedule2.setArrival(LocalDateTime.now().plusDays(1));

        Schedule schedule3 =new Schedule();
        schedule3.setId("3");
        schedule3.setDeparture(LocalDateTime.now().plusMinutes(15));
        schedule3.setArrival(LocalDateTime.now().plusDays(1));

        Schedule schedule4 = new Schedule();
        schedule4.setDeparture(LocalDateTime.now().plusMinutes(20));
        schedule4.setArrival(LocalDateTime.now().plusDays(1));

        RouteEntry routeEntry1 = new RouteEntry();
        routeEntry1.setId("11");
        routeEntry1.setRoute(route);
        routeEntry1.setStation(stationA);
        routeEntry1.setSchedule(schedule1);

        RouteEntry routeEntry2 = new RouteEntry();
        routeEntry2.setId("22");
        routeEntry2.setRoute(route);
        routeEntry2.setStation(stationB);
        routeEntry2.setSchedule(schedule2);

        RouteEntry routeEntry3 = new RouteEntry();
        routeEntry3.setId("33");
        routeEntry3.setRoute(route);
        routeEntry3.setStation(stationC);
        routeEntry3.setSchedule(schedule3);

        RouteEntry routeEntry4 = new RouteEntry();
        routeEntry4.setId("44");
        routeEntry4.setRoute(route);
        routeEntry4.setStation(stationD);
        routeEntry4.setSchedule(schedule4);

        List<RouteEntry>  routeEntriesList = new LinkedList<>();
        routeEntriesList.add(routeEntry1);
        routeEntriesList.add(routeEntry2);
        routeEntriesList.add(routeEntry3);
        routeEntriesList.add(routeEntry4);

        Route route = new Route();
        route.setId("1111");
        route.setRouteEntries(routeEntriesList);
        route.setStatus(EntityStatus.ACTIVE);

        Train train1 = new Train();
        train1.setId("4444");
        train1.setNumber("525");
        train1.setPlaces(5);
        train1.setRoute(route);
        train1.setStatus(EntityStatus.ACTIVE);

        trainServiceImpl.checkAvailableTrainByStation(train1,stationA.getName());
    }
}




