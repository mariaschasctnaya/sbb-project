package com.tsystems.train.service.impl;

import com.tsystems.train.entity.Passenger;
import com.tsystems.train.repository.PassengerRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;


import java.time.LocalDate;


import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;


@RunWith(MockitoJUnitRunner.class)
public class PassengerServiceImplTest {
    @Mock
    private PassengerRepository passengerRepositoryMock;

    @InjectMocks
    private PassengerServiceImpl passengerServiceImpl;

    private Passenger passenger;

    @Before
    public void setUp() {
        passenger = new Passenger();
        passenger.setId("1234");
        passenger.setBirthday(LocalDate.now());
        passenger.setName("name");
        passenger.setSurname("surname");
    }

    @Test
    public void refreshPassengerTest() {
        passengerServiceImpl.refresh(passenger);
        verify(passengerRepositoryMock, times(1)).findByNameAndSurnameAndBirthday(anyString(), anyString(), any(LocalDate.class));

    }
}
