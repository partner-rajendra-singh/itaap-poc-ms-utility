package com.philips.itaap.utility.repository;

import com.philips.itaap.utility.entity.Deployment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DeploymentRepo extends JpaRepository<Deployment, Integer> {
}
