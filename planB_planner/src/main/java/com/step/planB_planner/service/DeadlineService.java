package com.step.planB_planner.service;

import com.step.planB_planner.model.constant.AppStatus;
import com.step.planB_planner.model.constant.TaskStatus;
import com.step.planB_planner.model.dto.request.DeadlineRequestDto;
import com.step.planB_planner.model.dto.response.DeadlineResponseDto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author Daria Pevets
 **/
public interface DeadlineService {

    DeadlineResponseDto addDeadline(DeadlineRequestDto deadlineRequestDto);

    DeadlineResponseDto updateDeadline(Long id, DeadlineRequestDto deadlineRequestDto);

    TaskStatus updateDeadlineStatus(Long id, TaskStatus taskStatus);

    AppStatus deleteDeadline(Long id);

    List<DeadlineResponseDto> getDeadlinesByRange(LocalDateTime start, LocalDateTime end);

    List<DeadlineResponseDto> getDeadlinesByDay(LocalDate day);

    List<DeadlineResponseDto> getDeadlinesByWeek(LocalDate day);

    List<DeadlineResponseDto> getDeadLinesByMonth(LocalDate day);
}
