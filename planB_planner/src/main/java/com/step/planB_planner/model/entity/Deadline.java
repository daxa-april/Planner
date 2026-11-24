package com.step.planB_planner.model.entity;

import com.step.planB_planner.model.constant.TaskStatus;
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
public class Deadline {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String description;
    private LocalDateTime whenGot;
    private LocalDateTime theEnd;

    @Enumerated(EnumType.STRING)
    private TaskStatus status;
    private boolean notified = false;
}
