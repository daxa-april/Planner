package com.step.planB_planner.controller;

import com.step.planB_planner.annotation.Log;
import com.step.planB_planner.constant.AppStatus;
import com.step.planB_planner.constant.TaskStatus;
import com.step.planB_planner.model.dto.request.TaskRequestDto;
import com.step.planB_planner.model.dto.response.TaskResponseDto;
import com.step.planB_planner.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

/**
 * @author Daria Pevets
 **/
@Log
@RequiredArgsConstructor
@RestController("/api/tasks/")
public class TaskController {

    private final TaskService taskService;

    @PostMapping("/addTask")
    public TaskResponseDto createTask(@RequestBody TaskRequestDto taskRequestDto) {
        return taskService.createTask(taskRequestDto);
    }

    @PatchMapping("/updateTask")
    public TaskResponseDto updateTask(Long Id, TaskRequestDto taskRequestDto) {
        return taskService.updateTask(Id, taskRequestDto);
    }

    @PatchMapping("/TaskStatus")
    public TaskStatus changeTaskStatus(Long Id, TaskStatus status) {
        return taskService.changeTaskStatus(Id, status);
    }

    @DeleteMapping("/delete")
    public AppStatus deleteTask(Long Id) {
        return taskService.deleteTask(Id);
    }

    @GetMapping("/taskDay/{date}")
    public List<TaskResponseDto> getTasksByDay(@PathVariable LocalDate date) {
        return taskService.getTasksByDay(date);
    }

    @GetMapping("taskWeek/{date}")
    public List<TaskResponseDto> getTasksByWeek(@PathVariable LocalDate date) {
        return taskService.getTasksByWeek(date);
    }

    @GetMapping("/taskMonth/{date}")
    public List<TaskResponseDto> getTasksByMonth(@PathVariable LocalDate date) {
        return taskService.getTasksByMonth(date);
    }
}
