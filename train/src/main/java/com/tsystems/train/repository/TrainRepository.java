package com.tsystems.train.repository;

import com.tsystems.train.entity.EntityStatus;
import com.tsystems.train.entity.Route;
import com.tsystems.train.entity.Train;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface TrainRepository extends JpaRepository<Train,String> {
    Train findByNumber(String number);
    List<Train> findByRouteIn(Collection<Route> routes);
    @Modifying
    @Query("UPDATE Train st SET st.status = ?2 WHERE st.route = ?1")
    void updateStatusByRoute(Route route, EntityStatus entityStatus);
    @Modifying
    @Query("UPDATE Train st SET st.status = ?2 WHERE st.id = ?1")
    void updateStatus(String id, EntityStatus entityStatus);
}
