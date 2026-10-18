package com.step.planB_planner.model.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

/**
 * @author Daria Pevets
 **/
public record RoutineRequestDto(

        @NotBlank
        String name,

        String description,

        @NotNull
        LocalDateTime startTime,

        @NotNull
        LocalDateTime endTime
) {

}
