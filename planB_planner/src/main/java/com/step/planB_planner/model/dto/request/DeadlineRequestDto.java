package com.step.planB_planner.model.dto.request;

import jakarta.validation.constraints.NotBlank;

import java.time.LocalDateTime;

/**
 * @author Daria Pevets
 **/
public record DeadlineRequestDto(

        @NotBlank
        String name,

        String description,

        @NotBlank
        LocalDateTime whenGot,

        @NotBlank
        LocalDateTime deadLine
) {
}
