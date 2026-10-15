package com.step.planB_planner.service.impl;

import com.step.planB_planner.constant.AppStatus;
import com.step.planB_planner.constant.TaskStatus;
import com.step.planB_planner.exception.IllegalTimeArgument;
import com.step.planB_planner.exception.TaskException;
import com.step.planB_planner.mapper.DeadlineMapper;
import com.step.planB_planner.model.dto.request.DeadlineRequestDto;
import com.step.planB_planner.model.dto.response.DeadlineResponseDto;
import com.step.planB_planner.model.entity.Deadline;
import com.step.planB_planner.repository.DeadlineRepository;
import com.step.planB_planner.service.DeadlineService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * @author Daria Pevets
 **/
@Service
@RequiredArgsConstructor
public class DeadlineServiceImpl implements DeadlineService {

    private final DeadlineRepository deadlineRepository;
    private final DeadlineMapper deadlineMapper;

    @Override
    public DeadlineResponseDto addDeadline(DeadlineRequestDto deadlineRequestDto) {

        if (deadlineRequestDto.deadLine().isBefore(LocalDateTime.now())) {
            throw new IllegalTimeArgument("Такой продуктивности не бывает!");
        } else if (deadlineRequestDto.whenGot().isBefore(LocalDateTime.now())) {
            throw new IllegalTimeArgument("Я понимаю, что задача должна быть выполнена еще вчера, но будь реалистом!");
        } else if (deadlineRequestDto.whenGot().isAfter(deadlineRequestDto.deadLine())) {
            throw new IllegalTimeArgument("Совсем сдурел?");
        } else if (deadlineRepository.getDeadlineByName(deadlineRequestDto.name()).isPresent()) {
            throw new TaskException("А ты очень забывчив(а) однако!");
        } else if (deadlineRepository.getDeadlineByWhenGot(deadlineRequestDto.whenGot()).isPresent()) {
            throw new TaskException("Может таблеточки попить?");
        } else {
            Deadline deadline = deadlineMapper.mapDeadlineRequestDtoToDeadline(deadlineRequestDto);
            deadlineRepository.save(deadline);
            return deadlineMapper.mapDeadlineToDeadlineResponseDto(deadline);
        }
    }

    @Override
    public DeadlineResponseDto updateDeadline(Long id, DeadlineRequestDto deadlineRequestDto) {
        if (deadlineRepository.getDeadlineById(id).isPresent()) {
            Deadline deadline = deadlineRepository.getDeadlineById(id).get();
            if (deadlineRequestDto.deadLine().isBefore(LocalDateTime.now())) {
                throw new IllegalTimeArgument("Не пытайся обмануть меня, человек!");
            } else {
                deadline.setDeadline(deadlineRequestDto.deadLine());
            }
            deadline.setName(deadlineRequestDto.name());
            deadline.setDescription(deadlineRequestDto.description());
            deadlineRepository.save(deadline);
            return deadlineMapper.mapDeadlineToDeadlineResponseDto(deadline);
        } else {
            throw new IllegalArgumentException("Specified deadline does not exist!");
        }
    }

    @Override
    public TaskStatus updateDeadlineStatus(Long id, TaskStatus taskStatus) {
        if (deadlineRepository.getDeadlineById(id).isPresent()) {
            deadlineRepository.getDeadlineById(id).get().setStatus(taskStatus);
        }
        throw new IllegalArgumentException("Specified deadline does not exist!");
    }

    @Override
    public AppStatus deleteDeadline(Long id) {
        if (deadlineRepository.getDeadlineById(id).isPresent()) {
            deadlineRepository.deleteById(id);
            return AppStatus.success;
        } else {
            throw new IllegalArgumentException("Specified deadline does not exist!");
        }
    }
}
