package com.step.planB_planner.service.impl;

import com.step.planB_planner.constant.AppStatus;
import com.step.planB_planner.constant.TaskStatus;
import com.step.planB_planner.exception.IllegalTimeArgument;
import com.step.planB_planner.exception.TaskException;
import com.step.planB_planner.mapper.TaskMapper;
import com.step.planB_planner.model.dto.request.TaskRequestDto;
import com.step.planB_planner.model.dto.response.TaskResponseDto;
import com.step.planB_planner.model.entity.Task;
import com.step.planB_planner.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * @author Daria Pevets
 **/
@Service
@RequiredArgsConstructor
public class TaskServiceImpl {

    private final TaskRepository taskRepository;
    private final TaskMapper taskMapper;

    public TaskResponseDto createTask(TaskRequestDto taskRequestDto) {
        if (taskRepository.findTaskByName(taskRequestDto.name()).isPresent()) {
            throw new TaskException("Task with name " + taskRequestDto.name() + " already exists");
        } else if (taskRepository.findTaskByTime(taskRequestDto.time()).isPresent()) {
            throw new TaskException("Task with time " + taskRequestDto.time() + " already exists");
        } else if (taskRequestDto.time().isBefore(LocalDateTime.now())) {
            throw new IllegalTimeArgument("Еще вчера задачки к сожалению не делаются...(");
        }
        Task task = taskMapper.mapTaskRequestDtoToTask(taskRequestDto);
        taskRepository.save(task);
        return taskMapper.mapTaskToTaskResponseDto(task);
    }

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

    public TaskStatus changeTaskStatus(Long id, TaskStatus status) {
        if (taskRepository.findTaskById(id).isPresent()) {
            taskRepository.findTaskById(id).get().setStatus(status);
            return status;
        } else {
            throw new TaskException("Task with id " + id + " does not exist");
        }
    }

    public AppStatus deleteTask(Long id) {
        if (taskRepository.findTaskById(id).isPresent()) {
            taskRepository.deleteById(id);
            return AppStatus.success;
        } else {
            throw new TaskException("Task with id " + id + " does not exist");
        }
    }
}

