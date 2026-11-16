package com.step.planB_planner.model.dto.response;

import com.step.planB_planner.model.constant.TaskStatus;
import com.step.planB_planner.model.constant.WeekDays;

import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * @author Daria Pevets
 **/
public record RoutineResponseDto(
        Long id,
        String name,
        String description,
        LocalDateTime startTime,
        LocalDateTime endTime,
        WeekDays weekDay,
        TaskStatus status
) {
}
