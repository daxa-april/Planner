package com.step.planB_planner.service;

import com.step.planB_planner.constant.AppStatus;
import com.step.planB_planner.constant.TaskStatus;
import com.step.planB_planner.exception.IllegalTimeArgument;
import com.step.planB_planner.mapper.DeadlineMapper;
import com.step.planB_planner.model.dto.request.DeadlineRequestDto;
import com.step.planB_planner.model.dto.response.DeadlineResponseDto;
import com.step.planB_planner.model.entity.Deadline;
import com.step.planB_planner.repository.DeadlineRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * @author Daria Pevets
 **/
@Service
@RequiredArgsConstructor
public class DeadlineServiceImpl {

    private final DeadlineRepository deadlineRepository;
    private final DeadlineMapper deadlineMapper;

    public DeadlineResponseDto addDeadline(DeadlineRequestDto deadlineRequestDto) {
        Deadline deadline = deadlineMapper.mapDeadlineRequestDtoToDeadline(deadlineRequestDto);
        if (deadline.getDeadline().isBefore(LocalDateTime.now())) {
            throw new IllegalTimeArgument("Такой продуктивности не бывает!");
        }
        if (deadline.getWhenGot().isBefore(LocalDateTime.now())) {
            throw new IllegalTimeArgument("Я понимаю, что задача должна быть выполнена еще вчера, но будь реалистом!");
        } else if (deadline.getWhenGot().isAfter(deadline.getDeadline())) {
            throw new IllegalTimeArgument("Совсем сдурел?");
        }
        deadlineRepository.save(deadline);
        return deadlineMapper.mapDeadlineToDeadlineResponseDto(deadline);
    }

    public DeadlineResponseDto updateDeadline(Long id, DeadlineRequestDto deadlineRequestDto) {
        if (deadlineRepository.getDeadlineById(id).isPresent()) {
            Deadline deadline = deadlineRepository.getDeadlineById(id).get();
            deadline.setDeadline(deadlineRequestDto.deadLine());
            deadline.setName(deadlineRequestDto.name());
            deadline.setDescription(deadlineRequestDto.description());
            deadlineRepository.save(deadline);
            return deadlineMapper.mapDeadlineToDeadlineResponseDto(deadline);
        } else {
            throw new IllegalArgumentException("Specified deadline does not exist!");
        }
    }

    public TaskStatus updateStatus(Long id) {
        if (deadlineRepository.getDeadlineById(id).isPresent()) {
            Deadline deadline = deadlineRepository.getDeadlineById(id).get();
            deadline.setStatus(TaskStatus.notStarted);
            deadlineRepository.save(deadline);
            return deadline.getStatus();
        }
        throw new IllegalArgumentException("Specified deadline does not exist!");
    }

    public AppStatus deleteDeadline(Long id) {
        if (deadlineRepository.getDeadlineById(id).isPresent()) {
            deadlineRepository.deleteById(id);
        } else {
            throw new IllegalArgumentException("Specified deadline does not exist!");
        }
        return AppStatus.success;
    }
}
