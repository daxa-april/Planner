package com.step.planB_planner.controller;

import com.step.planB_planner.annotation.Log;
import com.step.planB_planner.constant.AppStatus;
import com.step.planB_planner.constant.TaskStatus;
import com.step.planB_planner.model.dto.request.RoutineRequestDto;
import com.step.planB_planner.model.dto.response.RoutineResponseDto;
import com.step.planB_planner.service.RoutineService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

/**
 * @author Daria Pevets
 **/
@Log
@RequiredArgsConstructor
@RestController("/api/routines/")
public class RoutineController {

    private final RoutineService routineService;

    @PostMapping("/addRoutine")
    public RoutineResponseDto createRoutine(@RequestBody RoutineRequestDto routineRequestDto) {
        return routineService.createRoutine(routineRequestDto);
    }

    @PatchMapping("/updateRoutine")
    public RoutineResponseDto updateRoutine(@RequestParam Long id, @RequestBody RoutineRequestDto routineRequestDto) {
        return routineService.updateRoutine(id, routineRequestDto);
    }

    @PatchMapping("/routineStatus")
    public TaskStatus updateRoutineStatus(@RequestParam Long id, @RequestParam TaskStatus taskStatus) {
        return routineService.updateRoutineStatus(id, taskStatus);
    }

    @DeleteMapping("/deleteRoutine")
    public AppStatus deleteRoutine(@RequestParam Long id) {
        return routineService.deleteRoutine(id);
    }

    @GetMapping("/routineDay/{date}")
    public List<RoutineResponseDto> getRoutineByDay(@PathVariable LocalDate date) {
        return routineService.getRoutineByDay(date);
    }

    @GetMapping("/routineWeek/{date}")
    public List<RoutineResponseDto> getRoutineByWeek(@PathVariable LocalDate date) {
        return routineService.getRoutineByWeek(date);
    }

    @GetMapping("/routineMonth/{date}")
    public List<RoutineResponseDto> getRoutineByMonth(@PathVariable LocalDate date) {
        return routineService.getRoutineByMonth(date);
    }
}
