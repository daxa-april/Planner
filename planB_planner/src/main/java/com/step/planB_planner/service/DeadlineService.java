package com.step.planB_planner.service;

import com.step.planB_planner.model.constant.AppStatus;
import com.step.planB_planner.model.constant.TaskStatus;
import com.step.planB_planner.model.dto.request.DeadlineRequestDto;
import com.step.planB_planner.model.dto.response.DeadlineResponseDto;
import org.springframework.data.domain.Page;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * @author Daria Pevets
 **/
public interface DeadlineService {

    DeadlineResponseDto addDeadline(DeadlineRequestDto deadlineRequestDto);

    DeadlineResponseDto updateDeadline(Long id, DeadlineRequestDto deadlineRequestDto);

    TaskStatus updateDeadlineStatus(Long id, TaskStatus taskStatus);

    AppStatus deleteDeadline(Long id);

    Page<DeadlineResponseDto> getDeadlinesByRange(LocalDateTime start, LocalDateTime end, int page, int pageSize);

    Page<DeadlineResponseDto> getDeadlinesByDay(LocalDate day, int page, int pageSize);

    Page<DeadlineResponseDto> getDeadlinesByWeek(LocalDate day, int page, int pageSize);

    Page<DeadlineResponseDto> getDeadLinesByMonth(LocalDate day, int page, int pageSize);
}
