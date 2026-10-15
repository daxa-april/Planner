package com.step.planB_planner.repository;

import com.step.planB_planner.model.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * @author Daria Pevets
 **/
public interface TaskRepository extends JpaRepository<Task, Long> {

    Optional<Task> findTaskById(Long id);

    Optional<Task> findTaskByName(String name);

    Optional<Task> findTaskByTime(LocalDateTime time);

    Optional<Task> findTaskByTimeBetween(LocalDateTime start, LocalDateTime end);
}
