package com.step.planB_planner.model.dto.response;

import com.step.planB_planner.constant.TaskStatus;

import java.time.LocalDateTime;

/**
 * @author Daria Pevets
 **/
public record RoutineResponseDto(
        Long id,
        String name,
        String description,
        LocalDateTime startTime,
        LocalDateTime endTime,
        TaskStatus status
) {
}
