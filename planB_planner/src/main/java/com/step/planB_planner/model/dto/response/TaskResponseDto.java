package com.step.planB_planner.model.dto.response;

import com.step.planB_planner.constant.TaskStatus;

import java.time.LocalDateTime;

/**
 * @author Daria Pevets
 **/
public record TaskResponseDto(
        Long id,
        String name,
        String description,
        LocalDateTime time,
        TaskStatus status
) {
}
