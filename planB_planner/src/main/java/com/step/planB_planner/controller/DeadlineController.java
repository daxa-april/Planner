package com.step.planB_planner.controller;

import com.step.planB_planner.annotation.Log;
import com.step.planB_planner.constant.AppStatus;
import com.step.planB_planner.constant.TaskStatus;
import com.step.planB_planner.model.dto.request.DeadlineRequestDto;
import com.step.planB_planner.model.dto.response.DeadlineResponseDto;
import com.step.planB_planner.service.DeadlineService;
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
public class DeadlineController {
    private final DeadlineService deadlineService;

    @PostMapping("/addDeadline")
    public DeadlineResponseDto createDeadline(@RequestBody DeadlineRequestDto deadlineRequestDto) {
        return deadlineService.addDeadline(deadlineRequestDto);
    }

    @PatchMapping("/updateDeadline")
    public DeadlineResponseDto updateDeadline(Long id, DeadlineRequestDto deadlineRequestDto) {
        return deadlineService.updateDeadline(id, deadlineRequestDto);
    }

    @DeleteMapping("/deleteDeadline")
    public AppStatus deleteDeadline(Long id) {
        return deadlineService.deleteDeadline(id);
    }

    @GetMapping("/deadlineDay/{date}")
    public List<DeadlineResponseDto> getDeadlinesByDate(@PathVariable LocalDate date) {
        return deadlineService.getDeadlinesByDay(date);
    }

    @GetMapping("/deadlineWeek/{weekStart}")
    public List<DeadlineResponseDto> getDeadlinesByWeek(@PathVariable LocalDate weekStart) {
        return deadlineService.getDeadlinesByWeek(weekStart);
    }

    @GetMapping("/deadlineMonth/{monthStart}")
    public List<DeadlineResponseDto> getDeadlinesByMonth(@PathVariable LocalDate monthStart) {
        return deadlineService.getDeadLinesByMonth(monthStart);
    }

    @PatchMapping("/deadlineStatus")
    public TaskStatus updateDeadlineStatus(@RequestParam Long id, @RequestParam TaskStatus newStatus) {
        return deadlineService.updateDeadlineStatus(id, newStatus);
    }
}
