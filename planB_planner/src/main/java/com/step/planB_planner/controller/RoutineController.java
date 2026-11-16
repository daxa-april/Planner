package com.step.planB_planner.controller;

import com.step.planB_planner.annotation.Log;
import com.step.planB_planner.model.constant.AppStatus;
import com.step.planB_planner.model.constant.TaskStatus;
import com.step.planB_planner.model.constant.WeekDays;
import com.step.planB_planner.model.dto.request.RoutineRequestDto;
import com.step.planB_planner.model.dto.response.RoutineResponseDto;
import com.step.planB_planner.service.RoutineService;
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
@Tag(name = "routine", description = "daily activities and timetable")
@RestController("/api/routines/")
public class RoutineController {

    private final RoutineService routineService;

    @Operation(summary = "add routine")
    @PostMapping("/addRoutine")
    public RoutineResponseDto createRoutine(@RequestBody RoutineRequestDto routineRequestDto) {
        return routineService.createRoutine(routineRequestDto);
    }

    @Operation(summary = "update routine")
    @PatchMapping("/updateRoutine")
    public RoutineResponseDto updateRoutine(@RequestParam Long id, @RequestBody RoutineRequestDto routineRequestDto) {
        return routineService.updateRoutine(id, routineRequestDto);
    }

    @Operation(summary = "change status")
    @PatchMapping("/routineStatus")
    public TaskStatus updateRoutineStatus(@RequestParam Long id, @RequestParam TaskStatus taskStatus) {
        return routineService.updateRoutineStatus(id, taskStatus);
    }

    @Operation(summary = "delete routine")
    @DeleteMapping("/deleteRoutine")
    public AppStatus deleteRoutine(@RequestParam Long id) {
        return routineService.deleteRoutine(id);
    }

    @Operation(summary = "today's plan")
    @GetMapping("/routineDay")
    public Page<RoutineResponseDto> getRoutineByDay(@RequestParam LocalDate date,
                                                    @RequestParam(defaultValue = "0") int page,
                                                    @RequestParam(defaultValue = "10") int size) {
        return routineService.getRoutineByDay(date, page, size);
    }

    @Operation(summary = "plans by day of the week")
    @GetMapping("/routineDayOfWeek")
    public Page<RoutineResponseDto> getRoutineByDay(@RequestParam WeekDays weekDay,
                                                    @RequestParam(defaultValue = "0") int page,
                                                    @RequestParam(defaultValue = "10") int size) {
        return routineService.getRoutineByDay(weekDay, page, size);
    }

    @Operation(summary = "week's plan")
    @GetMapping("/routineWeek")
    public Page<RoutineResponseDto> getRoutineByWeek(@RequestParam LocalDate date,
                                                     @RequestParam(defaultValue = "0") int page,
                                                     @RequestParam(defaultValue = "10") int size) {
        return routineService.getRoutineByWeek(date, page, size);
    }

    @Operation(summary = "month's plan")
    @GetMapping("/routineMonth")
    public Page<RoutineResponseDto> getRoutineByMonth(@RequestParam LocalDate date,
                                                      @RequestParam(defaultValue = "0") int page,
                                                      @RequestParam(defaultValue = "10") int size) {
        return routineService.getRoutineByMonth(date, page, size);
    }
}
