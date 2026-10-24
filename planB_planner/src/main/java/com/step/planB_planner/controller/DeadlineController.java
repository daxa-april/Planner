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
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

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
    @GetMapping("/deadlineDay/{date}")
    public List<DeadlineResponseDto> getDeadlinesByDate(@PathVariable LocalDate date) {
        return deadlineService.getDeadlinesByDay(date);
    }

    @Operation(summary = "акты мазохизма в конкретную неделю")
    @GetMapping("/deadlineWeek/{weekStart}")
    public List<DeadlineResponseDto> getDeadlinesByWeek(@PathVariable LocalDate weekStart) {
        return deadlineService.getDeadlinesByWeek(weekStart);
    }

    @Operation(summary = "акты мазохизма в конкретный месяц")
    @GetMapping("/deadlineMonth/{monthStart}")
    public List<DeadlineResponseDto> getDeadlinesByMonth(@PathVariable LocalDate monthStart) {
        return deadlineService.getDeadLinesByMonth(monthStart);
    }

    @Operation(summary = "изменить отношения с актом мазохизма")
    @PatchMapping("/deadlineStatus")
    public TaskStatus updateDeadlineStatus(@RequestParam Long id, @RequestParam TaskStatus newStatus) {
        return deadlineService.updateDeadlineStatus(id, newStatus);
    }
}
