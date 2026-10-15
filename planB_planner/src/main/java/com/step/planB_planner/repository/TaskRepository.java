package com.step.planB_planner.repository;

import com.step.planB_planner.model.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Daria Pevets
 **/
public interface TaskRepository extends JpaRepository<Task, Long> {

}
