package com.step.planB_planner.service.impl;

import com.step.planB_planner.constant.AppStatus;
import com.step.planB_planner.constant.TaskStatus;
import com.step.planB_planner.exception.IllegalTimeArgument;
import com.step.planB_planner.exception.TaskException;
import com.step.planB_planner.mapper.RoutineMapper;
import com.step.planB_planner.model.dto.request.RoutineRequestDto;
import com.step.planB_planner.model.dto.response.RoutineResponseDto;
import com.step.planB_planner.model.entity.Routine;
import com.step.planB_planner.repository.RoutineRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author Daria Pevets
 **/
@Service
@RequiredArgsConstructor
public class RoutineServiceImpl implements com.step.planB_planner.service.RoutineService {

    private final RoutineRepository routineRepository;
    private final RoutineMapper routineMapper;

    @Override
    public RoutineResponseDto createRoutine(RoutineRequestDto routineRequestDto) {
        if (routineRepository.findRoutineByName(routineRequestDto.name()).isPresent()) {
            throw new TaskException("Routine with name " + routineRequestDto.name() + " already exists");
        } else if (routineRepository.findRoutineByStartTime(routineRequestDto.startTime()).isPresent()) {
            throw new TaskException("Routine with start time " + routineRequestDto.startTime() + " already exists");
        } else if (routineRequestDto.startTime().isAfter(routineRequestDto.endTime())) {
            throw new IllegalTimeArgument("Опять хочешь все закончить еще вчера?");
        } else if (routineRequestDto.endTime().isBefore(routineRequestDto.startTime())) {
            throw new IllegalTimeArgument("Я с тобой не разговариваю...");
        } else {
            Routine routine = routineMapper.mapRoutineRequestDtoToRoutine(routineRequestDto);
            routineRepository.save(routine);
            return routineMapper.mapRoutineToRoutineResponseDto(routine);
        }
    }

    @Override
    public RoutineResponseDto updateRoutine(Long id, RoutineRequestDto routineRequestDto) {
        if (routineRepository.findRoutineById(id).isPresent()) {
            Routine routine = routineRepository.findRoutineById(id).get();
            routine.setName(routineRequestDto.name());
            routine.setDescription(routineRequestDto.description());
            routine.setStartTime(routineRequestDto.startTime());
            routine.setEndTime(routineRequestDto.endTime());
            routineRepository.save(routine);
            return routineMapper.mapRoutineToRoutineResponseDto(routine);
        } else {
            throw new IllegalArgumentException("Specified deadline does not exist!");
        }
    }

    @Override
    public TaskStatus updateRoutineStatus(Long id, TaskStatus taskStatus) {
        if (routineRepository.findRoutineById(id).isPresent()) {
            routineRepository.findRoutineById(id).get().setStatus(taskStatus);
            return taskStatus;
        } else {
            throw new IllegalArgumentException("Specified deadline does not exist!");
        }
    }

    @Override
    public AppStatus deleteRoutine(Long id) {
        if (routineRepository.findRoutineById(id).isPresent()) {
            routineRepository.deleteById(id);
            return AppStatus.success;
        } else {
            throw new IllegalArgumentException("Specified deadline does not exist!");
        }
    }

    public List<RoutineResponseDto> getRoutinesByRange(LocalDateTime start, LocalDateTime end) {
        if (start.isAfter(end)) {
            throw new IllegalArgumentException("Start date cannot be after end date");
        }
        List<Routine> currentTasks = routineRepository.findRoutineByStartTimeBetween(start, end).stream().toList();
        return routineMapper.mapRoutineToRoutineResponseDtoList(currentTasks);
    }

    public List<RoutineResponseDto> getRoutineByDay(LocalDate date) {
        LocalDateTime from = date.atStartOfDay().withHour(0).withMinute(0).withSecond(0);
        LocalDateTime to = date.atStartOfDay().withHour(23).withMinute(59).withSecond(59);
        return getRoutinesByRange(from, to);
    }

    public List<RoutineResponseDto> getTasksByWeek(LocalDate date) {
        LocalDateTime from = date.atStartOfDay().withHour(0).withMinute(0).withSecond(0);
        LocalDateTime to = date.plusDays(7).atStartOfDay().withHour(23).withMinute(59).withSecond(59);
        return getRoutinesByRange(from, to);
    }

    public List<RoutineResponseDto> getTasksByMonth(LocalDate date) {
        LocalDateTime from = date.atStartOfDay().withHour(0).withMinute(0).withSecond(0);
        LocalDateTime to = date.plusMonths(1).atStartOfDay().withHour(23).withMinute(59).withSecond(59);
        return getRoutinesByRange(from, to);
    }
}
