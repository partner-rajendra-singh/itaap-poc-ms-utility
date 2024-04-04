package com.philips.itaap.utility.repository;

import com.philips.itaap.utility.entity.UserTask;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserTasksRepo extends JpaRepository<UserTask, Integer> {
    @Query(value = "select * from poc.users_assigned_tasks where task_id = :taskID and user_id = :userID", nativeQuery = true)
    Optional<UserTask> findByTaskAndUserId(Integer taskID, Integer userID);
}
