package com.step.planB_planner.controller;

import com.step.planB_planner.annotation.Log;
import com.step.planB_planner.model.constant.AppStatus;
import com.step.planB_planner.model.constant.TaskStatus;
import com.step.planB_planner.model.dto.request.RoutineRequestDto;
import com.step.planB_planner.model.dto.response.RoutineResponseDto;
import com.step.planB_planner.service.RoutineService;
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
@Tag(name = "серые будни", description = "твоя унылая повседневность")
@RestController("/api/routines/")
public class RoutineController {

    private final RoutineService routineService;

    @Operation(summary = "добавить скучную батрачку")
    @PostMapping("/addRoutine")
    public RoutineResponseDto createRoutine(@RequestBody RoutineRequestDto routineRequestDto) {
        return routineService.createRoutine(routineRequestDto);
    }

    @Operation(summary = "изменить скучную батрачку")
    @PatchMapping("/updateRoutine")
    public RoutineResponseDto updateRoutine(@RequestParam Long id, @RequestBody RoutineRequestDto routineRequestDto) {
        return routineService.updateRoutine(id, routineRequestDto);
    }

    @Operation(summary = "изменить отношения с батрачкой")
    @PatchMapping("/routineStatus")
    public TaskStatus updateRoutineStatus(@RequestParam Long id, @RequestParam TaskStatus taskStatus) {
        return routineService.updateRoutineStatus(id, taskStatus);
    }

    @Operation(summary = "удалить скучную батрачку")
    @DeleteMapping("/deleteRoutine")
    public AppStatus deleteRoutine(@RequestParam Long id) {
        return routineService.deleteRoutine(id);
    }

    @Operation(summary = "батрачка на день")
    @GetMapping("/routineDay/{date}")
    public List<RoutineResponseDto> getRoutineByDay(@PathVariable LocalDate date) {
        return routineService.getRoutineByDay(date);
    }

    @Operation(summary = "батрачка на неделю")
    @GetMapping("/routineWeek/{date}")
    public List<RoutineResponseDto> getRoutineByWeek(@PathVariable LocalDate date) {
        return routineService.getRoutineByWeek(date);
    }

    @Operation(summary = "батрачка на месяц")
    @GetMapping("/routineMonth/{date}")
    public List<RoutineResponseDto> getRoutineByMonth(@PathVariable LocalDate date) {
        return routineService.getRoutineByMonth(date);
    }
}
