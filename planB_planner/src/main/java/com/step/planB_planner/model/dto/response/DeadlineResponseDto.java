package com.step.planB_planner.model.dto.response;

import com.step.planB_planner.constant.TaskStatus;

import java.time.LocalDateTime;

/**
 * @author Daria Pevets
 **/

public record DeadlineResponseDto(
        Long id,
        String name,
        String description,
        LocalDateTime whenGot,
        LocalDateTime theEnd,
        TaskStatus status
) {
}
