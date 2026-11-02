package com.step.planB_planner.repository;

import com.step.planB_planner.model.entity.Task;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * @author Daria Pevets
 **/
public interface TaskRepository extends JpaRepository<Task, Long> {

    Optional<Task> findTaskById(Long id);

    Optional<Task> findTaskByName(String name);

    Optional<Task> findTaskByTime(LocalDateTime time);

    Page<Task> findTaskByTimeBetween(LocalDateTime start, LocalDateTime end, Pageable pageable);

    @Query("SELECT t FROM Task t " +
            "WHERE t.notified = false AND t.time BETWEEN :startWindow AND :endWindow")
    List<Task> findTasksForNotification(
            @Param("startWindow") LocalDateTime startWindow,
            @Param("endWindow") LocalDateTime endWindow
    );
}
