package com.philips.itaap.utility.repository;

import com.philips.itaap.utility.entity.ConnectivityHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ConnectivityHistoryRepo extends JpaRepository<ConnectivityHistory, Integer> {
    @Query("from ConnectivityHistory order by triggeredAt desc")
    List<ConnectivityHistory> findAllSorted();
}
