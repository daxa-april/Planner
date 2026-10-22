package com.step.planB_planner.controller;

import com.step.planB_planner.annotation.Log;
import com.step.planB_planner.constant.AppStatus;
import com.step.planB_planner.constant.TaskStatus;
import com.step.planB_planner.model.dto.request.TaskRequestDto;
import com.step.planB_planner.model.dto.response.TaskResponseDto;
import com.step.planB_planner.service.TaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

/**
 * @author Daria Pevets
 **/
@Log
@RequiredArgsConstructor
@Tag(name = "задачки", description = "простая разовая опция типа to-do по созданию самому себе проблем")
@RestController("/api/tasks/")
public class TaskController {

    private final TaskService taskService;

    @Operation(summary = "Создание новой проблемы")
    @PostMapping("/addTask")
    public TaskResponseDto createTask(@RequestBody TaskRequestDto taskRequestDto) {
        return taskService.createTask(taskRequestDto);
    }

    @Operation(summary = "эволюция проблемы")
    @PatchMapping("/updateTask")
    public TaskResponseDto updateTask(Long Id, TaskRequestDto taskRequestDto) {
        return taskService.updateTask(Id, taskRequestDto);
    }

    @Operation(summary = "наши отношения с проблемой в данный момент")
    @PatchMapping("/TaskStatus")
    public TaskStatus changeTaskStatus(Long Id, TaskStatus status) {
        return taskService.changeTaskStatus(Id, status);
    }

    @Operation(summary = "Ну его нахер состояние")
    @DeleteMapping("/delete")
    public AppStatus deleteTask(Long Id) {
        return taskService.deleteTask(Id);
    }

    @Operation(summary = "проблемы на сегодня")
    @GetMapping("/taskDay/{date}")
    public List<TaskResponseDto> getTasksByDay(@PathVariable LocalDate date) {
        return taskService.getTasksByDay(date);
    }

    @Operation(summary = "проблемы на неделю")
    @GetMapping("taskWeek/{date}")
    public List<TaskResponseDto> getTasksByWeek(@PathVariable LocalDate date) {
        return taskService.getTasksByWeek(date);
    }

    @Operation(summary = "проблемы на месяц")
    @GetMapping("/taskMonth/{date}")
    public List<TaskResponseDto> getTasksByMonth(@PathVariable LocalDate date) {
        return taskService.getTasksByMonth(date);
    }
}
