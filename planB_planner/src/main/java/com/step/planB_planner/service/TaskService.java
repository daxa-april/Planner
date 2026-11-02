package com.step.planB_planner.service;

import com.step.planB_planner.model.constant.AppStatus;
import com.step.planB_planner.model.constant.TaskStatus;
import com.step.planB_planner.model.dto.request.TaskRequestDto;
import com.step.planB_planner.model.dto.response.TaskResponseDto;
import org.springframework.data.domain.Page;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author Daria Pevets
 **/
public interface TaskService {

    TaskResponseDto createTask(TaskRequestDto taskRequestDto);

    TaskResponseDto updateTask(Long id, TaskRequestDto taskRequestDto);

    TaskStatus changeTaskStatus(Long id, TaskStatus status);

    AppStatus deleteTask(Long id);

    Page<TaskResponseDto> getTasksByRange(LocalDateTime start, LocalDateTime end, int page, int pageSize);

    Page<TaskResponseDto> getTasksByDay(LocalDate date, int page, int pageSize);

    Page<TaskResponseDto> getTasksByWeek(LocalDate date, int page, int pageSize);

    Page<TaskResponseDto> getTasksByMonth(LocalDate date, int page, int pageSize);
}
