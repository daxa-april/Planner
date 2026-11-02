package com.step.planB_planner.controller;

import com.step.planB_planner.annotation.Log;
import com.step.planB_planner.model.constant.AppStatus;
import com.step.planB_planner.model.constant.TaskStatus;
import com.step.planB_planner.model.dto.request.DeadlineRequestDto;
import com.step.planB_planner.model.dto.response.DeadlineResponseDto;
import com.step.planB_planner.service.DeadlineService;
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
@RestController("/api/deadlines/")
@Tag(name = "Дедлайны", description = "кому и чего ты должен, работяга несчастный")
public class DeadlineController {
    private final DeadlineService deadlineService;

    @Operation(summary = "новый акт мазохизма")
    @PostMapping("/addDeadline")
    public DeadlineResponseDto createDeadline(@RequestBody DeadlineRequestDto deadlineRequestDto) {
        return deadlineService.addDeadline(deadlineRequestDto);
    }

    @Operation(summary = "модификация акта мазохизма")
    @PatchMapping("/updateDeadline")
    public DeadlineResponseDto updateDeadline(Long id, DeadlineRequestDto deadlineRequestDto) {
        return deadlineService.updateDeadline(id, deadlineRequestDto);
    }

    @Operation(summary = "закончить мучения")
    @DeleteMapping("/deleteDeadline")
    public AppStatus deleteDeadline(Long id) {
        return deadlineService.deleteDeadline(id);
    }

    @Operation(summary = "акты мазохизма в конкретный день")
    @GetMapping("/deadlineDay")
    public Page<DeadlineResponseDto> getDeadlinesByDate(@RequestParam LocalDate date,
                                                        @RequestParam(defaultValue = "0") int page,
                                                        @RequestParam(defaultValue = "10") int size) {
        return deadlineService.getDeadlinesByDay(date, page, size);
    }

    @Operation(summary = "акты мазохизма в конкретную неделю")
    @GetMapping("/deadlineWeek/")
    public Page<DeadlineResponseDto> getDeadlinesByWeek(@RequestParam LocalDate weekStart,
                                                        @RequestParam(defaultValue = "0") int page,
                                                        @RequestParam(defaultValue = "10") int size) {
        return deadlineService.getDeadlinesByWeek(weekStart, page, size);
    }

    @Operation(summary = "акты мазохизма в конкретный месяц")
    @GetMapping("/deadlineMonth")
    public Page<DeadlineResponseDto> getDeadlinesByMonth(@RequestParam LocalDate monthStart,
                                                         @RequestParam(defaultValue = "0") int page,
                                                         @RequestParam(defaultValue = "10") int size) {
        return deadlineService.getDeadLinesByMonth(monthStart, page, size);
    }

    @Operation(summary = "изменить отношения с актом мазохизма")
    @PatchMapping("/deadlineStatus")
    public TaskStatus updateDeadlineStatus(@RequestParam Long id, @RequestParam TaskStatus newStatus) {
        return deadlineService.updateDeadlineStatus(id, newStatus);
    }
}
