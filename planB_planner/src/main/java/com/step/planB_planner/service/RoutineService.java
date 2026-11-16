package com.step.planB_planner.service;

import com.step.planB_planner.model.constant.AppStatus;
import com.step.planB_planner.model.constant.TaskStatus;
import com.step.planB_planner.model.constant.WeekDays;
import com.step.planB_planner.model.dto.request.RoutineRequestDto;
import com.step.planB_planner.model.dto.response.RoutineResponseDto;
import org.springframework.data.domain.Page;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * @author Daria Pevets
 **/
public interface RoutineService {

    RoutineResponseDto createRoutine(RoutineRequestDto routineRequestDto);

    RoutineResponseDto updateRoutine(Long id, RoutineRequestDto routineRequestDto);

    TaskStatus updateRoutineStatus(Long id, TaskStatus taskStatus);

    AppStatus deleteRoutine(Long id);

    Page<RoutineResponseDto> getRoutinesByRange(LocalDateTime start, LocalDateTime end, int page, int pageSize);

    Page<RoutineResponseDto> getRoutineByDay(LocalDate date, int page, int pageSize);

    Page<RoutineResponseDto> getRoutineByDay(WeekDays weekDay, int page, int pageSize);

    Page<RoutineResponseDto> getRoutineByWeek(LocalDate date, int page, int pageSize);

    Page<RoutineResponseDto> getRoutineByMonth(LocalDate date, int page, int pageSize);
}
