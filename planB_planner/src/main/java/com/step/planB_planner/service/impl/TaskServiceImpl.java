package com.step.planB_planner.service.impl;

import com.step.planB_planner.exception.IllegalTimeArgument;
import com.step.planB_planner.exception.TaskException;
import com.step.planB_planner.mapper.TaskMapper;
import com.step.planB_planner.model.constant.AppStatus;
import com.step.planB_planner.model.constant.TaskStatus;
import com.step.planB_planner.model.dto.request.TaskRequestDto;
import com.step.planB_planner.model.dto.response.TaskResponseDto;
import com.step.planB_planner.model.entity.Task;
import com.step.planB_planner.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * @author Daria Pevets
 **/
@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements com.step.planB_planner.service.TaskService {

    private final TaskRepository taskRepository;
    private final TaskMapper taskMapper;

    @Transactional
    @Override
    public TaskResponseDto createTask(TaskRequestDto taskRequestDto) {
        if (taskRepository.findTaskByName(taskRequestDto.name()).isPresent()) {
            throw new TaskException("Task with name " + taskRequestDto.name() + " already exists");
        } else if (taskRepository.findTaskByTime(taskRequestDto.time()).isPresent()) {
            throw new TaskException("Task with time " + taskRequestDto.time() + " already exists");
        } else if (taskRequestDto.time().isBefore(LocalDateTime.now())) {
            throw new IllegalTimeArgument("Еще вчера задачки к сожалению не делаются...(");
        }
        Task task = taskMapper.mapTaskRequestDtoToTask(taskRequestDto);
        task.setStatus(TaskStatus.NOT_STARTED);
        taskRepository.save(task);
        return taskMapper.mapTaskToTaskResponseDto(task);
    }

    @Transactional
    public TaskResponseDto updateTask(Long id, TaskRequestDto taskRequestDto) {
        if (taskRepository.findTaskById(id).isPresent()) {
            Task task = taskRepository.findTaskById(id).get();
            task.setName(taskRequestDto.name());
            if (taskRequestDto.time().isBefore(LocalDateTime.now())) {
                throw new IllegalTimeArgument("Не пытайся обмануть меня, жалкий человек!");
            } else {
                task.setTime(taskRequestDto.time());
            }
            task.setDescription(taskRequestDto.description());
            taskRepository.save(task);
            return taskMapper.mapTaskToTaskResponseDto(task);
        } else {
            throw new TaskException("Task with id " + id + " does not exist");
        }
    }

    @Transactional
    public TaskStatus changeTaskStatus(Long id, TaskStatus status) {
        if (taskRepository.findTaskById(id).isPresent()) {
            Task task = taskRepository.findTaskById(id).get();
            task.setStatus(status);
            return status;
        } else {
            throw new TaskException("Task with id " + id + " does not exist");
        }
    }

    public AppStatus deleteTask(Long id) {
        if (taskRepository.findTaskById(id).isPresent()) {
            taskRepository.deleteById(id);
            return AppStatus.SUCCESS;
        } else {
            throw new TaskException("Task with id " + id + " does not exist");
        }
    }

    public Page<TaskResponseDto> getTasksByRange(LocalDateTime start, LocalDateTime end, int page, int pageSize) {
        if (start.isAfter(end)) {
            throw new IllegalArgumentException("Start date cannot be after end date");
        } else {
            Pageable pageable = PageRequest.of(page, pageSize, Sort.by("time").descending());
            return taskRepository.findTaskByTimeBetween(start, end, pageable).map(taskMapper::mapTaskToTaskResponseDto);
        }
    }

    public Page<TaskResponseDto> getTasksByDay(LocalDate date, int page, int pageSize) {
        LocalDateTime from = date.atStartOfDay().withHour(0).withMinute(0).withSecond(0);
        LocalDateTime to = date.atStartOfDay().withHour(23).withMinute(59).withSecond(59);
        return getTasksByRange(from, to, page, pageSize);
    }

    public Page<TaskResponseDto> getTasksByWeek(LocalDate date, int page, int pageSize) {
        LocalDateTime from = date.atStartOfDay().withHour(0).withMinute(0).withSecond(0);
        LocalDateTime to = date.plusDays(7).atStartOfDay().withHour(23).withMinute(59).withSecond(59);
        return getTasksByRange(from, to, page, pageSize);
    }

    public Page<TaskResponseDto> getTasksByMonth(LocalDate date, int page, int pageSize) {
        LocalDateTime from = date.atStartOfDay().withHour(0).withMinute(0).withSecond(0);
        LocalDateTime to = date.plusMonths(1).atStartOfDay().withHour(23).withMinute(59).withSecond(59);
        return getTasksByRange(from, to, page, pageSize);
    }
}

