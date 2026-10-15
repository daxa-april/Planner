package com.step.planB_planner.model.dto.request;

import jakarta.validation.constraints.NotBlank;

import java.time.LocalDateTime;

/**
 * @author Daria Pevets
 **/
public record RoutineRequestDto(

        @NotBlank
        String name,

        String description,

        @NotBlank
        LocalDateTime startTime,

        @NotBlank
        LocalDateTime endTime
) {

}
