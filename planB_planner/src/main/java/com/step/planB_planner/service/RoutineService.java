package com.step.planB_planner.service;

import com.step.planB_planner.model.constant.AppStatus;
import com.step.planB_planner.model.constant.TaskStatus;
import com.step.planB_planner.model.dto.request.RoutineRequestDto;
import com.step.planB_planner.model.dto.response.RoutineResponseDto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author Daria Pevets
 **/
public interface RoutineService {

    RoutineResponseDto createRoutine(RoutineRequestDto routineRequestDto);

    RoutineResponseDto updateRoutine(Long id, RoutineRequestDto routineRequestDto);

    TaskStatus updateRoutineStatus(Long id, TaskStatus taskStatus);

    AppStatus deleteRoutine(Long id);

    List<RoutineResponseDto> getRoutinesByRange(LocalDateTime start, LocalDateTime end);

    List<RoutineResponseDto> getRoutineByDay(LocalDate date);

    List<RoutineResponseDto> getRoutineByWeek(LocalDate date);

    List<RoutineResponseDto> getRoutineByMonth(LocalDate date);
}
