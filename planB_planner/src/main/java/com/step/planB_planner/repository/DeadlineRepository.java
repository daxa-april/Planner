package com.step.planB_planner.repository;

import com.step.planB_planner.model.entity.Deadline;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * @author Daria Pevets
 **/
public interface DeadlineRepository extends JpaRepository<Deadline, Long> {

    Optional<Deadline> getDeadlineById(Long id);
}
