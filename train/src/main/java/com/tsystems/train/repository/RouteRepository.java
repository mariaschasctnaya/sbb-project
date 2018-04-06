package com.tsystems.train.repository;

import com.tsystems.train.entity.EntityStatus;
import com.tsystems.train.entity.Route;
import com.tsystems.train.entity.Station;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RouteRepository extends JpaRepository<Route, String> {
    @Query("SELECT r_e.route FROM RouteEntry r_e WHERE r_e.station IN ?1 GROUP BY r_e.route HAVING COUNT (r_e.route) > 1")
    List<Route> findByStations(Station... stations);

    @Query("SELECT r_e.route FROM RouteEntry r_e WHERE r_e.station = ?1")
    List<Route> findByStation(Station station);

    @Modifying
    @Query("UPDATE Route st SET st.status = ?2 WHERE st.id = ?1")
    void updateStatus(String id, EntityStatus entityStatus);
}

