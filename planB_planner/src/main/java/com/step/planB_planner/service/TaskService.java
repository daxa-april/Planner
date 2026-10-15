package com.step.planB_planner.service;

import com.step.planB_planner.model.dto.request.TaskRequestDto;
import com.step.planB_planner.model.dto.response.TaskResponseDto;

/**
 * @author Daria Pevets
 **/
public interface TaskService {
    TaskResponseDto createTask(TaskRequestDto taskRequestDto);
}
