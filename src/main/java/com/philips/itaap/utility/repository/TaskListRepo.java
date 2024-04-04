package com.philips.itaap.utility.repository;

import com.philips.itaap.utility.entity.TaskList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskListRepo extends JpaRepository<TaskList, Integer> {

}
