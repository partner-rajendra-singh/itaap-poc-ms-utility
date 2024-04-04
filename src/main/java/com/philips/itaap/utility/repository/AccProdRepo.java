package com.philips.itaap.utility.repository;

import com.philips.itaap.utility.entity.AccProd;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccProdRepo extends JpaRepository<AccProd, Integer> {

    @Query(value = "select * from poc.acc_prod where build_number = :buildNumber and stage = :stage", nativeQuery = true)
    Optional<AccProd> findByStageAndBuildNumber(String buildNumber, Integer stage);

    @Query(value = "select * from poc.acc_prod where build_number = :buildNumber and stage = :stage and deployment_id = :pipelineID", nativeQuery = true)
    Optional<AccProd> findByStageBuildNumberPipelineID(String buildNumber, Integer stage, Integer pipelineID);
}
