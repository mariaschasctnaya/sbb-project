package com.tsystems.train.repository;

import com.tsystems.train.entity.EntityStatus;
import com.tsystems.train.entity.Station;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StationRepository extends JpaRepository<Station, String> {

    Station findByName(String name);

    List<Station> findByNameIsLike(String name);

    @Modifying
    @Query("UPDATE Station st SET st.status = ?2 WHERE st.id = ?1")
    void updateStatus(String id, EntityStatus entityStatus);
}
