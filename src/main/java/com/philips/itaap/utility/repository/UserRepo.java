package com.philips.itaap.utility.repository;

import com.philips.itaap.utility.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepo extends JpaRepository<User, Integer> {
    List<User> findByRole(String role);
    Optional<User> findByEmail(String email);
}
