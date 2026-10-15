package com.step.planB_planner.service;

import com.step.planB_planner.constant.AppStatus;
import com.step.planB_planner.constant.TaskStatus;
import com.step.planB_planner.model.dto.request.DeadlineRequestDto;
import com.step.planB_planner.model.dto.response.DeadlineResponseDto;

/**
 * @author Daria Pevets
 **/
public interface DeadlineService {

    DeadlineResponseDto addDeadline(DeadlineRequestDto deadlineRequestDto);

    DeadlineResponseDto updateDeadline(Long id, DeadlineRequestDto deadlineRequestDto);

    TaskStatus updateDeadlineStatus(Long id, TaskStatus taskStatus);

    AppStatus deleteDeadline(Long id);
}
