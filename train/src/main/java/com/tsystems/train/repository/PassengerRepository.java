package com.tsystems.train.repository;

import com.tsystems.train.entity.Passenger;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface PassengerRepository extends JpaRepository<Passenger, String> {
    Passenger findByNameAndSurnameAndBirthday(String name, String surname, LocalDate birthday);
}
