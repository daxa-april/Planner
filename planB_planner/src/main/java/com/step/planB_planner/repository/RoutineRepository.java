package com.step.planB_planner.repository;

import com.step.planB_planner.model.constant.WeekDays;
import com.step.planB_planner.model.entity.Routine;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * @author Daria Pevets
 **/
public interface RoutineRepository extends JpaRepository<Routine, Long> {

    Optional<Routine> findRoutineById(Long id);

    Optional<Routine> findRoutineByName(String name);

    Optional<Routine> findRoutineByStartTime(LocalDateTime startTime, WeekDays weekDay);

    Page<Routine> findRoutineByStartTimeBetween(LocalDateTime start, LocalDateTime end, Pageable pageable);

    Page<Routine> findRoutinesByWeekDay(WeekDays weekDay, Pageable pageable);
}
