package com.step.planB_planner.service;

import com.step.planB_planner.constant.AppStatus;
import com.step.planB_planner.constant.TaskStatus;
import com.step.planB_planner.model.dto.request.RoutineRequestDto;
import com.step.planB_planner.model.dto.response.RoutineResponseDto;

/**
 * @author Daria Pevets
 **/
public interface RoutineService {

    RoutineResponseDto createRoutine(RoutineRequestDto routineRequestDto);

    RoutineResponseDto updateRoutine(Long id, RoutineRequestDto routineRequestDto);

    TaskStatus updateRoutineStatus(Long id, TaskStatus taskStatus);

    AppStatus deleteRoutine(Long id);
}
