package com.step.planB_planner.service.impl;

import com.step.planB_planner.exception.IllegalTimeArgument;
import com.step.planB_planner.exception.TaskException;
import com.step.planB_planner.mapper.RoutineMapper;
import com.step.planB_planner.model.constant.AppStatus;
import com.step.planB_planner.model.constant.TaskStatus;
import com.step.planB_planner.model.constant.WeekDays;
import com.step.planB_planner.model.dto.request.RoutineRequestDto;
import com.step.planB_planner.model.dto.response.RoutineResponseDto;
import com.step.planB_planner.model.entity.Routine;
import com.step.planB_planner.repository.RoutineRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * @author Daria Pevets
 **/
@Service
@RequiredArgsConstructor
public class RoutineServiceImpl implements com.step.planB_planner.service.RoutineService {

    private final RoutineRepository routineRepository;
    private final RoutineMapper routineMapper;

    @Transactional
    @Override
    public RoutineResponseDto createRoutine(RoutineRequestDto routineRequestDto) {
        if (routineRepository.findRoutineByName(routineRequestDto.name()).isPresent()) {
            throw new TaskException("Routine with name " + routineRequestDto.name() + " already exists");
        } else if (routineRepository.findRoutineByStartTime(routineRequestDto.startTime(), routineRequestDto.weekDay()).isPresent()) {
            throw new TaskException("Routine with start time " + routineRequestDto.startTime() + " already exists");
        } else if (routineRequestDto.startTime().isAfter(routineRequestDto.endTime())) {
            throw new IllegalTimeArgument("Опять хочешь все закончить еще вчера?");
        } else if (routineRequestDto.endTime().isBefore(routineRequestDto.startTime())) {
            throw new IllegalTimeArgument("Я с тобой не разговариваю...");
        } else {
            Routine routine = routineMapper.mapRoutineRequestDtoToRoutine(routineRequestDto);
            routine.setStatus(TaskStatus.NOT_STARTED);
            routineRepository.save(routine);
            return routineMapper.mapRoutineToRoutineResponseDto(routine);
        }
    }

    @Transactional
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
            throw new IllegalArgumentException("Specified routine does not exist!");
        }
    }

    @Transactional
    @Override
    public TaskStatus updateRoutineStatus(Long id, TaskStatus taskStatus) {
        if (routineRepository.existsById(id)) {
            Routine routine = routineRepository.findRoutineById(id).get();
            routine.setStatus(taskStatus);
            return taskStatus;
        } else {
            throw new IllegalArgumentException("Specified Routine does not exist!");
        }
    }

    @Override
    public AppStatus deleteRoutine(Long id) {
        if (routineRepository.findRoutineById(id).isPresent()) {
            routineRepository.deleteById(id);
            return AppStatus.SUCCESS;
        } else {
            throw new IllegalArgumentException("Specified theEnd does not exist!");
        }
    }

    public Page<RoutineResponseDto> getRoutinesByRange(LocalDateTime start, LocalDateTime end, int page, int pageSize) {
        if (start.isAfter(end)) {
            throw new IllegalArgumentException("Start date cannot be after end date");
        } else {
            Pageable pageable = PageRequest.of(page, pageSize, Sort.by("startTime").descending());
            return routineRepository.findRoutineByStartTimeBetween(start, end, pageable).map(routineMapper::mapRoutineToRoutineResponseDto);
        }

    }

    public Page<RoutineResponseDto> getRoutineByDay(LocalDate date, int page, int pageSize) {
        LocalDateTime from = date.atStartOfDay().withHour(0).withMinute(0).withSecond(0);
        LocalDateTime to = date.atStartOfDay().withHour(23).withMinute(59).withSecond(59);
        return getRoutinesByRange(from, to, page, pageSize);
    }

    public Page<RoutineResponseDto> getRoutineByDay(WeekDays weekDay, int page, int pageSize) {

        Pageable pageable = PageRequest.of(page, pageSize, Sort.by("startTime").descending());
        return routineRepository.findRoutinesByWeekDay(weekDay, pageable).map(routineMapper::mapRoutineToRoutineResponseDto);
    }

    public Page<RoutineResponseDto> getRoutineByWeek(LocalDate date, int page, int pageSize) {
        LocalDateTime from = date.atStartOfDay().withHour(0).withMinute(0).withSecond(0);
        LocalDateTime to = date.plusDays(7).atStartOfDay().withHour(23).withMinute(59).withSecond(59);
        return getRoutinesByRange(from, to, page, pageSize);
    }

    public Page<RoutineResponseDto> getRoutineByMonth(LocalDate date, int page, int pageSize) {
        LocalDateTime from = date.atStartOfDay().withHour(0).withMinute(0).withSecond(0);
        LocalDateTime to = date.plusMonths(1).atStartOfDay().withHour(23).withMinute(59).withSecond(59);
        return getRoutinesByRange(from, to, page, pageSize);
    }
}
