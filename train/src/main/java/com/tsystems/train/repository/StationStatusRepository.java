package com.tsystems.train.repository;

import com.tsystems.train.entity.StationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StationStatusRepository extends JpaRepository<StationStatus, String> {
}
