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
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

/**
 * @author Daria Pevets
 **/
@ExtendWith(MockitoExtension.class)
class RoutineServiceImplTest {

    @Mock
    private RoutineRepository routineRepository;

    @Mock
    private RoutineMapper routineMapper;

    @InjectMocks
    RoutineServiceImpl routineService;

    @Nested
    class CreateRoutineTests {

        @Test
        void createRoutineSuccess() {
            RoutineRequestDto request = new RoutineRequestDto(
                    "name",
                    "desc",
                    WeekDays.MONDAY,
                    LocalDateTime.now(),
                    LocalDateTime.now().plusDays(1));

            Routine routine = new Routine();

            RoutineResponseDto response = new RoutineResponseDto(
                    1L,
                    "name",
                    "desc",
                    LocalDateTime.now(),
                    LocalDateTime.now().plusDays(1),
                    WeekDays.MONDAY,
                    TaskStatus.NOT_STARTED
            );

            when(routineRepository.findRoutineByName(request.name())).thenReturn(Optional.empty());
            when(routineRepository.findRoutineByStartTime(request.startTime(), request.weekDay())).thenReturn(Optional.empty());
            when(routineMapper.mapRoutineRequestDtoToRoutine(request)).thenReturn(routine);
            when(routineMapper.mapRoutineToRoutineResponseDto(routine)).thenReturn(response);

            RoutineResponseDto result = routineService.createRoutine(request);
            Assertions.assertNotNull(result);
            Assertions.assertEquals(TaskStatus.NOT_STARTED, routine.getStatus());
            verify(routineRepository, times(1)).save(routine);

        }

        @Test
        void createRoutineStartAfterEndTime() {

            RoutineRequestDto request = new RoutineRequestDto(
                    "name",
                    "desc",
                    WeekDays.MONDAY,
                    LocalDateTime.now().plusDays(3),
                    LocalDateTime.now().plusDays(1));

            IllegalTimeArgument exception = assertThrows(IllegalTimeArgument.class,
                    () -> routineService.createRoutine(request));
            Assertions.assertEquals("Опять хочешь все закончить еще вчера?", exception.getMessage());
        }

        @Test
        void createRoutineNameExists() {
            RoutineRequestDto request = new RoutineRequestDto(
                    "name",
                    "desc",
                    WeekDays.MONDAY,
                    LocalDateTime.now().plusDays(1),
                    LocalDateTime.now().plusDays(2));

            when(routineRepository.findRoutineByName(request.name())).thenReturn(Optional.of(new Routine()));

            TaskException exception = assertThrows(TaskException.class,
                    () -> routineService.createRoutine(request));
            Assertions.assertEquals("Routine with name " + request.name() + " already exists", exception.getMessage());
        }

        @Test
        void createRoutineTimeExists() {
            RoutineRequestDto request = new RoutineRequestDto(
                    "name",
                    "desc",
                    WeekDays.MONDAY,
                    LocalDateTime.now().plusDays(1),
                    LocalDateTime.now().plusDays(2));

            when(routineRepository.findRoutineByStartTime(request.startTime(), request.weekDay())).thenReturn(Optional.of(new Routine()));

            TaskException exception = assertThrows(TaskException.class,
                    () -> routineService.createRoutine(request));
            Assertions.assertEquals("Routine with start time " + request.startTime() + " already exists", exception.getMessage());
        }
    }

    @Nested
    class UpdateRoutineTests {

        @Test
        void updateRoutine() {
            Long routineId = 1L;
            RoutineRequestDto request = new RoutineRequestDto(
                    "new name",
                    "new desc",
                    WeekDays.FRIDAY,
                    LocalDateTime.now().plusDays(1),
                    LocalDateTime.now().plusDays(2)
            );

            Routine routine = new Routine();

            RoutineResponseDto response = new RoutineResponseDto(
                    routineId,
                    "name",
                    "desc",
                    LocalDateTime.now().plusDays(1),
                    LocalDateTime.now().plusDays(2),
                    WeekDays.FRIDAY,
                    TaskStatus.NOT_STARTED
            );

            when(routineRepository.findRoutineById(routineId)).thenReturn(Optional.of(routine));
            when(routineMapper.mapRoutineToRoutineResponseDto(routine)).thenReturn(response);

            RoutineResponseDto result = routineService.updateRoutine(routineId, request);

            Assertions.assertNotNull(result);
            Assertions.assertEquals("new name", routine.getName());
            Assertions.assertEquals("new desc", routine.getDescription());
            Assertions.assertEquals(request.startTime(), result.startTime());
            Assertions.assertEquals(request.weekDay(), result.weekDay());
            verify(routineRepository, times(1)).save(routine);
        }

        @Test
        void updateRoutineNotFound() {

            Long routineId = 1L;
            RoutineRequestDto request = new RoutineRequestDto(
                    " new name",
                    "new desc",
                    WeekDays.FRIDAY,
                    LocalDateTime.now().plusDays(1),
                    LocalDateTime.now().plusDays(2)
            );
            when(routineRepository.findRoutineById(routineId)).thenReturn(Optional.empty());

            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                    () -> routineService.updateRoutine(routineId, request));
            Assertions.assertEquals("Specified routine does not exist!", exception.getMessage());
        }
    }

    @Nested
    class UpdateRoutineStatusTests {

        @Test
        void updateRoutineStatus() {

            Long routineId = 1L;
            TaskStatus status = TaskStatus.COMPLETED;

            Routine routine = new Routine();
            routine.setStatus(TaskStatus.NOT_STARTED);

            when(routineRepository.existsById(routineId)).thenReturn(true);
            when(routineRepository.findRoutineById(routineId)).thenReturn(Optional.of(routine));

            TaskStatus result = routineService.updateRoutineStatus(routineId, status);

            Assertions.assertNotNull(result);
            Assertions.assertEquals(status, result);
        }

        @Test
        void UpdateRoutineStatusNotFound() {
            Long routineId = 1L;

            when(routineRepository.existsById(routineId)).thenReturn(false);

            assertThrows(IllegalArgumentException.class,
                    () -> routineService.updateRoutineStatus(routineId, TaskStatus.NOT_STARTED));
        }
    }

    @Nested
    class DeleteRoutineTests {

        @Test
        void deleteRoutine() {

            Long routineId = 1L;
            Routine routine = new Routine();

            when(routineRepository.findRoutineById(routineId)).thenReturn(Optional.of(routine));

            AppStatus result = routineService.deleteRoutine(routineId);

            Assertions.assertEquals(AppStatus.SUCCESS, result);
            verify(routineRepository, times(1)).deleteById(routineId);
        }

        @Test
        void deleteRoutineNotFound() {
            Long routineId = 1L;
            when(routineRepository.findRoutineById(routineId)).thenReturn(Optional.empty());
            assertThrows(IllegalArgumentException.class,
                    () -> routineService.deleteRoutine(routineId));
        }
    }

    @Nested
    class DateRangeTests {

        @Test
        void getRoutinesByRange() {

            LocalDateTime start = LocalDateTime.now();
            LocalDateTime end = LocalDateTime.now().plusDays(1);
            int page = 0;
            int pageSize = 10;

            Pageable pageable = PageRequest.of(page, pageSize, Sort.by("startTime").descending());
            List<Routine> mockList = Collections.singletonList(new Routine());
            Page<Routine> mockPage = new PageImpl<>(mockList, pageable, mockList.size());
            RoutineResponseDto response = new RoutineResponseDto(
                    1L,
                    "name",
                    "desc",
                    LocalDateTime.now().plusDays(1),
                    LocalDateTime.now().plusDays(2),
                    WeekDays.FRIDAY,
                    TaskStatus.NOT_STARTED
            );

            when(routineRepository.findRoutineByStartTimeBetween(start, end, pageable)).thenReturn(mockPage);
            when(routineMapper.mapRoutineToRoutineResponseDto(any(Routine.class))).thenReturn(response);

            Page<RoutineResponseDto> result = routineService.getRoutinesByRange(start, end, page, pageSize);

            Assertions.assertNotNull(result);
            Assertions.assertEquals(1, result.getTotalElements());
            Assertions.assertEquals(1, result.getContent().size());
            //Todo: finish unit tests.
        }

        @Test
        void getRoutinesByRangeNotFound() {
            LocalDateTime start = LocalDateTime.now().plusDays(1);
            LocalDateTime end = LocalDateTime.now();
            int page = 1;
            int pageSize = 10;

            assertThrows(IllegalArgumentException.class,
                    () -> routineService.getRoutinesByRange(start, end, page, pageSize));
        }
    }
}