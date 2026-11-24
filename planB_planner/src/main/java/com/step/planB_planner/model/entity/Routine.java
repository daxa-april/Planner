package com.step.planB_planner.model.entity;

import com.step.planB_planner.model.constant.TaskStatus;
import com.step.planB_planner.model.constant.WeekDays;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * @author Daria Pevets
 **/
@Entity
@Getter
@Setter
@RequiredArgsConstructor
public class Routine {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String description;
    private LocalDateTime startTime;
    private LocalDateTime endTime;

    @Enumerated(EnumType.STRING)
    private WeekDays weekDay;

    @Enumerated(EnumType.STRING)
    private TaskStatus status;
    private boolean notified = false;

}
