package com.step.planB_planner.controller;

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
@RequiredArgsConstructor
@RestController("/api/deadlines/")
public class DeadlineController {
    private final DeadlineService deadlineService;

    @PostMapping("/add")
    public DeadlineResponseDto createDeadline(@RequestBody DeadlineRequestDto deadlineRequestDto) {
        return deadlineService.addDeadline(deadlineRequestDto);
    }

    @PatchMapping("/update")
    public DeadlineResponseDto updateDeadline(Long id, DeadlineRequestDto deadlineRequestDto) {
        return deadlineService.updateDeadline(id, deadlineRequestDto);
    }

    @DeleteMapping("/delete")
    public AppStatus deleteDeadline(Long id) {
        return deadlineService.deleteDeadline(id);
    }

    @GetMapping("/day/{date}")
    public List<DeadlineResponseDto> getDeadlinesByDate(@PathVariable LocalDate date) {
        return deadlineService.getDeadlinesByDay(date);
    }

    @GetMapping("/week/{weekStart}")
    public List<DeadlineResponseDto> getDeadlinesByWeek(@PathVariable LocalDate weekStart) {
        return deadlineService.getDeadlinesByWeek(weekStart);
    }

    @GetMapping("/month/{monthStart}")
    public List<DeadlineResponseDto> getDeadlinesByMonth(@PathVariable LocalDate monthStart) {
        return deadlineService.getDeadLinesByMonth(monthStart);
    }

    @PatchMapping("/status")
    public TaskStatus updateDeadlineStatus(@RequestParam Long id, @RequestParam TaskStatus newStatus) {
        return deadlineService.updateDeadlineStatus(id, newStatus);
    }
}
