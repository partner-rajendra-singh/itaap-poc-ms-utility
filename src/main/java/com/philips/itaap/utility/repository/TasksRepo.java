package com.philips.itaap.utility.repository;

import com.philips.itaap.utility.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TasksRepo extends JpaRepository<Task, Integer> {

    @Query(value = "select * " +
            "from poc.tasks t " +
            "where t.task_title = :taskTitle " +
            "and t.microservice_id = :microserviceID " +
            "and t.user_id = :userID", nativeQuery = true)
    Optional<Task> findExistingTask(Integer microserviceID, Integer userID, String taskTitle);
}
