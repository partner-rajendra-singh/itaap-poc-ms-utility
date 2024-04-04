package com.philips.itaap.utility.repository;

import com.philips.itaap.utility.entity.NonProd;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface NonProdRepo extends JpaRepository<NonProd, Integer> {

    @Query(value = "select * from poc.non_prod where build_number = :buildNumber and stage = :stage", nativeQuery = true)
    Optional<NonProd> findByStageAndBuildNumber(String buildNumber, Integer stage);
}
