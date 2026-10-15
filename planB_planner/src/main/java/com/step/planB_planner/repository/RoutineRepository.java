package com.step.planB_planner.repository;

import com.step.planB_planner.model.entity.Routine;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Daria Pevets
 **/
public interface RoutineRepository extends JpaRepository<Routine, Long> {
}
