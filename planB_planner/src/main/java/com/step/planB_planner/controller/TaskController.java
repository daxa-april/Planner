package com.step.planB_planner.controller;

import com.step.planB_planner.annotation.Log;
import com.step.planB_planner.model.constant.AppStatus;
import com.step.planB_planner.model.constant.TaskStatus;
import com.step.planB_planner.model.dto.request.TaskRequestDto;
import com.step.planB_planner.model.dto.response.TaskResponseDto;
import com.step.planB_planner.service.TaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

/**
 * @author Daria Pevets
 **/
@Log
@RequiredArgsConstructor
@Tag(name = "tasks", description = "to-do")
@RestController("/api/tasks/")
public class TaskController {

    private final TaskService taskService;

    @Operation(summary = "create task")
    @PostMapping("/addTask")
    public TaskResponseDto createTask(@RequestBody TaskRequestDto taskRequestDto) {
        return taskService.createTask(taskRequestDto);
    }

    @Operation(summary = "update task")
    @PatchMapping("/updateTask")
    public TaskResponseDto updateTask(Long Id, TaskRequestDto taskRequestDto) {
        return taskService.updateTask(Id, taskRequestDto);
    }

    @Operation(summary = "update task status")
    @PatchMapping("/TaskStatus")
    public TaskStatus changeTaskStatus(Long Id, TaskStatus status) {
        return taskService.changeTaskStatus(Id, status);
    }

    @Operation(summary = "delete task")
    @DeleteMapping("/delete")
    public AppStatus deleteTask(Long Id) {
        return taskService.deleteTask(Id);
    }

    @Operation(summary = "tasks for today")
    @GetMapping("/taskDay")
    public Page<TaskResponseDto> getTasksByDay(@RequestParam LocalDate date,
                                               @RequestParam(defaultValue = "0") int page,
                                               @RequestParam(defaultValue = "10") int size) {
        return taskService.getTasksByDay(date, page, size);
    }

    @Operation(summary = "tasks for a week")
    @GetMapping("taskWeek")
    public Page<TaskResponseDto> getTasksByWeek(@RequestParam LocalDate date,
                                                @RequestParam(defaultValue = "0") int page,
                                                @RequestParam(defaultValue = "10") int size) {
        return taskService.getTasksByWeek(date, page, size);
    }

    @Operation(summary = "tasks for a month")
    @GetMapping("/taskMonth")
    public Page<TaskResponseDto> getTasksByMonth(@RequestParam LocalDate date,
                                                 @RequestParam(defaultValue = "0") int page,
                                                 @RequestParam(defaultValue = "10") int size) {
        return taskService.getTasksByMonth(date, page, size);
    }
}
