package com.philips.itaap.utility.repository;

import com.philips.itaap.utility.entity.Microservices;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MicroservicesRepo extends JpaRepository<Microservices, Integer> {
    Optional<Microservices> findByName(String name);
}
