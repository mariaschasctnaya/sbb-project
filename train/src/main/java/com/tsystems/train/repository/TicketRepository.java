package com.tsystems.train.repository;

import com.tsystems.train.entity.Passenger;
import com.tsystems.train.entity.Ticket;
import com.tsystems.train.entity.Train;
import com.tsystems.train.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, String> {
    List<Ticket> findByTrain(Train train);
    boolean existsByTrainAndPassenger(Train train, Passenger passenger);
    int countAllByTrain(Train train);
    List<Ticket> findByUser(User user);
}
