package com.step.planB_planner.model.entity;

import com.step.planB_planner.constant.TaskStatus;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * @author Daria Pevets
 **/
@Setter
@Getter
@Entity
@RequiredArgsConstructor
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String description;
    private LocalDateTime time;
    private TaskStatus status;
}
