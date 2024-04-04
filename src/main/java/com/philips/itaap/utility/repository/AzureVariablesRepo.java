package com.philips.itaap.utility.repository;

import com.philips.itaap.utility.entity.AzureVariablesEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;


@Repository
@Transactional
public interface AzureVariablesRepo extends JpaRepository<AzureVariablesEntity, Integer> {

    List<AzureVariablesEntity> findByApprover(String approver);

    List<AzureVariablesEntity> findByRequester(String requester);

    @Modifying
    @Query(value = "update poc.AZURE_VARIABLES_REQUEST set STATUS = :status where ID = :id", nativeQuery = true)
    int updateStatusById(int id, String status);

}
