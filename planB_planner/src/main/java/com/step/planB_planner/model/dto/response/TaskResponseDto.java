package com.step.planB_planner.model.dto.response;

import java.time.LocalDateTime;

/**
 * @author Daria Pevets
 **/
public record TaskResponseDto(
        Long id,
        String name,
        String description,
        LocalDateTime time,
        boolean completed
) {
}
